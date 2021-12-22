package main;
import view.ManagerPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel panel = new ManagerPanel();

        panel.setBackground(Color.lightGray);
        frame.add(panel);
        frame.setTitle("Memory manager simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setLayout(null);
        frame.setSize(500,500);
        frame.setVisible(true);
        //new MyAPP();
        /*
        Memory m = new Memory();

        System.out.println(m);

        m.bestSlot(800);

        System.out.println(m);

        m.bestSlot(500);

        System.out.println(m);

        m.bestSlot(200);
        System.out.println(m)



        Map<Slot, Process> memory = new TreeMap<>();
        Slot s1 = new Slot(100,0);
        Slot s2 = new Slot(200,1);
        Slot s3 = new Slot(300,2);
        Slot s4 = new Slot(400,1);

        Process p1 = new Process("p1",0,100,3, Color.BLUE);
        Process p2 = new Process("p2",0,200,3,Color.BLUE);
        Process p3 = new Process("p3",0,300,3,Color.BLUE);
        Process p4 = new Process("p4",0,400,3,Color.BLUE);

        memory.put(s1,p1);
        memory.put(s2,p2);
        memory.put(s3,p3);

        memory.put(s2,null);

        for (Map.Entry pairEntry: memory.entrySet() ) {
            System.out.println(pairEntry.getKey()+" "+pairEntry.getValue());
        }
*/


    }
}
