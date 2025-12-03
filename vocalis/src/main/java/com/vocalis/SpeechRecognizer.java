package com.vocalis;

import org.vosk.Model;
import org.vosk.Recognizer;
import javax.sound.sampled.*;
import java.io.IOException;

public class SpeechRecognizer {
    private Model model;
    private Recognizer recognizer;
    private Microphone mic;
    private volatile boolean listening = false;

    public SpeechRecognizer() {
        try {
            // 1. Load Vosk model (change path if needed)
            String modelPath = "C:/Users/yojus/vocalis-assistant/vocalis/vosk-model-small-en-us-0.15";
            // For portable version, you can also use:
            // String modelPath = System.getProperty("user.dir") + "/vosk-model-small-en-us-0.15";

            model = new Model(modelPath);
            recognizer = new Recognizer(model, 16000.0f); // 16kHz sample rate

            // 2. Initialize microphone (MUST be before any thread starts!)
            mic = new Microphone(16000);

            System.out.println("SpeechRecognizer initialized successfully!");
        } catch (Exception e) {
            System.err.println("Failed to initialize Vosk SpeechRecognizer:");
            e.printStackTrace();
        }
    }

    public void startListening() {
        if (listening || mic == null || recognizer == null) {
            System.out.println("Already listening or cannot start listening (mic/recognizer not ready)");
            return;
        }

        listening = true;
        new Thread(() -> {
            byte[] buffer = new byte[4096];
            try {
                System.out.println("Listening... Speak now!");
                while (listening) {
                    int bytesRead = mic.read(buffer);
                    if (bytesRead > 0) {
                        if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                            String result = recognizer.getResult();
                            System.out.println("You said: " + result);
                            // TODO: send result to your assistant logic here
                        }
                        // Optional: show live text:
                        // else {
                        //     System.out.println("Partial: " + recognizer.getPartialResult());
                        // }
                    }
                }
            } catch (Exception e) {
                if (listening) { // only print if not normal stop
                    System.err.println("Error during speech recognition:");
                    e.printStackTrace();
                }
            } finally {
                if (mic != null) mic.close();
                System.out.println("Speech recognition stopped.");
            }
        }, "Vosk-Listening-Thread").start();
    }

    public void stopListening() {
        listening = false;
    }

    // Optional: clean up when app closes
    public void close() {
        stopListening();
        if (recognizer != null) recognizer.close();
        if (model != null) model.close();
    }
}