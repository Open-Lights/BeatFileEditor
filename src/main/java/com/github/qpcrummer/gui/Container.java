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

public class Container {
    private final ImString channels = new ImString();
    private final List<String> stringList = new ArrayList<>();
    public static final ImVec2 stringDimensions = ImGui.calcTextSize("[1000000, 1000000]");

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

    public Container() {
        // Initial strings in the list
        stringList.add("0");
        stringList.add("[1, 2]");
    }

    public void render(Iterator<Container> iterator, int index, float x) {
        ImGui.pushItemWidth(stringDimensions.x * 0.5F + 5);
        ImGui.inputText("##IntegerArrayInput", this.channels, ImGuiInputTextFlags.CallbackCharFilter, this.CALLBACK);
        ImGui.popItemWidth();

        ImGui.sameLine();

        if (ImGui.button("Remove##" + index)) {
            iterator.remove();
        }

        ImGui.pushItemWidth(stringDimensions.x);

        // Render modifiable strings
        int clicked = -1;

        for (int i = 0; i < stringList.size(); i++) {
            ImString string = new ImString(stringList.get(i));
            ImGui.setCursorPosX(x);

            if (ImGui.inputText("##" + index + ";" + i, string, ImGuiInputTextFlags.CallbackResize)) {
                stringList.set(i, string.toString());
            }

            // Check for right-click and open context menu
            if (ImGui.isItemHovered() && ImGui.isItemClicked(ImGuiMouseButton.Right)) {
                clicked = i;
            }
        }

        // Handle completion of the CompletableFuture
        if (clicked != -1) {
            popUpMenu(clicked);
        }


        ImGui.popItemWidth();
    }

    private void popUpMenu(final int index) {
        ImGui.openPopup("Context Menu");

        if (ImGui.beginPopup("Context Menu")) {
            if (ImGui.menuItem("Add Beat Before")) {
                this.stringList.add(index, "");
            }
            if (ImGui.menuItem("Add Beat After")) {
                this.stringList.add(index + 1, "");
            }
            ImGui.endPopup();
        }
    }

    public void modifyContainerName(String newName) {
        this.channels.set(newName);
    }

    public int[] getChannels() {
        return parseIntArray(this.channels.get());
    }

    public List<Object> getCorrectedList() {
        List<Object> list = new ArrayList<>();

        for (String str : this.stringList) {
            if (str.contains("[")) {
                list.add(parseLongArray(str));
            } else {
                list.add(Long.valueOf(str));
            }
        }

        return list;
    }

    private static long[] parseLongArray(String input) {
        // Remove brackets and split the string by ","
        String[] numberStrings = input.replaceAll("\\[|\\]", "").split(",");

        // Convert string array to long array
        long[] result = new long[numberStrings.length];
        for (int i = 0; i < numberStrings.length; i++) {
            result[i] = Long.parseLong(numberStrings[i].trim());
        }

        return result;
    }

    private static int[] parseIntArray(String input) {
        // Remove brackets and split the string by ","
        String[] numberStrings = input.replaceAll("\\[|\\]", "").split(",");

        // Convert string array to long array
        int[] result = new int[numberStrings.length];
        for (int i = 0; i < numberStrings.length; i++) {
            result[i] = Integer.parseInt(numberStrings[i].trim());
        }

        return result;
    }
}
