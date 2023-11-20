package com.github.qpcrummer.gui;

import com.github.qpcrummer.Main;
import com.github.qpcrummer.music.MusicPlayer;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class MainGUI {
    private static String playPause = "Play";
    private static final short STANDARD_VIEW = 1;
    public static final short HUNDRED_MS_VIEW = 3;
    public static final short TEN_MS_VIEW = 4;
    public static final short MS_VIEW = 5;
    public static int zoom = 1;
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

            if (ImGui.beginMenu("View")) {
                if (ImGui.menuItem("Zoom Regular")) {
                    TimeLine.TOTAL_TIME_MODIFIER = 1;
                    zoom(STANDARD_VIEW);
                }

                if (ImGui.menuItem("Zoom 100ms")) {
                    TimeLine.TOTAL_TIME_MODIFIER = 1;
                    zoom(HUNDRED_MS_VIEW);
                }

                if (ImGui.menuItem("Zoom 10ms")) {
                    TimeLine.TOTAL_TIME_MODIFIER = 10;
                    zoom(TEN_MS_VIEW);
                }

                if (ImGui.menuItem("Zoom 1ms")) {
                    TimeLine.TOTAL_TIME_MODIFIER = 100;
                    zoom(MS_VIEW);
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

    private static void zoom(final int zoomFactor) {
        TimeLine.LARGE_TICK_SPACING = (short) (100 * zoomFactor);
        TimeLine.SMALL_TICK_SPACING = TimeLine.LARGE_TICK_SPACING * 0.1f;
        TimeLine.CULLING_PADDING = (14f / TimeLine.LARGE_TICK_SPACING) * 100;
        // TODO Make this not constant 600
        TimeLine.TOTAL_TIME = 600 * TimeLine.TOTAL_TIME_MODIFIER;
        zoom = zoomFactor;
    }
}
