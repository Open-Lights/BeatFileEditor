package com.github.qpcrummer.gui;

import com.github.qpcrummer.Main;
import com.github.qpcrummer.music.MusicPlayer;
import com.github.qpcrummer.processing.BeatFile;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MainGUI {
    private static String playPause = "Play";
    private static final List<Container> containers = new ArrayList<>();
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
                    for (Container container : containers) {
                        BeatFile.save(container.getCorrectedList(), container.getChannels());
                    }
                }
                if (ImGui.menuItem("New Beat File")) {
                    containers.clear();
                    Container container = new Container();
                    container.modifyContainerName("0");
                    containers.add(container);
                }
                ImGui.endMenu();
            }

            if (ImGui.beginMenu("Edit")) {
                if (ImGui.menuItem("Add Channel")) {
                    containers.add(new Container());
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

        ImGui.end();
    }
}
