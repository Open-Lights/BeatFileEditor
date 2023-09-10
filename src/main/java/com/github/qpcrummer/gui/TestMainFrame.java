package com.github.qpcrummer.gui;

import com.github.qpcrummer.gui.panels.BottomTaskBarPanel;
import com.github.qpcrummer.gui.panels.EditorRectanglePanel;
import com.github.qpcrummer.gui.panels.TaskBarPanel;

import javax.swing.*;
import java.awt.*;

public class TestMainFrame extends JFrame {
    private final JPanel editorPanelHolder = new JPanel();
    public TestMainFrame() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        add(new TaskBarPanel(), gbc);

        gbc.gridy = 1;
        add(new JSeparator(), gbc);

        gbc.gridy = 2;
        gbc.weighty = 1.0; // Allow EditorRectanglePanel to fill vertical space
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Make it span all columns
        editorPanelHolder.setLayout(new BoxLayout(editorPanelHolder, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(editorPanelHolder);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        addNewEditor();
        add(scrollPane, gbc);

        gbc.gridy = 3;
        add(new BottomTaskBarPanel(this), gbc);

        setMinimumSize(new Dimension(1200, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
        pack(); // Automatically adjust the frame size based on components
        setVisible(true);
    }

    public void addNewEditor() {
        this.editorPanelHolder.add(new EditorRectanglePanel(1200, this, new JSeparator()));
        this.revalidate();
    }

    public void removeEditor(EditorRectanglePanel panel, JSeparator separator) {
        this.editorPanelHolder.remove(panel);
        this.editorPanelHolder.remove(separator);
        this.revalidate();
    }
}
