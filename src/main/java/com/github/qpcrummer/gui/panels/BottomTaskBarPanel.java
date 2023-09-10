package com.github.qpcrummer.gui.panels;

import com.github.qpcrummer.gui.TestMainFrame;

import javax.swing.*;
import java.awt.*;

public class BottomTaskBarPanel extends JPanel {
    private final JButton addNewEditorButton = new JButton("Add Channel Editor");
    private final TestMainFrame parent;
    public BottomTaskBarPanel(TestMainFrame parent) {
        this.parent = parent;
        setup();
    }

    public void setup() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        addNewEditorButton.setPreferredSize(new Dimension(100, 50));
        add(addNewEditorButton, gbc);

        // Action Listeners
        addNewEditorButton.addActionListener(e -> parent.addNewEditor());
    }
}
