package com.github.qpcrummer.gui;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Container {
    private String headerText = "Container";
    private final List<String> stringList = new ArrayList<>();
    public static final ImVec2 stringDimensions = ImGui.calcTextSize("[1000000, 1000000]");

    public Container() {
        // Initial strings in the list
        stringList.add("0");
        stringList.add("[1, 2]");
    }

    public void render(Iterator<Container> iterator, int index, float x) {
        ImGui.text(headerText);
        ImGui.sameLine();

        if (ImGui.button("Remove##" + index)) {
            iterator.remove();
        }

        ImGui.pushItemWidth(stringDimensions.x);

        // Render modifiable strings
        for (int i = 0; i < stringList.size(); i++) {
            ImString string = new ImString(stringList.get(i));
            ImGui.setCursorPosX(x);
            if (ImGui.inputText("##" + index + ";" + i, string, ImGuiInputTextFlags.CallbackResize)) {
                stringList.set(i, string.toString());
            }
        }

        ImGui.popItemWidth();
    }

    public void modifyContainerName(String newName) {
        this.headerText = newName;
    }

    // TODO Get these values from the name
    public int[] getChannels() {
        return new int[]{0, 1};
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
}
