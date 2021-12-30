package view;

import model.Gestor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.SwingConstants.TOP;

public class ManagerPanel extends JPanel {
    private Gestor g;
    public static final int left_margin = 20;
    public final String filename = "src/Files/processes.txt";
    private int marginMemories = 0;
    private JButton nextGapButton= new JButton("Next Gap Algorithm");
    private JButton bestGapButton = new JButton("Best Gap Algorithm");
    ManagerDrawing managerDrawing = new ManagerDrawing(filename);

    private Color[] c = {Color.BLUE,Color.RED,Color.MAGENTA,Color.ORANGE,Color.CYAN, Color.GREEN};

    public ManagerPanel(){
        addNextGapButton();
        addBestGapButton();

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

        //addProcessLabel();
    }

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
        Gestor g = new Gestor(filename);
        String[] p = g.showProcesses().split("\n");

        for (int i = 0; i < p.length; i++) {
            JLabel label = new JLabel();
            label.setText(p[i]);
            label.setBounds(left_margin, -160 + 15*i, 500, 500);
            label.setHorizontalAlignment(0); // set the horizontal alignement on the x axis !
            label.setVerticalAlignment(SwingConstants.BOTTOM);
            this.add(label);
            setVisible(true);
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
