package view;

import model.Gestor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerPanel extends JPanel {
    private Gestor g;
    public static final int left_margin = 20;
    private int marginMemories = 0;
    private JButton nextGapButton= new JButton("Next Gap Algorithm");
    private JButton bestGapButton = new JButton("Best Gap Algorithm");
    ManagerDrawing managerDrawing = new ManagerDrawing("/Users/me/IdeaProjects/SO/src/Files/processes5.txt");

    private Color[] c = {Color.BLUE,Color.RED,Color.MAGENTA,Color.ORANGE,Color.CYAN, Color.GREEN};

    public ManagerPanel(){
        addNextGapButton();
        addBestGapButton();

        //g.addProcesses();
        //addProcessLabel();
        managerDrawing.setForeground(Color.white);
        //managerDrawing.setOpaque(true);
        managerDrawing.setLocation(200,50);

        managerDrawing.setPreferredSize(new Dimension(400,400));
        add(managerDrawing,BorderLayout.NORTH);

        NextGapListener nextGapListener= new NextGapListener();
        BestGapListener bestGapListener= new BestGapListener();

        nextGapButton.addActionListener(nextGapListener);
        bestGapButton.addActionListener(bestGapListener);

        add(nextGapButton,BorderLayout.SOUTH);
        add(bestGapButton,BorderLayout.SOUTH);


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

    public class NextGapListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            managerDrawing.nextGap();
        }
    }

    public class BestGapListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            managerDrawing.bestGap();
        }
    }

    public void addProcessLabel() {
        Gestor g = new Gestor("/Users/me/IdeaProjects/SO/src/Files/processes5.txt");
        String[] p = g.showProcesses().split("\n");

        for (int i = 0; i < p.length; i++) {
            JLabel label = new JLabel();
            label.setText(p[i]);
            label.setBounds(left_margin, -160 + 15*i, 500, 500);
            this.add(label);
        }

    }

    public void addBestGapButton() {
        bestGapButton.setBounds(left_margin, -200, 100, 50);
        bestGapButton.setForeground(Color.DARK_GRAY);
        bestGapButton.setFocusable(false);
        bestGapButton.setFont(new Font("Comic Sans",Font.BOLD,10));
    }

    public void addNextGapButton() {
        nextGapButton.setBounds(left_margin+100, 0, 100, 50);
        nextGapButton.setLocation(20+100,0);
        nextGapButton.setFocusable(false);
        nextGapButton.setFont(new Font("Comic Sans",Font.BOLD,10));
        bestGapButton.setForeground(Color.DARK_GRAY);
    }
}
