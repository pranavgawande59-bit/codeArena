package com.mcq.ui;

import com.mcq.dao.StudentDAO;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * StudentLoginFrame.java
 * Secure student login screen allowing learners to authenticate and access
 * their personalized dashboard, attempt history, and performance analytics.
 */
public class StudentLoginFrame extends JFrame {

    private final JFrame parentFrame;
    private final StudentDAO studentDAO;

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public StudentLoginFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.studentDAO = new StudentDAO();
        initUI();
    }

    private void initUI() {
        setTitle("Student Login - Online MCQ Quiz");
        setSize(480, 430);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(22, 20, 20, 20));
        headerPanel.setLayout(new BorderLayout(0, 5));

        JLabel lblTitle = new JLabel("STUDENT PORTAL LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Sign in to view your dashboard, analytics, and attempt history", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(210, 225, 245));

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 247, 250));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 15, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblUser, gbc);

        gbc.gridx = 1;
        txtUsername = new JTextField(15);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setPreferredSize(new Dimension(200, 32));
        txtUsername.setText("student"); // Preload default demo student
        formPanel.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblPass, gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(15);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(200, 32));
        txtPassword.setText("student123"); // Preload default demo password
        formPanel.add(txtPassword, gbc);

        // Demo credential hint
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel lblHint = new JLabel("<html><center>Default Account: <b>student</b> / <b>student123</b></center></html>", SwingConstants.CENTER);
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblHint.setForeground(new Color(110, 120, 130));
        formPanel.add(lblHint, gbc);

        // Link to Register
        gbc.gridy = 3;
        JButton btnGoRegister = new JButton("<html><u>New Student? Click here to Register</u></html>");
        btnGoRegister.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnGoRegister.setForeground(new Color(13, 110, 253));
        btnGoRegister.setContentAreaFilled(false);
        btnGoRegister.setBorderPainted(false);
        btnGoRegister.setFocusPainted(false);
        btnGoRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(btnGoRegister, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Bottom Button Bar
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(new Color(235, 240, 248));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints bgbc = new GridBagConstraints();
        bgbc.insets = new Insets(0, 10, 0, 10);
        bgbc.gridy = 0;

        JButton btnLogin = HomeFrame.createStyledButton("Login", new Color(13, 110, 253), Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(130, 40));
        bgbc.gridx = 0;
        bottomPanel.add(btnLogin, bgbc);

        JButton btnBack = HomeFrame.createStyledButton("Back", new Color(108, 117, 125), Color.WHITE);
        btnBack.setPreferredSize(new Dimension(110, 40));
        bgbc.gridx = 1;
        bottomPanel.add(btnBack, bgbc);

        add(bottomPanel, BorderLayout.SOUTH);

        // Event Listeners
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        txtPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        btnGoRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StudentRegisterFrame(StudentLoginFrame.this).setVisible(true);
                StudentLoginFrame.this.setVisible(false);
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentLoginFrame.this.dispose();
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                }
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                }
            }
        });
    }

    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Required Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = studentDAO.authenticate(username, password);
        if (student != null) {
            StudentSession.login(student);
            StudentLoginFrame.this.dispose();
            new StudentDashboardFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid student username or password.\nPlease try again or register a new account.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
