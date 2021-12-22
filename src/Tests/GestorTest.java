package Tests;

import model.Gestor;
import org.junit.Test;

import static org.junit.Assert.*;

public class GestorTest {
    private final String PROCESSES = "Process: P1 (Arrives at: 0 Requires 800K of memory with a duration of 5)\n" +
            "Process: P2 (Arrives at: 0 Requires 200K of memory with a duration of 3)\n" +
            "Process: P3 (Arrives at: 0 Requires 200K of memory with a duration of 5)\n" +
            "Process: P4 (Arrives at: 0 Requires 800K of memory with a duration of 3)\n" +
            "Process: P5 (Arrives at: 0 Requires 150K of memory with a duration of 2)\n";

    @Test
    public void testAddProcesses() {
        Gestor g = new Gestor
        ("/Users/me/IdeaProjects/SO/src/Files/processes.txt");
        assertEquals(PROCESSES,g.showProcesses());
    }

    @Test
    public void testBestGap(){
        Gestor g = new Gestor("/Users/me/IdeaProjects/SO/src/Files/processes.txt");
        g.addProcesses();
        g.bestGap();
    }

    @Test
    public void testBestGap2(){
        Gestor g = new Gestor("/Users/me/IdeaProjects/SO/src/Files/processes5.txt");
        Gestor g2 = new Gestor("/Users/me/IdeaProjects/SO/src/Files/processes5.txt");
        g.addProcesses();
        g2.addProcesses();
        //System.out.println(g.getNumMemories());

        g.bestGap();
        System.out.println(g.showMemories());
        //g.resetMemory();

        g2.bestGap();

        //g.bestGap();
        //System.out.println(g.showMemories());

        //System.out.println(g.getNumMemories());
    }

    @Test
    public void testBestGap3(){
        Gestor g = new Gestor("/Users/me/IdeaProjects/SO/src/Files/processes4.txt");
        g.bestGap();
    }

    @Test
    public void numMemories(){
        Gestor g = new Gestor("/Users/me/IdeaProjects/SO/src/Files/processes2.txt");
        assertEquals(9,g.getNumMemories());
    }

    @Test
    public void numMemories2(){
        Gestor g = new Gestor("/Users/me/IdeaProjects/SO/src/Files/processes3.txt");
        assertEquals(15,g.getNumMemories());
    }

}