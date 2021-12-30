package model;

import java.awt.*;

/**
 * This class represents a process and the slot it has assigned
 * @author Enrique Abma Romero
 */
public class Process{

    private String name;
    private int arrive;
    private int memory;
    private int duration;
    private Color color;
    private Slot slot;
    private int x;

    /**
     * Constructor of the class
     * @param name identifier of the process
     * @param arrive arriving time
     * @param memory space it will occupy
     * @param duration or instances it will last
     * @param color which will be represented in the graphics
     */
    public Process(String name,int arrive,int memory,int duration, Color color) {
        this.name=name;
        this.arrive=arrive;
        this.memory=memory;
        this.duration=duration;
        this.color=color;
        this.slot = null;
        x = -1;
    }

    /**
     * Copy constructor of the class
     * @param p process that will be copied
     */
    public Process(Process p){
        this.name=p.name;
        this.arrive=p.arrive;
        this.memory=p.memory;
        this.duration=p.duration;
        this.color=p.color;
        this.slot = p.slot;
        this.x = p.x;
    }

    /**
     * Getter of name
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter of colour
     * @return the value of color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Getter of arrive
     * @return the value of arrive
     */
    public int getArrive() {
        return arrive;
    }

    /**
     * Getter of memory
     * @return the value of memory space
     */
    public int getMemory() {
        return memory;
    }

    /**
     * Getter of the position
     * @return the value of its position
     */
    public int getPosition(){
        if (slot!=null)
            return slot.getX();
        return -1;
    }

    /**
     * Setter of slot
     * @param slot which will be assigned
     */
    public void setSlot(Slot slot){
        this.slot = slot;
    }

    /**
     * Normally used for subtract one unit to its duration
     * @param qty that will increase of decrease it value
     */
    public void setDuration(int qty){
        duration+=qty;
    }

    /**
     * Setter of position
     * @param x used to update the value of its position
     */
    public void setPosition(int x){
        this.x=x;
    }

    /**
     * Getter of the slot it has assigned
     * @return the value of slot
     */
    public Slot getSlot(){
        return slot;
    }

    /**
     * Getter of duration
     * @return the value of its duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Used to show the details of the object
     * @return a string with the details
     */
    @Override
    public String toString() {
        return "Process: " + name + " (Arrives at: " + arrive + " Requires " + memory + "K of memory " +  "with a duration of " + duration + ")";
    }

    /**
     * Used for just knowing the name of the process
     * @return a string that concat the name of the process
     */
    public String showName(){
        return " Process: " + name;
    }

}
