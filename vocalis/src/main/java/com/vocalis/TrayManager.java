package com.vocalis;

import java.awt.*;

public class TrayManager {
    public static void createTrayIcon(Assistant assistant) {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray not supported on this OS");
            return;
        }

        try {
            // Create a tiny 16×16 coloured square (no file needed)
            Image image = new java.awt.image.BufferedImage(16, 16, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) image.getGraphics();
            g.setColor(new Color(70, 130, 180));     // nice blue
            g.fillRect(0, 0, 16, 16);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            g.drawString("V", 4, 12);
            g.dispose();

            TrayIcon trayIcon = new TrayIcon(image, "Vocalis Assistant");
            trayIcon.setImageAutoSize(true);

            PopupMenu popup = new PopupMenu();

            MenuItem helloItem = new MenuItem("Say hello again");
            helloItem.addActionListener(e -> assistant.startMorningRoutine());

            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(e -> System.exit(0));

            popup.add(helloItem);
            popup.addSeparator();
            popup.add(exitItem);

            trayIcon.setPopupMenu(popup);
            SystemTray.getSystemTray().add(trayIcon);

            trayIcon.displayMessage("Vocalis", "Assistant is running", TrayIcon.MessageType.INFO);

        } catch (AWTException e) {
            e.printStackTrace();
        }
        // ← Removed IOException completely – no file loading removed
    }
}