package com.github.qpcrummer.gui.layout_manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ScalableGridBagLayout extends GridBagLayout {
    private void scale(ScalablePanel panel, int xOffset, int yOffset) {
        if (xOffset != 0 && !panel.getxScalable()) {
            xOffset = 0;
        }
        if (yOffset != 0 && !panel.getyScalable()) {
            yOffset = 0;
        }
        panel.setSize(panel.getWidth() + xOffset, panel.getHeight() + yOffset);
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        super.addLayoutComponent(name, comp);
        makeScalable(comp);
    }

    private void makeScalable(Component component) {
        if (component instanceof ScalablePanel) {
            component.addMouseListener(new MouseAdapter() {
                int startX;
                int startY;
                Directions border;

                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        border = getNearBorder(e.getPoint(), component.getBounds());
                        if (border != Directions.NONE) {
                            startX = e.getXOnScreen();
                            startY = e.getYOnScreen();
                        }
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (border != Directions.NONE) {
                            int displacementX = startX - e.getXOnScreen();
                            int displacementY = startY - e.getYOnScreen();

                            scale((ScalablePanel) component, displacementX, displacementY);
                        }
                    }
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    super.mouseMoved(e);

                    Directions border = getNearBorder(e.getPoint(), component.getBounds());

                    if (border == Directions.UP || border == Directions.DOWN) {
                        component.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                    } else if (border == Directions.LEFT || border == Directions.RIGHT) {
                        component.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                    } else {
                        component.setCursor(Cursor.getDefaultCursor());
                    }
                }
            });
        }
    }

    private final int edgeThreshold = 5;

    private boolean isNearLeftBorder(Point mousePoint, Rectangle bounds) {
        return Math.abs(mousePoint.getX() - bounds.getX()) <= edgeThreshold;
    }

    private boolean isNearRightBorder(Point mousePoint, Rectangle bounds) {
        return Math.abs(mousePoint.getX() - bounds.getMaxX()) <= edgeThreshold;
    }

    private boolean isNearTopBorder(Point mousePoint, Rectangle bounds) {
        return Math.abs(mousePoint.getY() - bounds.getY()) <= edgeThreshold;
    }

    private boolean isNearBottomBorder(Point mousePoint, Rectangle bounds) {
        return Math.abs(mousePoint.getY() - bounds.getMaxY()) <= edgeThreshold;
    }

    private Directions getNearBorder(Point mousePoint, Rectangle bounds) {
        if (isNearBottomBorder(mousePoint, bounds)) {
            return Directions.DOWN;
        } else if (isNearTopBorder(mousePoint, bounds)) {
            return Directions.UP;
        } else if (isNearLeftBorder(mousePoint, bounds)) {
            return Directions.LEFT;
        } else  if (isNearRightBorder(mousePoint, bounds)) {
            return Directions.RIGHT;
        } else {
            return Directions.NONE;
        }
    }

    private boolean isNearBorder(Point mousePoint, Rectangle bounds) {
        return isNearBottomBorder(mousePoint, bounds) || isNearTopBorder(mousePoint, bounds) || isNearLeftBorder(mousePoint, bounds) || isNearRightBorder(mousePoint, bounds);
    }

    private enum Directions {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        NONE
    }
}
