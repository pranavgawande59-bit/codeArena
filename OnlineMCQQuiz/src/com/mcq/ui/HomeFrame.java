package com.mcq.ui;

import com.mcq.model.Student;
import com.mcq.model.StudentSession;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * HomeFrame.java
 * Main landing screen of the Online MCQ Quiz System.
 * Connects students to their personalized dashboard, quick quiz mode, and admin portal.
 */
public class HomeFrame extends JFrame {

    private JLabel lblSessionStatus;
    private JButton btnStudentPortal;

    public HomeFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("Online MCQ Quiz System");
        setSize(680, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(24, 43, 73)); // Deep navy blue
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 20, 20));
        headerPanel.setLayout(new BorderLayout(0, 8));

        JLabel lblTitle = new JLabel("ONLINE MCQ QUIZ SYSTEM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSubtitle = new JLabel(
            "<html><center>Interactive MCQ Assessment & Learning Platform with Topic Mastery,<br>"
            + "Difficulty Levels, Instant Explanations, and Student Performance Analytics</center></html>",
            SwingConstants.CENTER
        );
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(210, 225, 245));

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSubtitle, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Center Content Panel with Buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 247, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 40, 8, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Session status pill / label
        lblSessionStatus = new JLabel("", SwingConstants.CENTER);
        lblSessionStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        updateSessionLabel();
        gbc.gridy = 0;
        centerPanel.add(lblSessionStatus, gbc);

        // Button 1: Student Portal (Dashboard / Login / Register)
        btnStudentPortal = createStyledButton(
            StudentSession.isLoggedIn() ? "Student Dashboard & Analytics" : "Student Portal (Login / Register)",
            new Color(13, 110, 253),
            Color.WHITE
        );
        btnStudentPortal.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnStudentPortal.setPreferredSize(new Dimension(300, 46));
        gbc.gridy = 1;
        centerPanel.add(btnStudentPortal, gbc);

        // Button 2: Start Quick Quiz (Topic & Difficulty Selection)
        JButton btnStartQuiz = createStyledButton("Take a Practice Quiz", new Color(40, 167, 69), Color.WHITE);
        btnStartQuiz.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnStartQuiz.setPreferredSize(new Dimension(300, 46));
        gbc.gridy = 2;
        centerPanel.add(btnStartQuiz, gbc);

        // Button 3: Programming Problem Bank
        JButton btnProblemBank = createStyledButton("Programming Problem Bank", new Color(0, 128, 128), Color.WHITE);
        btnProblemBank.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnProblemBank.setPreferredSize(new Dimension(300, 46));
        gbc.gridy = 3;
        centerPanel.add(btnProblemBank, gbc);

        // Button 4: Admin Login
        JButton btnAdminLogin = createStyledButton("Admin Management Portal", new Color(111, 66, 193), Color.WHITE);
        btnAdminLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAdminLogin.setPreferredSize(new Dimension(300, 46));
        gbc.gridy = 4;
        centerPanel.add(btnAdminLogin, gbc);

        // Button 5: Exit
        JButton btnExit = createStyledButton("Exit", new Color(108, 117, 125), Color.WHITE);
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExit.setPreferredSize(new Dimension(300, 42));
        gbc.gridy = 5;
        centerPanel.add(btnExit, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(230, 235, 242));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblFooter = new JLabel("Online MCQ Quiz System | Java Swing + JDBC + MySQL", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblFooter.setForeground(new Color(100, 110, 120));
        footerPanel.add(lblFooter);
        add(footerPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---

        // Student Portal
        btnStudentPortal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (StudentSession.isLoggedIn()) {
                    new StudentDashboardFrame().setVisible(true);
                } else {
                    new StudentLoginFrame(HomeFrame.this).setVisible(true);
                }
                HomeFrame.this.setVisible(false);
            }
        });

        // Start Practice Quiz
        btnStartQuiz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LanguageFrame(HomeFrame.this).setVisible(true);
                HomeFrame.this.setVisible(false);
            }
        });

        // Problem Bank
        btnProblemBank.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProblemBankFrame(HomeFrame.this).setVisible(true);
                HomeFrame.this.setVisible(false);
            }
        });

        // Admin Login
        btnAdminLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminLoginFrame(HomeFrame.this).setVisible(true);
                HomeFrame.this.setVisible(false);
            }
        });

        // Exit
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                    HomeFrame.this,
                    "Are you sure you want to exit the application?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    private void updateSessionLabel() {
        if (StudentSession.isLoggedIn()) {
            Student s = StudentSession.getCurrentStudent();
            lblSessionStatus.setText("Signed in as: " + s.getFullName() + " (" + s.getUsername() + ")");
            lblSessionStatus.setForeground(new Color(40, 167, 69));
        } else {
            lblSessionStatus.setText("Guest Mode: Sign in to Student Portal to save analytics & scores");
            lblSessionStatus.setForeground(new Color(120, 130, 140));
        }
    }

    public static JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }
}
