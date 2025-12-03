package com.vocalis;

import org.vosk.Model;
import org.vosk.Recognizer;
import org.vosk.LibVosk;
import javax.sound.sampled.*;

public class STTTest {

    public static void main(String[] args) {
        try {
            //LibVosk.setLogLevel(...);

            Model model = new Model("vosk-model-small-en-us-0.15");

            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);

            microphone.open(format);
            microphone.start();

            Recognizer recognizer = new Recognizer(model, 16000);
            System.out.println("🎤 Speak now…");

            byte[] buffer = new byte[4096];

            while (true) {
                int bytesRead = microphone.read(buffer, 0, buffer.length);
                if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                    System.out.println("You said: " + recognizer.getResult());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}