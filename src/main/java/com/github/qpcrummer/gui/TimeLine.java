package com.github.qpcrummer.gui;

import com.github.qpcrummer.music.MusicPlayer;
import imgui.ImGui;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiWindowFlags;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class TimeLine {
    public static short TOTAL_TIME_MODIFIER = 1;
    public static int TOTAL_TIME = 600 * TOTAL_TIME_MODIFIER;
    public static short LARGE_TICK_SPACING = 100;
    public static float SMALL_TICK_SPACING = LARGE_TICK_SPACING * 0.1f;
    public static final short TIMELINE_HEIGHT = 30;
    public static float CULLING_PADDING = (14f / LARGE_TICK_SPACING) * 100;
    public static final List<Track> tracks = new ArrayList<>();

    public static void render() {
        boolean leftClick = ImGui.isMouseClicked(ImGuiMouseButton.Left);

        ImGui.beginChild("TimeLineViewRegion", ImGui.getIO().getDisplaySizeX(), ImGui.getIO().getDisplaySizeY() - 36 - Toolbox.TOOLBOX_HEIGHT, false, ImGuiWindowFlags.HorizontalScrollbar);

        // Set the fixed height of the timeline
        float scrollBarPosPercentage = ImGui.getScrollX() / ImGui.getScrollMaxX();
        float visibleRange = scrollBarPosPercentage * TOTAL_TIME;

        // Draw the timeline background
        ImGui.dummy(TOTAL_TIME * LARGE_TICK_SPACING + LARGE_TICK_SPACING, TIMELINE_HEIGHT);

        // Draw tick marks at each second interval with labels
        for (int i = 0; i <= (TOTAL_TIME * LARGE_TICK_SPACING); i++) {
            if (!notCulled(visibleRange, i)) {
                continue;
            }

            float tickX = ImGui.getCursorScreenPosX() + i * LARGE_TICK_SPACING;
            float tickY = ImGui.getCursorScreenPosY() - 10;
            float tickEndY = tickY + TIMELINE_HEIGHT - 10;

            ImGui.getForegroundDrawList().addLine(tickX, tickY, tickX, tickEndY, 0xFFFFFFFF, 1.0f);

            if (MainGUI.zoom != MainGUI.STANDARD_VIEW) {
                // Draw shorter ticks at every 10px (representing 10ms intervals)
                for (float subTick = 0; subTick < LARGE_TICK_SPACING; subTick += SMALL_TICK_SPACING) {
                    float subTickX = tickX + subTick;
                    float subTickEndY = tickY + 0.5f * TIMELINE_HEIGHT - 10;

                    ImGui.getForegroundDrawList().addLine(subTickX, tickY, subTickX, subTickEndY, 0xFFFFFFFF, 0.5f);

                    renderLinesOnTracks(subTickX, (int) (i * 10 + subTick));
                }
            } else {
                renderLinesOnTracks(tickX, i);
            }

            // Draw text above large ticks
            switch (MainGUI.zoom) {
                case MainGUI.MS_VIEW -> ImGui.getForegroundDrawList().addText(tickX - 5, tickY - 20, 0xFFFFFFFF, String.format("%.2f", (float) i * 0.01));
                case MainGUI.TEN_MS_VIEW -> ImGui.getForegroundDrawList().addText(tickX - 5, tickY - 20, 0xFFFFFFFF, String.format("%.1f", (float) i * 0.1));
                default -> ImGui.getForegroundDrawList().addText(tickX - 5, tickY - 20, 0xFFFFFFFF, String.format("%.0f", (float) i));
            }
        }

        ImGui.separator();

        // Right click menu
        handleRightClick();

        // Render tracks below timeline
        renderTracks(leftClick);

        // Render music progress line
        renderMusicProgressLine(18, TIMELINE_HEIGHT + 18, TOTAL_TIME * LARGE_TICK_SPACING + LARGE_TICK_SPACING);

        ImGui.endChild();
    }

    private static boolean notCulled(float visibleRange, int current) {
        return Math.abs(current - visibleRange) <= CULLING_PADDING;
    }

    private static void renderTracks(boolean leftClick) {
        // Draw tracks below the timeline
        ListIterator<Track> iterator = tracks.listIterator();
        float initialPosY = ImGui.getCursorPosY();
        int i = 0;
        while (iterator.hasNext()) {
            float y = initialPosY + (i * Track.TRACK_HEIGHT) + (40 * i) + Track.TRACK_OFFSET;
            iterator.next().render(iterator, y);
            i++;
        }
    }

    private static void renderLinesOnTracks(float xValue, int tick) {
        for (int i = 0; i < tracks.size(); i++) {
            float trackPosY = ImGui.getCursorPosY() + (i * Track.TRACK_HEIGHT) + (40 * i) + Track.TRACK_OFFSET + 3;
            ImGui.getForegroundDrawList().addLine(xValue, trackPosY + Track.TRACK_HEIGHT, xValue, trackPosY, 0xFF0000FF, 0.5f);
            BeatRenderer.render(tick, tracks.get(i), xValue, trackPosY, SMALL_TICK_SPACING);
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

    private static void renderMusicProgressLine(float yMin, float yMax, float screenLength) {
        float songProgress = (float) MusicPlayer.getPositionMilliseconds() / MusicPlayer.getSongLengthMilliseconds();
        float desiredScrollX = songProgress * screenLength;

        float middleScreenX = ImGui.getIO().getDisplaySizeX() / 2.0f;

        // Only move the music line if it's not in the middle of the screen
        if (desiredScrollX <= middleScreenX) {
            ImGui.getForegroundDrawList().addLine(desiredScrollX, yMin, desiredScrollX, yMax, 0xFF0000FF, 0.5f);
        } else {
            ImGui.getForegroundDrawList().addLine(middleScreenX, yMin, middleScreenX, yMax, 0xFF0000FF, 0.5f);
        }

        float currentScrollX = ImGui.getScrollX();

        // Check if the music line is in the middle of the screen
        if (desiredScrollX > middleScreenX) {
            float desiredSpeed = (desiredScrollX - currentScrollX) / ImGui.getIO().getDeltaTime();

            // Adjust the scrolling speed
            if (MusicPlayer.playing) {
                ImGui.setScrollX(currentScrollX + desiredSpeed * ImGui.getIO().getDeltaTime());
            }
        }
    }
}
