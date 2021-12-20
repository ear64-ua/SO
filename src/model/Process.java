package model;
import java.awt.Color;
import java.awt.Graphics;

public class Process{

    private String name;
    private int arrive;
    private int memory;
    private int duration;
    private Color color;
    private Slot slot;

    public Process(String name,int arrive,int memory,int duration, Color color) {
        this.name=name;
        this.arrive=arrive;
        this.memory=memory;
        this.duration=duration;
        this.color=color;
        this.slot = null;
    }

    public void paintProcess(Graphics g, int t, int y)
    {

    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getArrive() {
        return arrive;
    }

    public int getMemory() {
        //return memory/5;
        return memory;
    }

    public int getPosition(){
        return slot.getX();
    }

    public void setSlot(Slot slot){
        this.slot = slot;
    }

    public void setDuration(int qty){
        duration+=qty;
    }

    public Slot getSlot(){
        return slot;
    }

    public int getDuration() {
        return duration;
    }

    public String toString() {
        return "Process: " + name + " (Arrives at: " + arrive + " Requires " + memory + "K of memory " +  "with a duration of " + duration + ")";
    }

    public String showName(){
        return " Process: " + name;
    }

}
