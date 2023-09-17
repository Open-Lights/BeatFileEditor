package com.github.qpcrummer.gui.layout_manager;

import javax.swing.*;

public class ScalablePanel extends JPanel {
    private boolean xScalable = true;
    private boolean yScalable = true;

    public void setxScalable(boolean bool) {
        this.xScalable = bool;
    }

    public void setyScalable(boolean bool) {
        this.yScalable = bool;
    }

    public boolean getxScalable() {
        return this.xScalable;
    }

    public boolean getyScalable() {
        return this.yScalable;
    }
}
