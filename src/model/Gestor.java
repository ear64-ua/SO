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
    private final Color[] c = {Color.BLUE,Color.RED,Color.MAGENTA,Color.ORANGE,Color.CYAN, Color.GREEN};
    String filename;
    ByteArrayOutputStream baos;
    PrintStream ps, old;

    public Gestor(String filename){
        processes = new ArrayList<>();
        memories = new ArrayList<>();
        this.filename = filename;
        System.out.println(filename);
    }

    public void addProcesses() {

        try {
            File file = new File(filename);
            @SuppressWarnings("resource")
            Scanner scan = new Scanner(file);
            int j = 0;
            while(scan.hasNextLine()) {
                String[] s =scan.nextLine().split(" ");
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

    /**
     * This method uses Memory.bestSlot which will determine the best gap for each process and check if its
     * duration has reached 0
     */
    public void bestGap(){
        addMemories();
        Queue<Process> queue = new LinkedList<>();
        standardIO2Stream(); //Changes exit standard to Stream

        for (int i = 0; i < memories.size(); i++){
            // the last memory will be equal to this
            Memory m = i != 0 ? new Memory(memories.get(i-1)) : new Memory();
            checkProcess(queue, i);

            m = executionBestGap(queue, i, m);
            m.joinEmptySlots();

            m =  assignBestGap(queue, i, m);

            m.joinEmptySlots();
            memories.set(i,m);
            System.out.println(i+": "+memories.get(i));
        }

        String sout = Stream2StandardIO(); //Changes the exit from Stream to console
        try {
            writeResults2File(sout,"bestGap");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * We add to the queue the processes when the time for them to execute comes
     * @param queue of processes
     * @param i index of memory
     */
    private void checkProcess(Queue<Process> queue, int i) {
        for (Process p : processes){
            if (p.getArrive()== i){
                queue.add(p);
            }
        }
    }

    /**
     * For every process in queue that has not been assigned yet, we add them to the memory
     * @param queue of processes
     * @param i index of memory
     */
    private Memory assignBestGap(Queue<Process> queue, int i, Memory m) {
        for (Process p : queue){
            if(p.getDuration()!=0 && !m.containsProcess(p)){
                if(m.bestSlot(p)){
                    p.setDuration(-1);
                }

            }
        }
        return m;
    }

    /**
     *  We remove from the queue the processes that are not executing anymore,
     *  remove from memory the processes that are no longer executing
     *  and remove one unit to the duration of the processes that are still running
     * @param queue of processes
     * @param i index of memory
     */
    private Memory executionBestGap(Queue<Process> queue, int i, Memory m) {
        for (Process p : processes) {
            if (p.getDuration() == 0) {
                try {
                    m.removeProcess(p);
                } catch (ProcessNotFound ignored) {
                }
                queue.remove(p);
            }
            if (p.getDuration()!=0 && m.containsProcess(p)){
                p.setDuration(-1);
            }
        }
        return m;
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
            //System.out.println("###################");
            //System.out.println(memories.get(i));
            // we remove from the queue the processes that are not executing anymore
            for (Process p : processes) {
                if (p.getDuration() == 0) {
                    //memories.get(i).setInSlot(positions.get(k),p);
                    //System.out.println(k);
                    //if(k==0){
                      //  memories.get(i).bestSlot(p);
                    //}
                    //memories.get(i).nextSlot(p);

                    queue.remove(p);
                    k++;
                }
                else
                    k++;
            }
            //System.out.println(memories.get(i));
            //System.out.println("###################");

            memories.get(i).joinEmptySlots();
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

            for(Process p : processes){
                if (p.getDuration()==0){
                    try {
                        memories.get(i).removeProcess(p);
                    } catch (ProcessNotFound ignored) {
                    }
                }
            }

            memories.get(i).joinEmptySlots();

            System.out.println(i+" "+memories.get(i));
            //System.out.println(memories.get(i).getNextPos());
        }

        String sout = Stream2StandardIO(); //Changes exit from Stream to console
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
        StringBuilder s = new StringBuilder();
        for (Process p : processes)
            s.append(p).append("\n");
        return s+"";
    }

    public String showMemories(){
        StringBuilder s = new StringBuilder();
        for (Memory memory : memories) {
            s.append(memory.showMemory()).append("\n");
        }
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

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for (Memory memory : memories) {
            s.append(memory.toString()).append("\n");
        }
        return s+"";
    }

}
