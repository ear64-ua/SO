package Tests;

import model.Memory;
import model.Process;
import model.ProcessNotFound;
import model.Slot;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class MemoryTest {
    private Memory m = new Memory();
    private final String ONESLOT= "[ ( 0: 2000 ) ]";
    private final String TWOSLOTS = "[ ( 0: 300 | Process: p1)  ( 1: 1700 ) ]";
    private final String THREE_PROCESS_ONE_SLOT = "[ ( 0: 500 | Process: p1)  ( 1: 500 | Process: p2)  ( 2: 500 | Process: p3)  ( 3: 500 ) ]";
    private final String REMOVE_ONE_PROCESS = "[ ( 0: 500 | Process: p1)  ( 1: 500 )  ( 2: 500 | Process: p3)  ( 3: 500 ) ]";
    private final String PUSH_AND_ADD = "[ ( 0: 500 | Process: p1)  ( 1: 300 | Process: p4)  ( 2: 200 )  ( 3: 500 | Process: p3)  ( 4: 500 ) ]";
    private final String BESTSLOT1 = "[ ( 0: 300 | Process: p2)  ( 1: 1500 )  ( 2: 200 | Process: p3) ]";
    private final String NEXTSLOT1 = "[ ( 0: 500 | Process: p1)  ( 1: 700 )  ( 2: 200 | Process: p3)  ( 3: 100 | Process: p5)  ( 4: 100 | Process: p4)  ( 5: 400 ) ]";
    private final String NEXTSLOT2 = "[ ( 0: 500 | Process: p1)  ( 1: 700 )  ( 2: 200 | Process: p3)  ( 3: 500 | Process: p5)  ( 4: 100 ) ]";
    private final String SETUPNEXTSLOT = "[ ( 0: 500 | Process: p1)  ( 1: 700 )  ( 2: 200 | Process: p3)  ( 3: 600 ) ]";
    private final String JOINEMPTYSLOTS = "[ ( 0: 500 )  ( 1: 1000 | Process: p3)  ( 2: 500 ) ]";

    @Test
    public void testSlotIsEmpty(){
        assertTrue(m.slotIsEmpty(new Slot(2000,0)));
        m.bestSlot(new Process("p1",0,2000,3, Color.BLUE));
       assertFalse(m.slotIsEmpty(new Slot(2000,0)));
    }

    @Test
    public void testIsSpace() {
        assertTrue(m.isSpace(200));
        assertTrue(m.isSpace(1800));
        assertFalse(m.isSpace(2001));
        assertTrue(m.isSpace(2000));
        assertFalse(m.isSpace(5000));
        assertTrue(m.isSpace(100));
    }

    @Test
    public void testShowName(){
        assertEquals(ONESLOT,m.toString());
        m.bestSlot(new Process("p1",0,300,3, Color.BLUE));
        assertEquals(TWOSLOTS,m.toString());
        m.bestSlot(new Process("p2",0,700,3, Color.RED));

    }

    @Test
    public void testBestSlot1() {
        assertTrue(m.bestSlot(new Process("p1",0,1800,3, Color.BLUE)));
        assertFalse(m.bestSlot(new Process("p2",0,300,3, Color.RED)));
        assertTrue(m.bestSlot(new Process("p3",0,200,3, Color.MAGENTA)));

    }

    @Test
    public void testBestSlot2() {
        Process p1 = new Process("p1",0,1800,3, Color.BLUE);
        Process p2 = new Process("p2",0,300,3, Color.RED);
        Process p3 = new Process("p3",0,200,3, Color.BLACK);
        assertTrue(m.bestSlot(p1));
        assertTrue(m.bestSlot(p3));

        try {
            m.removeProcess(p1);
            assertTrue(m.bestSlot(p2));
            assertEquals(BESTSLOT1,m.toString());

        } catch (ProcessNotFound e) {
            fail("The exception shouldn't have been thrown");
        }

    }

    @Test
    public void testRemoveProcess1(){
        Process p1 = new Process("p1",0,1800,3, Color.BLUE);
        m.bestSlot(p1);
        assertTrue(m.containsProcess(p1));
        try {
            m.removeProcess(p1);
            assertFalse(m.containsProcess(p1));
        } catch (ProcessNotFound e) {
            fail("The exception shouldn't have been thrown");
        }
    }

    @Test
    public void testRemoveProcess2(){
        Process p1 = new Process("p1",0,1800,3, Color.BLUE);
        Process p2 = new Process("p2",0,200,3, Color.RED);
        m.bestSlot(p1);
        assertFalse(m.containsProcess(p2));
        try {
            m.removeProcess(p2);
            fail("The exception should have been thrown");
        } catch (Exception ignored) { }
    }

    @Test
    public void testPushSlots(){
        Process p1 = new Process("p1",0,500,3, Color.BLUE);
        Process p2 = new Process("p2",0,500,3, Color.RED);
        Process p3 = new Process("p3",0,500,3, Color.BLACK);
        Process p4 = new Process("p4",0,300,3, Color.BLACK);
        Process p5 = new Process("p5",0,300,3, Color.RED);

        m.bestSlot(p1);
        m.bestSlot(p2);
        m.bestSlot(p3);
        assertEquals(THREE_PROCESS_ONE_SLOT,m.toString());
        try {
            m.removeProcess(p2);
            assertEquals(REMOVE_ONE_PROCESS,m.toString());
            m.bestSlot(p4);
        } catch (ProcessNotFound e) {
            e.getMessage();
        }
        assertEquals(PUSH_AND_ADD,m.toString());

    }

    @Test
    public void testNextSlot1() {
        setUpNextSlot();
        m.nextSlot(new Process("p5",0,100,3, Color.MAGENTA));
        m.nextSlot(new Process("p4",0,100,3, Color.MAGENTA));
        assertEquals(NEXTSLOT1,m.toString());
    }

    private void setUpNextSlot() {
        Process p1 = new Process("p1",0,500,3, Color.BLUE);
        Process p2 = new Process("p2",0,700,3, Color.RED);
        m.nextSlot(p1);
        m.nextSlot(p2);
        assertTrue(m.nextSlot(new Process("p3",0,200,3, Color.MAGENTA)));

        try {
            m.removeProcess(p2);
            assertEquals(SETUPNEXTSLOT,m.toString());

        } catch (ProcessNotFound e) {
            e.getMessage();
        }
        System.out.println(m);
    }

    @Test
    public void testNextSlot2() {
        setUpNextSlot();
        m.nextSlot(new Process("p5",0,500,3, Color.MAGENTA));
        assertEquals(NEXTSLOT2,m.toString());
        System.out.println(m);
    }

    @Test
    public void testJoinEmptySlots(){
        Process p1 = new Process("p1",0,200,3, Color.BLUE);
        Process p2 = new Process("p2",0,300,3, Color.RED);
        Process p3 = new Process("p3",0,1000,3, Color.MAGENTA);
        Process p4 = new Process("p4",0,200,3, Color.RED);
        Process p5 = new Process("p5",0,300,3, Color.MAGENTA);
        m.bestSlot(p1);
        m.bestSlot(p2);
        m.bestSlot(p3);
        m.bestSlot(p4);
        m.bestSlot(p5);

        try {
            m.removeProcess(p1);
            m.removeProcess(p2);
            m.removeProcess(p4);
            m.removeProcess(p5);
        } catch (ProcessNotFound e) {
            e.getMessage();
        }
        m.joinEmptySlots();
        assertEquals(JOINEMPTYSLOTS,m.toString());

    }

    @Test
    public void testPutInSlot(){
        Process p1 = new Process("p1",0,200,3, Color.BLUE);
        Process p3 = new Process("p3",0,500,3, Color.BLUE);

        Process p2 = new Process("p2",0,300,3, Color.RED);

        m.bestSlot(p2);
        m.bestSlot(p3);
        try {
            m.removeProcess(p2);
            m.removeProcess(p3);
        } catch (ProcessNotFound e) {
            e.printStackTrace();
        }
        System.out.println(m);

        m.setInSlot(1,p1);
        System.out.println(m);
    }

}