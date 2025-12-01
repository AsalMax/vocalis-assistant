package com.vocalis;

import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import java.util.Locale;

public class VoiceEngine {
    private Synthesizer synth;

    public VoiceEngine() {
        try {
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
            synth = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
            synth.allocate();
            synth.resume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void speak(String text) {
        System.out.println("Vocalis: " + text);
        synth.speakPlainText(text, null);
        try { Thread.sleep(text.length() * 70L); } catch (Exception ignored) {}
    }
}