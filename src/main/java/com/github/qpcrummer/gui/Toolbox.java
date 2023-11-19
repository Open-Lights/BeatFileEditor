package com.github.qpcrummer.gui;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiColorEditFlags;
import imgui.flag.ImGuiWindowFlags;

public class Toolbox {
    public static final short TOOLBOX_HEIGHT = 100;
    private static final short TOOLBOX_ITEMS = 2;
    private static final short ITEM_DIMENSION = 50;
    private static final float ITEM_RADIUS = ITEM_DIMENSION * 0.5f;
    private static final short ITEM_SELECTION_WIDTH = 4;
    private static final short STRING_WIDTH = (short) Math.round(ImGui.calcTextSize("Toolbox").x);
    private static final float[] RED = new float[]{255, 0, 0};
    private static int selectedButton = -1;

    public static void render() {
        ImGui.beginChild("ToolboxRegion", ImGui.getIO().getDisplaySizeX(), TOOLBOX_HEIGHT - 2, false, ImGuiWindowFlags.NoScrollbar);
        ImGui.separator();

        ImGui.setCursorPosX(evenlyDistribute((short) 1, STRING_WIDTH, ImGui.getIO().getDisplaySizeX(), 0));
        ImGui.text("Toolbox");

        for (int i = 0; i < TOOLBOX_ITEMS; i++) {
            ImGui.setCursorPosY(evenlyDistribute((short) 1, ITEM_DIMENSION, TOOLBOX_HEIGHT, 0));
            ImGui.setCursorPosX(evenlyDistribute(TOOLBOX_ITEMS, ITEM_DIMENSION, ImGui.getIO().getDisplaySizeX(), i));

            float centerX = ImGui.getCursorScreenPosX() + ITEM_RADIUS;
            float centerY = ImGui.getCursorScreenPosY() + ITEM_RADIUS;

            if (ImGui.colorButton("##" + i, RED, ImGuiColorEditFlags.NoTooltip, ITEM_DIMENSION, ITEM_DIMENSION)) {
                if (selectedButton == i) {
                    selectedButton = -1;
                } else {
                    selectedButton = i;
                }
            }

            if (selectedButton == i) {
                ImGui.getForegroundDrawList().addRectFilled(centerX - ITEM_SELECTION_WIDTH - ITEM_RADIUS, centerY - ITEM_SELECTION_WIDTH - ITEM_RADIUS, centerX + ITEM_SELECTION_WIDTH + ITEM_RADIUS, centerY + ITEM_SELECTION_WIDTH + ITEM_RADIUS, ImGui.getColorU32(ImGuiCol.Border));
            }
        }

        ImGui.endChild();
    }

    private static float evenlyDistribute(final short totalItems, final short itemSize, final float totalSize, final int i) {
        return ((totalSize - (totalItems * itemSize)) / (totalItems + 1)) * (i + 1) + i * itemSize;
    }
}
