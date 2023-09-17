package com.github.qpcrummer.gui.panels;

import com.github.qpcrummer.gui.MovingLine;

import javax.swing.*;

public class TimeLinePanel extends JPanel {

    private final MovingLine line;

    public TimeLinePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.line = new MovingLine(this);
        add(this.line);
    }

    public void test() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            this.line.incrementPosition();
            Thread.sleep(100);
        }
    }
}
