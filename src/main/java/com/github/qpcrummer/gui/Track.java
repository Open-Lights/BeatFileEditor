package com.github.qpcrummer.gui;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.callback.ImGuiInputTextCallback;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiMouseButton;
import imgui.type.ImString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Track {
    private float y;
    public static final short TRACK_HEIGHT = 100;
    public static final short TRACK_OFFSET = 70;
    private final short TRACK_SPACING = 40;
    private final short DELETE_BUTTON_SIZE = 20;

    private final ImString inputTextValue = new ImString(); // Initial value for the input text
    private final StringBuilder OUTPUT = new StringBuilder();
    private final ImGuiInputTextCallback CALLBACK = new ImGuiInputTextCallback() {
        @Override
        public void accept(final imgui.ImGuiInputTextCallbackData data) {
            final char c = (char) data.getEventChar();
            if (!Character.isDigit(c) && c != ',') {
                data.setEventChar(0);
                OUTPUT.append("Invalid character entered: ").append(c).append('\n');
            } else {
                OUTPUT.append("Typed: ").append(c).append('\n');
            }
        }
    };


    // Single Beat Storage
    public final List<Object> beats = new ArrayList<>();

    public void render(Iterator<Track> iterator, float yPos) {
        // Draw the track as a rectangle with white lines on top and bottom
        float trackWidth = ImGui.getIO().getDisplaySizeX();

        float trackBottom = yPos + TRACK_HEIGHT;

        // Draw the rectangle
        ImGui.getBackgroundDrawList().addRectFilled(trackWidth, yPos, 0, trackBottom, 0xFFFFFFFF);
        ImGui.getForegroundDrawList().addRect(trackWidth, yPos, 0, trackBottom, 0xFFFFFFFF);


        // Get mouse position
        ImVec2 mousePos = ImGui.getMousePos();

        // Check if mouse position is within the rectangle's bounds
        if (mousePos.x >= 0 && mousePos.x <= trackWidth && mousePos.y >= yPos && mousePos.y <= trackBottom) {
            // Handle Left Click
            if (ImGui.isMouseClicked(ImGuiMouseButton.Left)) {
                BeatRenderer.handleLeftClick(this);
                System.out.println("Left Click; " + beats.size());
            }
        }

        // Draw the delete button inside the rectangle
        ImGui.setCursorPosY(yPos - 50f);
        if (ImGui.button("X##" + y, DELETE_BUTTON_SIZE, DELETE_BUTTON_SIZE)) {
            iterator.remove();
        }

        // Draw the input text for integers and commas inside the rectangle
        ImGui.sameLine(); // This sets the cursor position to the same line
        ImGui.inputText("##InputText" + yPos, inputTextValue, ImGuiInputTextFlags.CallbackCharFilter, this.CALLBACK);
    }


    public void updateY() {
        // Calculate the Y position for a new track
        if (!TimeLine.tracks.isEmpty()) {
            Track lastTrack = TimeLine.tracks.get(TimeLine.tracks.size() - 1);
            y = lastTrack.y + TRACK_HEIGHT + TRACK_SPACING;
        } else {
            // Set initial Y position below the timeline
            y = ImGui.getCursorPosY() + TimeLine.TIMELINE_HEIGHT + TRACK_SPACING;
        }
    }

    public void setChannelText(String text) {
        this.inputTextValue.set(text);
    }

    public String getChannels() {
        return this.inputTextValue.get();
    }
}