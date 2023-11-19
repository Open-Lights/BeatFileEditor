package com.github.qpcrummer.gui;

import com.github.qpcrummer.Main;
import com.github.qpcrummer.music.MusicPlayer;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class MainGUI {
    private static String playPause = "Play";
    public static void render() {
        ImGui.begin("Editor", ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoTitleBar);

        ImGui.setWindowSize(ImGui.getIO().getDisplaySizeX(), ImGui.getIO().getDisplaySizeY() - 18);
        ImGui.setWindowPos(0F, 18F);

        // Task bar at the top
        if (ImGui.beginMainMenuBar()) {
            if (ImGui.beginMenu("File")) {
                // File menu options
                if (ImGui.menuItem("Open Beat File")) {
                    FileExplorer.setFileExplorerType(true);
                    Main.enableFileExplorer = true;
                }
                if (ImGui.menuItem("Open WAV File")) {
                    FileExplorer.setFileExplorerType(false);
                    Main.enableFileExplorer = true;
                }
                if (ImGui.menuItem("Save Beat File")) {
                    // TODO Fix this
                }
                if (ImGui.menuItem("New Beat File")) {
                    // TODO Fix this
                }
                ImGui.endMenu();
            }

            if (ImGui.button(playPause)) {
                if (MusicPlayer.playing) {
                    MusicPlayer.pause();
                    playPause = "Play";
                } else {
                    if (MusicPlayer.play()) {
                        playPause = "Pause";
                    }
                }
            }
            if (ImGui.button("Rewind")) {
                if (MusicPlayer.setPosition(0)) {
                    playPause = "Pause";
                }
            }

            ImGui.endMainMenuBar();
        }

        TimeLine.render();

        Toolbox.render();

        ImGui.end();
    }
}
