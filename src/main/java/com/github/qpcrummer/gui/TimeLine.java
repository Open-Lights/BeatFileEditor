package com.github.qpcrummer.gui;

import imgui.ImGui;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiWindowFlags;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class TimeLine {
    private static final float TIMELINE_START = 0.0f;
    private static final float TIMELINE_END = 600f;
    public static final List<Track> tracks = new ArrayList<>();

    public static void render() {
        ImGui.beginChild("TimeLineViewRegion", ImGui.getIO().getDisplaySizeX(), ImGui.getIO().getDisplaySizeY() - 36, false, ImGuiWindowFlags.HorizontalScrollbar);

        // Set the fixed height of the timeline
        float timelineHeight = 30;
        float totalSeconds = TIMELINE_END - TIMELINE_START;
        float secondsTickSpacing = 100.0f;  // Pixels between each "second" tick
        float msTickSpacing = secondsTickSpacing * 0.1F; // Pixels between each 10ms sub-tick

        // Draw the timeline background
        ImGui.dummy(TIMELINE_END * secondsTickSpacing + secondsTickSpacing, timelineHeight);

        // Draw tick marks at each second interval with labels
        for (int i = 0; i <= (totalSeconds * secondsTickSpacing); i++) {
            float seconds = TIMELINE_START + i;

            float tickX = ImGui.getCursorScreenPosX() + i * secondsTickSpacing;
            float tickY = ImGui.getCursorScreenPosY() - 10;
            float tickEndY = tickY + timelineHeight - 10;

            ImGui.getForegroundDrawList().addLine(tickX, tickY, tickX, tickEndY, 0xFFFFFFFF, 1.0f);

            // Draw shorter ticks at every 10px (representing 10ms intervals)
            for (float subTick = 0; subTick < secondsTickSpacing; subTick += msTickSpacing) {
                float subTickX = tickX + subTick;
                float subTickEndY = tickY + 0.5f * timelineHeight - 10;

                ImGui.getForegroundDrawList().addLine(subTickX, tickY, subTickX, subTickEndY, 0xFFFFFFFF, 0.5f);
            }

            // Draw text above large ticks
            ImGui.getForegroundDrawList().addText(tickX - 5, tickY - 20, 0xFFFFFFFF, String.format("%.0f", seconds));
        }

        ImGui.separator();

        // Right click menu
        handleRightClick();

        // Render tracks below timeline
        renderTracks();

        ImGui.endChild();
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
