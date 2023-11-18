package com.github.qpcrummer.gui;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiMouseButton;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class TimeLine {
    private static final float TIMELINE_START = 0.0f;
    private static final float TIMELINE_END = 10.0f;
    private static final float INTERVAL = 1.0f;
    public static final List<Track> tracks = new ArrayList<>();

    public static void render() {
        // Set the width and height of the timeline
        ImVec2 timelineSize = new ImVec2(ImGui.getIO().getDisplaySizeX(), 30);

        // Draw the timeline background
        ImGui.dummy(timelineSize.x, timelineSize.y);

        // Draw tick marks at each second interval with labels
        for (float time = TIMELINE_START; time <= TIMELINE_END; time += INTERVAL) {
            float tickX = ImGui.getCursorScreenPosX() + time / TIMELINE_END * timelineSize.x;
            float tickY = ImGui.getCursorScreenPosY() - 10;
            float tickEndY = tickY + timelineSize.y - 10;

            ImGui.getForegroundDrawList().addLine(tickX, tickY, tickX, tickEndY, 0xFFFFFFFF, 1.0f);
            ImGui.getForegroundDrawList().addText(tickX - 5, tickY - 20, 0xFFFFFFFF, String.format("%.1f", time));
        }

        ImGui.separator();

        // Right click menu
        handleRightClick();

        // Render tracks below timeline
        renderTracks();
    }

    private static void renderTracks() {
        // Draw tracks below the timeline
        ListIterator<Track> iterator = tracks.listIterator();
        float initialPosY = ImGui.getCursorPosY();
        final int offset = 70;
        int i = 0;
        while (iterator.hasNext()) {
            float y = initialPosY + (i * Track.TRACK_HEIGHT) + (40 * i) + offset;
            iterator.next().render(iterator, y);
            i++;
        }
    }

    private static void handleRightClick() {
        if (ImGui.isMouseReleased(ImGuiMouseButton.Right)) {
            // Right-clicked on the background, show context menu
            ImGui.openPopup("ContextMenu");
        }

        // Context menu
        if (ImGui.beginPopup("ContextMenu")) {
            if (ImGui.menuItem("Add Track")) {
                Track newTrack = new Track();
                newTrack.updateY();
                tracks.add(newTrack);
            }

            ImGui.endPopup();
        }
    }
}
