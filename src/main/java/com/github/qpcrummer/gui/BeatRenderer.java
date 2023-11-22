package com.github.qpcrummer.gui;

import imgui.ImGui;
import imgui.ImVec2;

import java.util.List;

public final class BeatRenderer {
    private static final int INDICATOR_RADIUS = 10;
    private static boolean shouldRender(int tick, Track track) {
        int upperBound;

        switch (MainGUI.zoom) {
            case MainGUI.HUNDRED_MS_VIEW -> {
                upperBound = 100;
                return checkForBeat((long) tick * upperBound, upperBound, track);
            }
            case MainGUI.TEN_MS_VIEW -> {
                upperBound = 10;
                return checkForBeat((long) tick * upperBound, upperBound, track);
            }
            case MainGUI.MS_VIEW -> {
                upperBound = 1;
                return checkForBeat((long) tick * upperBound, upperBound, track);
            }
            default -> {
                upperBound = 1000;
                return checkForBeat((long) tick * upperBound, upperBound, track);
            }
        }
    }

    private static boolean checkForBeat(long beat, int upperBound, Track track) {
        return binarySearchWithLong(track.singleBeats, beat, upperBound) || binarySearchWithLongArray(track.heldBeats, beat, upperBound);
    }

    public static void addSingleBeat(long beat, Track track) {
        if (track.singleBeats.isEmpty()) {
            track.singleBeats.add(beat);
        }

        for (int i = 0; i < track.singleBeats.size(); i++) {
            if (beat > track.singleBeats.get(i)) {
                track.singleBeats.add(i, beat);
            }
        }
    }

    public static void addHeldBeat(long[] beat, Track track) {
        if (track.heldBeats.isEmpty()) {
            track.heldBeats.add(beat);
        }

        long initialBeat = beat[0];

        for (int i = 0; i < track.heldBeats.size(); i++) {
            if (initialBeat > track.heldBeats.get(i)[0]) {
                track.heldBeats.add(i, beat);
            }
        }
    }

    private static boolean binarySearchWithLong(List<Long> list, long target, int range) {
        int left = 0;
        int right = list.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            // Check if the current element is within the specified range
            if (Math.abs(list.get(mid) - target) <= range) {
                return true; // Found a value within the range
            }

            // Adjust the search space based on the comparison
            if (list.get(mid) < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return false; // Value not found within the specified range
    }

    private static boolean binarySearchWithLongArray(List<long[]> rangeList, long target, int range) {
        int left = 0;
        int right = rangeList.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            long[] currentRange = rangeList.get(mid);

            // Check if the target value is within the range
            if (target >= currentRange[0] && target <= currentRange[1]) {
                return true; // Found a value within the range
            }

            // Check if the target value is within "n" above the higher bound
            if (target > currentRange[1] && target - currentRange[1] <= range) {
                return true; // Found a value within the specified range above the higher bound
            }

            // Adjust the search space based on the comparison
            if (target < currentRange[0]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return false; // Value not found within the specified range
    }

    public static void render(int tick, Track track, float x, float y, float spacing) {
        if (shouldRender(tick, track)) {
            float centerX = x + (spacing * 0.5f);
            ImGui.getForegroundDrawList().addRectFilled(centerX + INDICATOR_RADIUS, y + INDICATOR_RADIUS, centerX - INDICATOR_RADIUS, y - INDICATOR_RADIUS, 0xFF0000FF);
        }
    }

    public static void handleLeftClick(Track track) {
        ImVec2 mouse = ImGui.getMousePos();

        double space = Math.floor(mouse.x / TimeLine.SMALL_TICK_SPACING);

        int multiplier;

        switch (MainGUI.zoom) {
            case MainGUI.HUNDRED_MS_VIEW -> {
                multiplier = 100;
            }
            case MainGUI.TEN_MS_VIEW -> {
                multiplier = 10;
            }
            case MainGUI.MS_VIEW -> {
                multiplier = 1;
            }
            default -> {
                multiplier = 1000;
            }
        }

        addSingleBeat((long) (space * multiplier), track);
    }
}
