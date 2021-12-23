package model;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Gestor {

    public ArrayList<Process> processes;
    public ArrayList<Memory> memories;
    private Color[] c = {Color.BLUE,Color.RED,Color.MAGENTA,Color.ORANGE,Color.CYAN, Color.GREEN};
    String filename;
    ByteArrayOutputStream baos;
    PrintStream ps, old;

    public Gestor(String filename){
        processes = new ArrayList<Process>();
        memories = new ArrayList<Memory>();
        this.filename = filename;
        System.out.println(filename);
    }

    public void resetMemory(){
        memories.clear();
    }

    public void addProcesses() {

        try {
            File file = new File(filename);
            @SuppressWarnings("resource")
            Scanner scan = new Scanner(file);
            int j = 0;
            while(scan.hasNextLine()) {
                String s[]=scan.nextLine().split(" ");
                //						   NAME     	ARRIVE				MEMORY					DURATION
                processes.add(new Process( s[0],Integer.parseInt(s[1]),Integer.parseInt(s[2]),Integer.parseInt(s[3]),c[j]));
                j++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

    }

    public void addMemories(){
        for (int i = 0; i < getNumMemories(); i++)
            memories.add(new Memory());
    }


    public boolean spaceInNewEmpty(int space){

        for (Process p : processes){
            if (p.getDuration() > 0) {
                if (space >= p.getMemory()){
                    return true;
                }
            }
        }

        return false;
    }

    public boolean wasNotAsigned(int k, Process p){
        for (int i = 0; i < memories.size();i++){
            if (i == k && k!=0){
                if (memories.get(i-1).containsProcess(p)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method uses Memory.bestSlot which will determine the best gap for each process and check if its
     * duration has reached 0
     */
    public void bestGap(){
        addMemories();
        Queue<Process> queue = new LinkedList<>();
        int j ;
        standardIO2Stream(); //Changes exit standard to Stream


        for (int i = 0; i < memories.size(); i++){

            // we add to the queue the processes when the time for them to execute comes
            for (Process p : processes){
                //System.out.println(p.getName()+" " +p.getPosition());
                if (p.getArrive()==i){
                    queue.add(p);
                }
            }

            // we remove from the queue the processes that are not executing anymore
            for (Process p : processes) {
                if (p.getDuration() == 0) {
                    memories.get(i).bestSlot(p);
                    try {
                        memories.get(i).removeProcess(p);
                    } catch (ProcessNotFound e) {
                    }
                    if (queue.contains(p)) {
                        queue.remove(p);
                    }
                }
            }
            //System.out.println("¢¢¢¢¢¢");
            //System.out.println(memories.get(i));
            //System.out.println("¢¢¢¢¢¢");
            // TODO assign instead of depending on the number of slot from last memory,
            //  depending on the number of the total of slots. Do a pull if the number of slots decreases
            // for every process that is stored in the queue, we assign them their position
            for (Process p : queue){
                if (memories.get(i).setInSlot(p.getPosition(),p))
                    p.setDuration(-1);

            }


            // then for every process in queue that has not been assigned yet, we add them to the memory
            for (Process p : queue){
                if(p.getDuration()!=0){
                    if(memories.get(i).bestSlot(p)) {
                        p.setDuration(-1);
                    }

                }
            }

            // if there's still processes on queue when the predicted number of memories is at its limit,
            // the number of memories will increase
            if (i==memories.size()-1 && queue.size()!=0) {
                memories.add(new Memory());
            }

            memories.get(i).joinEmptySlots();

            System.out.println(i+" "+memories.get(i));

        }


        String sout = Stream2StandardIO(); //Cambia salida de Stream a la consola
        try {
            writeResults2File(sout,"bestGap");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void nextGap(){
        addMemories();
        Queue<Process> queue = new LinkedList<>();
        int j ;
        ArrayList<Integer> positions = new ArrayList<>();
        standardIO2Stream(); //Changes exit standard to Stream

        for (int i = 0; i < memories.size(); i++){

            // we add to the queue the processes when the time for them to execute comes
            for (Process p : processes){
                //System.out.println(p.getName()+" " +p.getPosition());
                if (p.getArrive()==i){
                    queue.add(p);
                    positions.add(p.getPosition());
                }
            }

            int k = 0;
            // we remove from the queue the processes that are not executing anymore
            for (Process p : processes) {
                if (p.getDuration() == 0) {
                    memories.get(i).setInSlot(positions.get(k),p);
                    //System.out.println(k);
                    memories.get(k).setNextSlotPos(p.getPosition());

                    try {
                        memories.get(i).removeProcess(p);
                    } catch (ProcessNotFound e) {
                    }
                    if (queue.contains(p)) {
                        queue.remove(p);
                    }
                    k++;
                }
                else
                    k++;
            }
            //System.out.println( memories.get(k).getNextPos());
            //System.out.println("¢¢¢¢¢¢");
            //System.out.println(memories.get(i));
            //System.out.println("¢¢¢¢¢¢");
            // TODO assign instead of depending on the number of slot from last memory,
            //  depending on the number of the total of slots. Do a pull if the number of slots decreases
            // for every process that is stored in the queue, we assign them their position
            for (Process p : queue){

                if (memories.get(i).setInSlot(p.getPosition(),p)) {
                    p.setDuration(-1);
                    memories.get(i).setNextSlotPos(p.getPosition());
                }
            }


            // then for every process in queue that has not been assigned yet, we add them to the memory
            for (Process p : queue){
                if(p.getDuration()!=0){
                    if(memories.get(i).nextSlot(p)) {
                        p.setDuration(-1);
                    }

                }
            }

            // if there's still processes on queue when the predicted number of memories is at its limit,
            // the number of memories will increase
            if (i==memories.size()-1 && queue.size()!=0) {
                memories.add(new Memory());
            }

            memories.get(i).joinEmptySlots();

            System.out.println(i+" "+memories.get(i));
            //System.out.println(memories.get(i).getNextPos());
        }

        String sout = Stream2StandardIO(); //Changes exit from Stream to terminal
        try {
            writeResults2File(sout,"nextGap");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Determines the number of memories needed in base of the duration plus the moment it arrives
     * @return its size
     */
    public int getNumMemories(){

        int num = 0;
        for (Process p : processes) {
            if (p.getDuration()+p.getArrive()>num)
                num=p.getDuration()+p.getArrive();
        }

        return num+1;
    }

    public String showProcesses(){
        StringBuilder s = new StringBuilder("");
        for (Process p : processes)
            s.append(p).append("\n");
        return s+"";
    }

    public int getMemoriesSize(){
        return memories.size();
    }

    public String showMemories(){
        StringBuilder s = new StringBuilder("");
        for (int i = 0;i < memories.size(); i++) {
            s.append(memories.get(i).showMemory()).append("\n");
        }//TODO add color java.awt from string to MyAPP.class
        return s+"";
    }

    private void writeResults2File(String toWrite, String name) throws IOException {

        File myObj = new File("src/Files/"+name+"(result).txt");

        if (myObj.createNewFile())
            System.out.println("File created: " + myObj.getName());

        FileWriter myWriter = new FileWriter("src/Files/"+name+"(result).txt");
        myWriter.write(toWrite);
        myWriter.close();
    }

    private  void standardIO2Stream(){
        baos = new ByteArrayOutputStream();
        ps = new PrintStream(baos);
        old = System.out;
        System.setOut(ps);
    }

    private String Stream2StandardIO(){
        System.out.flush();
        System.setOut(old); // standard exit
        return (baos.toString());
    }
}
