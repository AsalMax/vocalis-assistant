package com.vocalis;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class App {
    public static void main(String[] args) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        Voice voice = VoiceManager.getInstance().getVoice("kevin16");
        if (voice == null) {
            System.out.println("Cannot find voice! Check FreeTTS installation.");
            return;
        }

        voice.allocate();
        System.out.println("Speaking...");
        voice.speak("Hello from Vocalis! FreeTTS is working perfectly on your machine!");
        voice.deallocate();
    }
}