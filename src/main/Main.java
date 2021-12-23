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
    }
}
