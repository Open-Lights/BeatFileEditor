package com.github.qpcrummer.gui.panels;

import javax.swing.*;
import java.awt.*;

public class TaskBarPanel extends JPanel {
    private final JButton exportToFileButton = new JButton("Export");
    private final JButton importFromFileButton = new JButton("Import");
    private final JButton playButton = new JButton("Play");
    private final JButton rewindButton = new JButton("Rewind");

    public TaskBarPanel() {
        setup();
    }

    private void setup() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        importFromFileButton.setPreferredSize(new Dimension(100, 50));
        add(importFromFileButton, gbc);

        gbc.gridx = 1;
        exportToFileButton.setPreferredSize(new Dimension(100, 50));
        add(exportToFileButton);

        gbc.gridx = 2;
        JPanel musicPanel = new JPanel();
        musicPanel.setPreferredSize(new Dimension(200, 50));
        musicPanel.add(playButton);
        musicPanel.add(rewindButton);
        add(musicPanel);
    }
}
