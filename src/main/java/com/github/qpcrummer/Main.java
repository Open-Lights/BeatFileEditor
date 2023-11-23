package com.github.qpcrummer;

import com.github.qpcrummer.directory.Directory;
import com.github.qpcrummer.gui.FileExplorer;
import com.github.qpcrummer.gui.MainGUI;
import com.github.qpcrummer.gui.Recorder;
import com.github.qpcrummer.gui.Warning;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.app.Application;
import imgui.app.Configuration;

import java.util.logging.Logger;

public class Main extends Application {
    private long previousTime = System.currentTimeMillis();
    private int targetFrameRate = (1000 / 5);
    private int mouseActivityCountdown;
    private final ImVec2 previousMousePos = new ImVec2();
    public static final Logger logger = Logger.getLogger("Christmas Celebrator Song Editor");

    // Toggles
    public static boolean enableFileExplorer;
    public static boolean enableRecorder;
    public static boolean enableWarning;

    public static void main(String[] args) {
        Directory.createDirectories();
        launch(new Main());
    }
    @Override
    protected void preRun() {
        super.preRun();
        logger.info("Loading Christmas Celebrator Song Editor");
    }

    @Override
    public void process() {
        setFPSLimit(30);
        //if (mouseActivity()) {
        //    setFPSLimit(30);
        //} else {
        //    setFPSLimit(5);
        //}

        MainGUI.render();

        if (enableFileExplorer) {
            FileExplorer.render();
        }

        if (enableRecorder) {
            Recorder.render();
        }

        if (enableWarning) {
            Warning.render();
        }
    }

    @Override
    protected void configure(Configuration config) {
        config.setTitle("Christmas Celebrator Song Editor");
        config.setHeight(1300);
        config.setWidth(1300);
    }

    @Override
    protected void runFrame() {
        long currentTime = System.currentTimeMillis();
        double elapsedTime = currentTime - this.previousTime;

        if (elapsedTime >= targetFrameRate) {
            super.runFrame();
            this.previousTime = currentTime;
        }

    }

    @Override
    protected void disposeWindow() {
        super.disposeWindow();
        System.exit(1);
    }

    private int previousFPSValue;
    private void setFPSLimit(int fps) {
        if (this.previousFPSValue == fps) {
            return;
        }
        this.previousFPSValue = fps;
        this.targetFrameRate = (1000 / fps);
    }


    private boolean mouseActivity() {
        if (this.mouseActivityCountdown > 0) {
            this.mouseActivityCountdown--;
            return true;
        } else if (ImGui.isAnyMouseDown() || mouseMoved()) {
            this.mouseActivityCountdown = 100;
            return true;
        }
        return false;
    }


    private boolean mouseMoved() {
        ImVec2 currentMousePos = ImGui.getMousePos();
        if (!currentMousePos.equals(previousMousePos)) {
            previousMousePos.set(currentMousePos);
            return true;
        }
        return false;
    }
}