package model;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Class that manages the processes inside an array of memories depending on the algorithm used
 * @author Enrique Abma Romero
 */
public class Gestor {

    public ArrayList<Process> processes;
    public ArrayList<Memory> memories;
    private final Color[] c = {Color.BLUE,Color.RED,Color.MAGENTA,Color.ORANGE,Color.CYAN, Color.GREEN};
    String filename;
    ByteArrayOutputStream baos;
    PrintStream ps, old;

    /**
     * Constructor of the class
     * @param filename name of the file in which the processes will be read
     */
    public Gestor(String filename){
        processes = new ArrayList<>();
        memories = new ArrayList<>();
        this.filename = filename;
        System.out.println(filename);
    }

    /**
     * Adds processes to the processes arrayList
     */
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

    /**
     * Adds memories depending on the greater duration of a process
     */
    public void addMemories(){
        for (int i = 0; i < getNumMemories(); i++)
            memories.add(new Memory());
    }

    /**
     * This method uses Memory.bestSlot for each memory which will determine the best gap for each process
     * It prints out to the stream the memories created
     */
    public void bestGap(){
        addMemories();
        Queue<Process> queue = new LinkedList<>();
        standardIO2Stream(); //Changes exit standard to Stream

        for (int i = 0; i < memories.size(); i++){
            // the last memory will be equal to this
            Memory m = i != 0 ? new Memory(memories.get(i-1)) : new Memory();
            checkProcess(queue, i);

            m = execution(queue, m);
            m.joinEmptySlots();

            m = assignBestGap(queue, m);

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
     * For every process in queue that has not been assigned yet, we add them to the memory using bestGap algorithm
     * @param queue of processes
     */
    private Memory assignBestGap(Queue<Process> queue, Memory m) {
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
     */
    private Memory execution(Queue<Process> queue, Memory m) {
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
                m.setNextSlotPos(p.getPosition());
            }
        }
        return m;
    }

    /**
     * For every process in queue that has not been assigned yet, we add them to the memory using nextGap algorithm
     * @param queue of processes
     */
    private Memory assignNextGap(Queue<Process> queue, Memory m) {
        for (Process p : queue){
            if(p.getDuration()!=0 && !m.containsProcess(p)){
                if(m.nextSlot(p)){
                    p.setDuration(-1);
                    m.setNextSlotPos(p.getPosition());
                }
            }
        }
        return m;
    }

    /**
     * This method uses Memory.nextSlot for each memory which will determine the best gap for each process
     * It prints out to the stream the memories created
     */
    public void nextGap(){
        addMemories();
        Queue<Process> queue = new LinkedList<>();
        standardIO2Stream();

        for (int i = 0; i < memories.size(); i++){

            Memory m = i != 0 ? new Memory(memories.get(i-1)) : new Memory();
            checkProcess(queue, i);
            m = execution(queue, m);
            m.joinEmptySlots();

            m = assignNextGap(queue,m);
            m.joinEmptySlots();

            memories.set(i,m);
            System.out.println(i+" "+memories.get(i));
        }

        String sout = Stream2StandardIO();
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

    /**
     * Shows the processes added to the class
     * @return a string with the information of each processes
     */
    public String showProcesses(){
        StringBuilder s = new StringBuilder();
        for (Process p : processes)
            s.append(p).append("\n");
        return s+"";
    }

    /**
     * Shows the memories added to the class
     * @return a string with the information of each memory
     */
    public String showMemories(){
        StringBuilder s = new StringBuilder();
        for (Memory memory : memories) {
            s.append(memory.showMemory()).append("\n");
        }
        return s+"";
    }

    /**
     * This method writes the memories to a file
     * @param toWrite string of memories
     * @param name of the file in which will be saved in
     * @throws IOException if it can't create the file
     */
    private void writeResults2File(String toWrite, String name) throws IOException {

        File myObj = new File("src/Files/"+name+"(result).txt");

        if (myObj.createNewFile())
            System.out.println("File created: " + myObj.getName());

        FileWriter myWriter = new FileWriter("src/Files/"+name+"(result).txt");
        myWriter.write(toWrite);
        myWriter.close();
    }

    /**
     * Changes the printing from standard to stream
     */
    private  void standardIO2Stream(){
        baos = new ByteArrayOutputStream();
        ps = new PrintStream(baos);
        old = System.out;
        System.setOut(ps);
    }

    /**
     * Changes the printing from stream to standard
     */
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
