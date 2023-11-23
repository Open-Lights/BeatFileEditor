package com.github.qpcrummer.gui;

public final class GUItils {
    public static float evenlyDistribute(final short totalItems, final short itemSize, final float totalSize, final int i) {
        return ((totalSize - (totalItems * itemSize)) / (totalItems + 1)) * (i + 1) + i * itemSize;
    }
}
