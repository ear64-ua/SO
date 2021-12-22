package view;

import model.Gestor;

import javax.swing.*;
import java.awt.*;

public class ManagerPanel extends JPanel {
    private Gestor g;
    final static int left_margin = 20;
    private int marginMemories = 0;
    private JButton nextGapButton= new JButton("Next Gap Algorithm");
    private JButton bestGapButton = new JButton("Best Gap Algorithm");
    ManagerDrawing managerDrawing = new ManagerDrawing();

    private Color[] c = {Color.BLUE,Color.RED,Color.MAGENTA,Color.ORANGE,Color.CYAN, Color.GREEN};

    public ManagerPanel(){
        addNextGapButton();
        addBestGapButton();

        //g = new Gestor("/Users/me/IdeaProjects/SO/src/Files/processes5.txt");
        //g.addProcesses();
        //addProcessLabel();

        //managerDrawing.setPreferredSize(new Dimension(300,300));
        //add(managerDrawing);

        NextGapListener nextGapListener= new NextGapListener();
        BestGapListener bestGapListener= new BestGapListener();

        nextGapButton.addActionListener(nextGapListener);
        bestGapButton.addActionListener(bestGapListener);

        add(nextGapButton);
        add(bestGapButton);


    }



    /*
    public void actionPerformed(ActionEvent e) {

        if (e.getSource()==mejorHueco_button) {
            System.out.println("button 1");
            //g.bestGap();
            System.out.println("");
            mejorHueco_button.setEnabled(false);
            siguienteHueco_button.setEnabled(true);

        }

        else {
            System.out.println("button 2");

            //nextGap();
            mejorHueco_button.setEnabled(true);
            siguienteHueco_button.setEnabled(false);

        }

    }*/

    public void addProcessLabel() {
        String[] p = g.showProcesses().split("\n");

        for (int i = 0; i < p.length; i++) {
            JLabel label = new JLabel();
            label.setText(p[i]);
            label.setBounds(left_margin, -160 + 15*i, 500, 500);
            this.add(label);
        }

    }

    public void addBestGapButton() {
        //bestGapButton = new JButton();
        bestGapButton.setBounds(left_margin, 20, 100, 50);
        /*bestGapButton.setFocusable(false);
        bestGapButton.setFont(new Font("Comic Sans",Font.BOLD,10));
        bestGapButton.setForeground(Color.DARK_GRAY);
        bestGapButton.setBorder(BorderFactory.createBevelBorder(0, getForeground(), getForeground(), getBackground(), getForeground()));;
        bestGapButton.setBackground(Color.lightGray);*/
    }

    public void addNextGapButton() {
        //nextGapButton = new JButton();
        nextGapButton.setBounds(left_margin+100, 20, 100, 50);
        /*nextGapButton.setFocusable(false);
        nextGapButton.setFont(new Font("Comic Sans",Font.BOLD,10));
        nextGapButton.setForeground(Color.DARK_GRAY);
        nextGapButton.setBorder(BorderFactory.createBevelBorder(0, getForeground(), getForeground(), getBackground(), getForeground()));;
        nextGapButton.setBackground(Color.lightGray);*/
    }
}
