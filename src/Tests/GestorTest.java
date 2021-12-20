package Tests;

import model.Gestor;
import org.junit.Test;

import static org.junit.Assert.*;

public class GestorTest {
    Gestor g = new Gestor();
    private final String PROCESSES = "Process: P1 (Arrives at: 0 Requires 800K of memory with a duration of 5)\n" +
            "Process: P2 (Arrives at: 0 Requires 200K of memory with a duration of 3)\n" +
            "Process: P3 (Arrives at: 0 Requires 200K of memory with a duration of 5)\n" +
            "Process: P4 (Arrives at: 0 Requires 800K of memory with a duration of 3)\n" +
            "Process: P5 (Arrives at: 0 Requires 150K of memory with a duration of 2)\n";

    @Test
    public void testAddProcesses() {
        g.addProcesses("/Users/me/IdeaProjects/SO/src/Files/processes.txt");
        assertEquals(PROCESSES,g.showProcesses());
    }

    @Test
    public void testBestGap(){
        g.addProcesses("/Users/me/IdeaProjects/SO/src/Files/processes.txt");
        g.bestGap();
    }

    @Test
    public void testBestGap2(){
        g.addProcesses("/Users/me/IdeaProjects/SO/src/Files/processes5.txt");
        g.bestGap();
    }

    @Test
    public void numMemories(){
        g.addProcesses("/Users/me/IdeaProjects/SO/src/Files/processes2.txt");
        assertEquals(9,g.getNumMemories());
    }

    @Test
    public void numMemories2(){
        g.addProcesses("/Users/me/IdeaProjects/SO/src/Files/processes3.txt");
        assertEquals(15,g.getNumMemories());
    }

}