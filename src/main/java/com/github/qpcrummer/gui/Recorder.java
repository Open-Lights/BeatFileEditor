package com.github.qpcrummer.gui;

import com.github.qpcrummer.Main;
import com.github.qpcrummer.music.MusicPlayer;
import com.github.qpcrummer.processing.Timer;
import imgui.ImGui;
import imgui.type.ImString;

public class Recorder {
    private static final ImString countdown = new ImString();
    private static short countdownTimer = -1;
    private static boolean decrement;
    private static boolean recording;
    private static long heldTime = -1;
    private static Track recordedTrack;
    public static void render() {
        ImGui.begin("Beat Recorder");

        if (ImGui.button("Close")) {
            if (MusicPlayer.playing) {
                MusicPlayer.pause();
            }

            Main.enableRecorder = false;
        }

        if (MusicPlayer.currentSong == null) {
            ImGui.text("Please select a song using File -> Open WAV");
            ImGui.end();
            return;
        }

        ImGui.text(countdown.get());

        if (ImGui.button("Beat", 200, 200) || decrement) {
           if (!recording) {
               recordedTrack = new Track();
               recordedTrack.setChannelText(String.valueOf(TimeLine.tracks.size()));

                switch (countdownTimer) {
                    case -1 -> {
                        countdownTimer = 5;
                        Timer.wait(1000, () -> decrement = true);
                    }
                    case 0 -> {
                        countdownTimer--;
                        recording = true;
                        MusicPlayer.play();
                    }
                    default -> {
                        countdownTimer--;
                        Timer.wait(1000, () -> decrement = true);
                    }
                }

                decrement = false;
                countdown.set(countdownTimer);
            }
        }

        // Recording
        if (recording) {
            if (ImGui.isItemActivated()) {
                heldTime = MusicPlayer.getPositionMilliseconds();
            } else if (ImGui.isItemDeactivated()) {
                long reference = MusicPlayer.getPositionMilliseconds();
                long time = reference - heldTime;

                if (time < 100) {
                    BeatRenderer.addNonSorted(recordedTrack, heldTime);
                } else {
                    BeatRenderer.addNonSorted(recordedTrack, new long[]{heldTime, reference});
                }

                heldTime = -1;
            }

            // Completed
            if (MusicPlayer.getPositionMilliseconds() >= MusicPlayer.getSongLengthMilliseconds()) {
                heldTime = -1;
                recording = false;
                TimeLine.tracks.add(recordedTrack);
                recordedTrack = null;
                countdownTimer = -1;
                countdown.set("Recording Complete");
                decrement = false;
            }
        }

        ImGui.end();
    }
}
