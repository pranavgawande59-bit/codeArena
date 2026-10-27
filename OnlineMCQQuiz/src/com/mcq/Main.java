package com.mcq;

import com.mcq.database.DBConnection;
import com.mcq.ui.HomeFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main.java
 * Application entry point for the Online MCQ Quiz System.
 * Initializes the Swing look-and-feel and launches the main HomeFrame.
 */
public class Main {

    public static void main(String[] args) {
        // Set native platform Look and Feel for clean, modern desktop aesthetics
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("[Main] System Look & Feel could not be initialized; using default.");
        }

        // Diagnostic check for MySQL database connection in background
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean connected = DBConnection.testConnection();
                if (connected) {
                    System.out.println("[Database Status] Successfully connected to MySQL database: online_mcq_quiz");
                } else {
                    System.out.println("[Database Status Notice] Could not connect to MySQL with current credentials in DBConnection.java.");
                    System.out.println("  -> Please verify MySQL is running and password matches in DBConnection.java.");
                }
            }
        }).start();

        // Launch UI on Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                HomeFrame homeFrame = new HomeFrame();
                homeFrame.setVisible(true);
            }
        });
    }
}
