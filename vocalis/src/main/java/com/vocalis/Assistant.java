package com.vocalis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Assistant {
    private final VoiceEngine voice = new VoiceEngine();
    private final SpeechRecognizer recognizer = new SpeechRecognizer();
    private final CalendarManager calendar = new CalendarManager();
    private final Properties config = ConfigLoader.load();

    public void startMorningRoutine() {
        voice.speak(greeting() + " " + config.getProperty("user.name") + "!");
        voice.speak("Today is " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d")));
        calendar.readTodayEvents(voice);
        voice.speak("I'm listening. Say " + config.getProperty("wake.word") + " to wake me.");
        recognizer.startListening();
    }

    public void handleCommand(String text) {
        String cmd = text.toLowerCase();
        if (cmd.contains("time")) {
            voice.speak("It is " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("h:mm a")));
        } else if (cmd.contains("next event") || cmd.contains("schedule")) {
            calendar.readTodayEvents(voice);
        } else if (cmd.contains("add")) {
            calendar.addSimpleEvent(cmd, voice);
        } else {
            voice.speak("I heard " + text);
        }
    }

    private String greeting() {
        int hour = LocalDateTime.now().getHour();
        if (hour < 12) return "Good morning";
        if (hour < 18) return "Good afternoon";
        return "Good evening";
    }
}