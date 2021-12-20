package Tests;

import model.Process;
import model.Slot;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class ProcessTest {
    private Process p = new Process("Process 1",0,500,3, Color.blue);
    private final String PROCESS1 = "Process: Process 1 (Arrives at: 0 Requires 500K of memory with a duration of 3)";


    @Test
    public void getName() {
        assertEquals("Process 1",p.getName());
    }
    @Test
    public void getColor() {
        assertEquals(Color.blue,p.getColor());
    }

    @Test
    public void getArrive() {
        assertEquals(0,p.getArrive());
    }

    @Test
    public void getMemory() {
        assertEquals(500,p.getMemory());
    }

    @Test
    public void getSlot(){
        assertEquals(null,p.getSlot());

    }

    @Test
    public void setPosition(){
        p.setSlot(new Slot(500,0));
        assertNotEquals(null,p.getSlot());
    }

    @Test
    public void getDuration() {
        assertEquals(3,p.getDuration());
    }

    @Test
    public void toString1(){
        assertEquals(PROCESS1, p.toString());
    }



}