package com.github.qpcrummer.gui;

import com.github.qpcrummer.Main;
import imgui.ImGui;
import imgui.ImVec2;

import java.util.Collections;
import java.util.Comparator;

public final class BeatRenderer {
    private static final int INDICATOR_RADIUS = 10;
    private static final Comparator<Object> customComparator = (o1, o2) -> {
        long val1 = (o1 instanceof long[]) ? ((long[]) o1)[0] : (long) o1;
        long val2 = (o2 instanceof long[]) ? ((long[]) o2)[0] : (long) o2;
        return Long.compare(val1, val2);
    };
    private static boolean shouldRender(int tick, Track track) {
        if (MainGUI.zoom != MainGUI.MS_VIEW) {
            return false;
        }

        return checkForBeat(tick, track);
    }

    private static boolean checkForBeat(long beat, Track track) {
        return Collections.binarySearch(track.beats, beat, customComparator) > 0;
    }

    public static void addSorted(Track track, Object element) {
        int index = Collections.binarySearch(track.beats, element, customComparator);

        if (index < 0) {
            // Element not found, calculate the insertion point
            int insertionPoint = -index - 1;
            track.beats.add(insertionPoint, element);
        } else {
            // Element already exists at the found index
            // track.beats.add(index, element);
            Main.logger.warning("Duplicate beat found");
        }
    }

    /**
     * WARNING: Only use this if the elements are being added in numerical value!
     * @param track Track that is being added to
     * @param element Beat (Either long or long[])
     */
    public static void addNonSorted(Track track, Object element) {
        track.beats.add(element);
    }

    public static void render(int tick, Track track, float x, float y, float spacing) {
        if (shouldRender(tick, track)) {
            float centerX = x + (spacing * 0.5f);
            ImGui.getForegroundDrawList().addRectFilled(centerX + INDICATOR_RADIUS, y + INDICATOR_RADIUS, centerX - INDICATOR_RADIUS, y - INDICATOR_RADIUS, 0xFF0000FF);
        }
    }

    public static void handleLeftClick(Track track) {
        if (MainGUI.zoom != MainGUI.MS_VIEW) {
            return;
        }

        ImVec2 mouse = ImGui.getMousePos();

        double space = Math.floor(mouse.x / TimeLine.SMALL_TICK_SPACING);

        addSorted(track, (long) space);
    }
}
