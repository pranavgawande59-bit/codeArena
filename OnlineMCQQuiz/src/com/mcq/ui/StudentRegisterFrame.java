package com.mcq.ui;

import com.mcq.dao.StudentDAO;
import com.mcq.model.Student;
import com.mcq.model.StudentSession;
import java.awt.BorderLayout;
import java.awt.Color;
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
 * StudentRegisterFrame.java
 * Registration form for new students to create an account and unlock persistent analytics.
 */
public class StudentRegisterFrame extends JFrame {

    private final JFrame parentFrame;
    private final StudentDAO studentDAO;

    private JTextField txtFullName;
    private JTextField txtEmail;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;

    public StudentRegisterFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.studentDAO = new StudentDAO();
        initUI();
    }

    private void initUI() {
        setTitle("Student Registration - Online MCQ Quiz");
        setSize(520, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(22, 20, 20, 20));
        headerPanel.setLayout(new BorderLayout(0, 5));

        JLabel lblTitle = new JLabel("CREATE STUDENT ACCOUNT", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Join the Online MCQ Quiz System to track progress and analytics", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(210, 225, 245));

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 247, 250));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Full Name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblName = new JLabel("Full Name:");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblName, gbc);

        gbc.gridx = 1;
        txtFullName = new JTextField(16);
        txtFullName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtFullName.setPreferredSize(new Dimension(240, 32));
        formPanel.add(txtFullName, gbc);

        // 2. Email Address
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblEmail = new JLabel("Email Address:");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblEmail, gbc);

        gbc.gridx = 1;
        txtEmail = new JTextField(16);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setPreferredSize(new Dimension(240, 32));
        formPanel.add(txtEmail, gbc);

        // 3. Username
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblUser, gbc);

        gbc.gridx = 1;
        txtUsername = new JTextField(16);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setPreferredSize(new Dimension(240, 32));
        formPanel.add(txtUsername, gbc);

        // 4. Password
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblPass, gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(16);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(240, 32));
        formPanel.add(txtPassword, gbc);

        // 5. Confirm Password
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblConfirm = new JLabel("Confirm Password:");
        lblConfirm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblConfirm, gbc);

        gbc.gridx = 1;
        txtConfirmPassword = new JPasswordField(16);
        txtConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtConfirmPassword.setPreferredSize(new Dimension(240, 32));
        formPanel.add(txtConfirmPassword, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Bottom Button Bar
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(new Color(235, 240, 248));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints bgbc = new GridBagConstraints();
        bgbc.insets = new Insets(0, 10, 0, 10);
        bgbc.gridy = 0;

        JButton btnRegister = HomeFrame.createStyledButton("Register & Sign In", new Color(40, 167, 69), Color.WHITE);
        btnRegister.setPreferredSize(new Dimension(170, 40));
        bgbc.gridx = 0;
        bottomPanel.add(btnRegister, bgbc);

        JButton btnBack = HomeFrame.createStyledButton("Back to Login", new Color(108, 117, 125), Color.WHITE);
        btnBack.setPreferredSize(new Dimension(140, 40));
        bgbc.gridx = 1;
        bottomPanel.add(btnBack, bgbc);

        add(bottomPanel, BorderLayout.SOUTH);

        // Event Listeners
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegistration();
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentRegisterFrame.this.dispose();
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

    private void performRegistration() {
        String fullName = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String confirmPass = new String(txtConfirmPassword.getPassword()).trim();

        if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters long.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (studentDAO.isUsernameTaken(username)) {
            JOptionPane.showMessageDialog(this, "Username '" + username + "' is already taken. Please choose another.", "Username Conflict", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (studentDAO.isEmailTaken(email)) {
            JOptionPane.showMessageDialog(this, "Email address is already registered. Please login.", "Email Conflict", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = new Student(username, password, fullName, email);
        boolean success = studentDAO.registerStudent(student);

        if (success) {
            JOptionPane.showMessageDialog(
                this,
                "Account created successfully!\nWelcome, " + fullName + "!",
                "Registration Success",
                JOptionPane.INFORMATION_MESSAGE
            );
            StudentSession.login(student);
            StudentRegisterFrame.this.dispose();
            new StudentDashboardFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Failed to register student account. Please verify database connection.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
