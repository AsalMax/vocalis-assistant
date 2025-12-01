package com.vocalis;

import javax.sound.sampled.*;

public class Microphone {
    private TargetDataLine line;

    public Microphone(int sampleRate) throws LineUnavailableException {
        AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("Microphone not supported with 16kHz format");
        }

        line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();
    }

    public int read(byte[] buffer) {
        return line.read(buffer, 0, buffer.length);
    }

    public void close() {
        if (line != null) {
            line.stop();
            line.close();
        }
    }
}