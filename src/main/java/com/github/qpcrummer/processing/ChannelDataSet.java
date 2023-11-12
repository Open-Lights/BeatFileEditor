package com.github.qpcrummer.processing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChannelDataSet {
    private final List<Integer> channels;
    private final List<Object> beats = new ArrayList<>();
    public ChannelDataSet(Integer[] channels) {
        this.channels = Arrays.asList(channels);
    }

    public List<Integer> getChannels() {
        return this.channels;
    }

    public void addChannel(int channel) {
        this.channels.add(channel);
    }

    public void removeChannel(int channel) {
        this.channels.remove(channel);
    }

    public void addBeat(Object millisecondPosition) {
        long position;
        if (millisecondPosition instanceof Long[] longs) {
            position = longs[0];
        } else {
            position = (long) millisecondPosition;
        }

        for (int i = 0; i < this.beats.size(); i++) {
            Object object = this.beats.get(i);
            if (object instanceof Long l) {
                if (position < l) {
                    this.beats.add(i, millisecondPosition);
                    return;
                }
            } else if (object instanceof Long[] longs) {
                if (position < longs[0]) {
                    this.beats.add(i, millisecondPosition);
                    return;
                }
            }
        }
    }

    public void removeBeat(Object millisecondPosition) {
        this.beats.remove(millisecondPosition);
    }

    public List<Object> getBeats() {
        return this.beats;
    }

}
