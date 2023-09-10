package com.github.qpcrummer.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AudioEngine {

    private Clip song;
    private long microSecPosition;

    public AudioEngine() {
    }

    public void loadSong(File song) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(song);
            this.song = AudioSystem.getClip();
            this.song.open(stream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void playSong(boolean beginning) {
        if (this.song != null) {
            this.song.start();
            if (!beginning) {
                this.song.setMicrosecondPosition(this.microSecPosition);
            }
        }
    }

    public void pauseSong() {
        if (this.song != null) {
            this.microSecPosition = this.song.getMicrosecondPosition();
            this.song.stop();
        }
    }

    public void setMillisecondPosition(long ms) {
        if (this.song != null) {
            this.microSecPosition = TimeUnit.MILLISECONDS.toMicros(ms);
        }
    }

    public int getSecondChunks() {
        if (this.song != null) {
            return Math.round(TimeUnit.MICROSECONDS.toSeconds(this.song.getMicrosecondLength()));
        }
        return 0;
    }
}
