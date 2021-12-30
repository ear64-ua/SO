package view;

import model.Gestor;

import javax.swing.JFrame;
import java.util.Queue;
import java.util.LinkedList;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class MyAPP extends JFrame implements ActionListener {

    private Gestor g;
    final static int left_margin = 20;
    private int marginMemories = 0;
    private JButton siguienteHueco_button;
    private JButton mejorHueco_button;

    private Color[] c = {Color.BLUE,Color.RED,Color.MAGENTA,Color.ORANGE,Color.CYAN, Color.GREEN};

    public MyAPP(){
        addBestGapButton();
        addNextGapButton();
        g = new Gestor("src/Files/processes.txt");
        g.addProcesses();
        addProcessLabel();

        this.setTitle("Memory manager simulator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(500,500);
        this.setVisible(true);
        this.add(siguienteHueco_button);
        this.add(mejorHueco_button);

    }


    @Override
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

    }

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
        mejorHueco_button = new JButton();
        mejorHueco_button.setBounds(left_margin, 20, 100, 50);
        mejorHueco_button.setText("Best Gap");
        mejorHueco_button.setFocusable(false);
        mejorHueco_button.setFont(new Font("Comic Sans",Font.BOLD,10));
        mejorHueco_button.addActionListener(this);
        mejorHueco_button.setForeground(Color.DARK_GRAY);
        mejorHueco_button.setBorder(BorderFactory.createBevelBorder(0, getForeground(), getForeground(), getBackground(), getForeground()));;
        mejorHueco_button.setBackground(Color.lightGray);
    }

    public void addNextGapButton() {
        siguienteHueco_button = new JButton();
        siguienteHueco_button.setBounds(left_margin+100, 20, 100, 50);
        siguienteHueco_button.setText("Next gap");
        siguienteHueco_button.setFocusable(false);
        siguienteHueco_button.setFont(new Font("Comic Sans",Font.BOLD,10));
        siguienteHueco_button.addActionListener(this);
        siguienteHueco_button.setForeground(Color.DARK_GRAY);
        siguienteHueco_button.setBorder(BorderFactory.createBevelBorder(0, getForeground(), getForeground(), getBackground(), getForeground()));;
        siguienteHueco_button.setBackground(Color.lightGray);
    }

}

