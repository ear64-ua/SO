package view;

import javax.swing.*;
import java.awt.*;

public class ManagerDrawing extends JComponent {
    public void paintComponent(Graphics g){
        g.fillOval(50,0,100,100);
    }
}
