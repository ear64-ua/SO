package view;

import model.Gestor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ManagerDrawing extends JComponent {

    String filename;
    Gestor gestor;
    ArrayList<Integer> slots = new ArrayList<Integer>();

    public ManagerDrawing(String filename){
        this.filename=filename;

    }

    public void paintComponent(Graphics g){
        if (gestor==null){
            g.setColor(Color.white);
            g.fillRect(20,0,2000,30);

        }

        else {

            String[] mem = gestor.showMemories().split("\n");
            for(int i = 0; i < mem.length; i++){

                //int margin = 20;
                String[] slots = mem[i].split("/");
                System.out.println(i+"----------------------");
                for(int j = 0; j < slots.length; j++){
                    String[] slot = slots[j].split(" ");
                    String[] RBG = slot[0].split(",");
                    g.fillRect(j+(50*j), i*(50+i), Integer.parseInt(slot[1])+(j*3), 30);
                    System.out.println(j+(50*j));
                    //margin += Integer.parseInt(slot[1]);
                    //g.fillRect(margin+40, i*(50+i), 100, 30);
                    g.setColor(new Color( Integer.parseInt(RBG[0]), Integer.parseInt(RBG[1]), Integer.parseInt( RBG[2])));

                    System.out.println( RBG[0]+" "+ RBG[1]+" "+ RBG[2]);
                }
            }
        }
    }

    public void slotsPaint(){

    }

    public void bestGap(){
        Gestor gestor2 = new Gestor(filename);
        gestor2.addProcesses();
        gestor2.bestGap();
        gestor=gestor2;
        repaint();
    }

    public void nextGap(){
        Gestor gestor2 = new Gestor(filename);
        gestor2.addProcesses();
        gestor2.nextGap();
        gestor=gestor2;
        repaint();
    }
}
