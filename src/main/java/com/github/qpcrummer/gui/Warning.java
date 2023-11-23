package com.github.qpcrummer.gui;

import com.github.qpcrummer.Main;
import com.github.qpcrummer.processing.BeatFile;
import imgui.ImGui;

public final class Warning {
    public static void render() {
        ImGui.begin("Warning");

        ImGui.text("You have unsaved work!");

        if (ImGui.button("Continue")) {
            TimeLine.tracks.clear();
            MainGUI.newBeatFile();
            Main.enableWarning = false;
        }

        ImGui.sameLine();

        if (ImGui.button("Save and Continue")) {
            BeatFile.saveAll();
            TimeLine.tracks.clear();
            MainGUI.newBeatFile();
            Main.enableWarning = false;
        }
        ImGui.end();
    }
}
