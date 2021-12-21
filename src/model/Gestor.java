package model;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Gestor {

    public ArrayList<Process> processes;
    public ArrayList<Memory> memories;
    private Color[] c = {Color.BLUE,Color.RED,Color.MAGENTA,Color.ORANGE,Color.CYAN, Color.GREEN};

    public Gestor(){
        processes = new ArrayList<Process>();
        memories = new ArrayList<Memory>();
    }
/*
    public void bestGap(){
        Queue<Process> queue = new LinkedList<>();
        for (int n = 0; n < getNumMemories(); n++) {

            for (int k = 0; k < processes.size(); k++) {
                if (processes.get(k).getDuration()>n && memories.get(n).isSpace(processes.get(k).getMemory())) {
                    memories.get(n).bestSlot(processes.get(k).getMemory());
                }

                if (!memories.get(n).isSpace(processes.get(k).getMemory())) {
                    queue.add((Process) processes.get(k));
                }
            }

            for (int l = 0; l < queue.size();l++) {
                if (memories.get(n).isSpace(queue.peek().getMemory())) {
                    memories.get(n).bestSlot(queue.peek().getMemory());
                    queue.remove();
                }
            }

            System.out.println(memories.get(n));

        }
    }
*/
    public void addProcesses(String filename) {

        File file = new File(filename);
        try {
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


        for (int i = 0; i < memories.size(); i++){

            // we add to the queue the processes when the time for them to execute comes
            for (Process p : processes){
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
}
