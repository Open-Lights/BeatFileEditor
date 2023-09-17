package com.github.qpcrummer.gui;

import javax.swing.*;
import java.awt.*;

public class MovingLine extends JComponent {

    private int xCoordinate = 0;
    private int yCoordinate = 0;

    public MovingLine(JPanel panel) {
        setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight()));
        setyCoordinate(panel.getHeight());
    }

    public void incrementPosition() {
        xCoordinate++;
        repaint();
    }

    public void setxCoordinate(int coordinate) {
        this.xCoordinate = coordinate;
    }

    public void setyCoordinate(int coordinate) {
        this.yCoordinate = coordinate;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.drawLine(xCoordinate, 0, xCoordinate, yCoordinate);
    }
}
