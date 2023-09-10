package com.github.qpcrummer.gui;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainFrame extends JFrame {
    private final JButton beatButton = new JButton("Beat");
    private final JFileChooser songSelector = new JFileChooser();
    private boolean running;
    private boolean isDuringCountdown;
    private Clip clip;
    private File song;
    private final BeatWriter writer = new BeatWriter();
    private boolean shiftPressed = false;
    private long shiftTimeStampPressed = 0;

    private final KeyListener shiftListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int keyCode = e.getKeyCode();
            if (!shiftPressed && keyCode == KeyEvent.VK_SHIFT) {
                System.out.println("Shift is pressed!");
                shiftTimeStampPressed = clip.getMicrosecondPosition();
                shiftPressed = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_SHIFT) {
                System.out.println("Shift is released!");
                long shiftTimeStampReleased = clip.getMicrosecondPosition();
                writer.writeClipTimeStamp(shiftTimeStampPressed, shiftTimeStampReleased);
                shiftPressed = false;
            }
        }
    };
    public MainFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 400));

        JPanel mainPanel = new JPanel();
        add(mainPanel);

        // MainPanel
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(songSelector);
        mainPanel.add(new JSeparator());
        mainPanel.add(beatButton);

        beatButton.setFocusable(false);

        beatButton.addActionListener(e -> {
            if (!isDuringCountdown && running && !shiftPressed) {
                System.out.println("Beat");
                writer.writeClipTimeStamp();
            } else if (!running) {
                startSimulation();
            }
        });
    }

    /**
     * Gets the Selected Song
     * @return Selected Song as a File
     */
    private File getFileFromFileChooser() {
        return songSelector.getSelectedFile();
    }

    /**
     * Starts the Beat Simulator
     */
    private void startSimulation() {
        System.out.println("Starting Simulation");
        song = getFileFromFileChooser();
        if (song != null) {
            isDuringCountdown = true;
            running = true;

            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(song);
                clip = AudioSystem.getClip();
                clip.open(stream);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }

            writer.setClip(clip);

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    removeKeyListener(shiftListener);
                    writer.saveData();
                    running = false;
                    clip = null;
                    song = null;
                }
            });

            System.out.println("Starting Countdown");

            Timer countdownTimer = getTimer();

            countdownTimer.start();
        }
    }

    /**
     * Fetches the Timer
     * @return new Timer
     */
    private Timer getTimer() {
        final int[] countdownSeconds = {5};
        return new Timer(1000, e -> {
            beatButton.setText(String.valueOf(countdownSeconds[0]));
            System.out.println(countdownSeconds[0]);
            countdownSeconds[0]--;

            if (countdownSeconds[0] < 0) {
                ((Timer) e.getSource()).stop();
                isDuringCountdown = false;
                beatButton.setText("Beat");
                System.out.println("Starting Music");
                clip.start();
                addKeyListener(shiftListener);
                requestFocusInWindow();
            }
        });
    }

    class BeatWriter {
        private Clip clip;

        /**
         * Replaces the existing clip (if it exists) with a new one
         * @param clip The new clip to replace the old
         */
        public void setClip(Clip clip) {
            this.clip = clip;
        }

        private StringWriter output = null;

        /**
         * Gets the current time of the song and writes it to a StringWriter to be saved later
         */
        public void writeClipTimeStamp() {
            String data = Long.toHexString(clip.getMicrosecondPosition());
            if (output != null) {
                try (BufferedWriter writer = new BufferedWriter(output)) {
                    writer.write(data);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                output = new StringWriter();
            }
        }

        /**
         * Gets the specified time stamps of the song and writes it to a StringWriter to be saved later.
         * If the range between time stamps is 100ms or less, only the first time stamp is taken
         * @param timeStampStart First time stamp
         * @param timeStampEnd Last time stamp
         */
        public void writeClipTimeStamp(long timeStampStart, long timeStampEnd) {
            String data;
            if (timeStampEnd - timeStampStart > 100000) {
                String time1 = Long.toHexString(timeStampStart);
                String time2 = Long.toHexString(timeStampEnd);

               data = "[" + time1 + ", " + time2 + "]";
            } else {
                data = Long.toHexString(timeStampStart);
            }

            if (output != null) {
                try (BufferedWriter writer = new BufferedWriter(output)) {
                    writer.write(data);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                output = new StringWriter();
            }
        }

        /**
         * Saves the data from the StringWriter
         */
        public void saveData() {
            System.out.println("Saving");
            String songName = song.getName().replace(".wav", "");

            String date = new SimpleDateFormat("yy-MM-dd-HH-mm-ss", Locale.US).format(new Date());

            String virtualFileName = songName + "-" + date + ".txt";
            String virtualFileContent = output.toString();

            try (FileWriter fileWriter = new FileWriter(virtualFileName);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write(virtualFileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


