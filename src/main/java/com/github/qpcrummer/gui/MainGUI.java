package com.github.qpcrummer.gui;

import com.github.qpcrummer.Main;
import com.github.qpcrummer.music.MusicPlayer;
import com.github.qpcrummer.processing.BeatFile;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class MainGUI {
    private static String playPause = "Play";
    public static final short STANDARD_VIEW = 1;
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
                    BeatFile.saveAll();
                }
                if (ImGui.menuItem("New Beat File")) {
                    if (!TimeLine.tracks.isEmpty()) {
                        Main.enableWarning = true;
                    } else {
                        newBeatFile();
                    }
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

            if (ImGui.beginMenu("Record")) {
                Main.enableRecorder = true;
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
        TimeLine.TOTAL_TIME = MusicPlayer.getSongLengthSec() * TimeLine.TOTAL_TIME_MODIFIER;
        zoom = zoomFactor;
    }

    public static void newBeatFile() {
        Track track = new Track();
        track.setChannelText(String.valueOf(0));
        TimeLine.tracks.add(track);
    }
}
