package com.github.qpcrummer.gui.panels;

import com.github.qpcrummer.gui.TestMainFrame;

import javax.swing.*;
import java.awt.*;

public class EditorRectanglePanel extends JPanel {
    private int[] channels = {0};
    private String name = "Channel Name";
    private final int width;
    private final TestMainFrame parent;
    private final JSeparator separator;

    // UI parts
    private final JTextField nameTextField = new JFormattedTextField(name);
    private final JTextField channelsTextField = new JFormattedTextField(channels);
    private final JButton removeButton = new JButton("Remove");
    public EditorRectanglePanel(int songLengthMs, TestMainFrame parent, JSeparator separator) {
        this.width = songLengthMs;
        this.parent = parent;
        this.separator = separator;
        setup();
    }

    private void setup() {
        setLayout(new GridBagLayout());

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(100, 100)); // Set to your desired size
        infoPanel.setMaximumSize(new Dimension(100, 100));
        infoPanel.setMinimumSize(new Dimension(100, 100));
        infoPanel.setBackground(Color.GRAY);
        infoPanel.add(nameTextField);
        infoPanel.add(channelsTextField);
        infoPanel.add(removeButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Place info panel in the 1st column
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        add(infoPanel, gbc);

        // Interaction Panel
        JPanel interactionPanel = new JPanel();
        interactionPanel.setMaximumSize(new Dimension(this.width - 100, 100));
        interactionPanel.setMinimumSize(new Dimension(this.width - 100, 100));
        interactionPanel.setBackground(Color.DARK_GRAY);

        gbc = new GridBagConstraints();
        gbc.gridx = 1; // Place interaction panel in the 2nd column
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Allow interaction panel to expand horizontally
        gbc.fill = GridBagConstraints.BOTH;
        add(interactionPanel, gbc);


        // Action Listeners
        removeButton.addActionListener(e -> parent.removeEditor(this, separator));
    }
}
