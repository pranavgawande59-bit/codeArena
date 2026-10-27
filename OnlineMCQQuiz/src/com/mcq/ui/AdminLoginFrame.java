package com.mcq.ui;

import com.mcq.dao.AdminDAO;
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
 * AdminLoginFrame.java
 * Secure authentication portal for administrative access.
 */
public class AdminLoginFrame extends JFrame {

    private final JFrame parentFrame;
    private final AdminDAO adminDAO;

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public AdminLoginFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.adminDAO = new AdminDAO();
        initUI();
    }

    private void initUI() {
        setTitle("Admin Login - Online MCQ Quiz");
        setSize(520, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 20, 20));
        headerPanel.setLayout(new BorderLayout(0, 6));

        JLabel lblTitle = new JLabel("ADMINISTRATOR LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Please enter your admin credentials to continue", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(210, 225, 245));

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Center Form Panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 247, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Label & Field
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(lblUser, gbc);

        txtUsername = new JTextField(18);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setPreferredSize(new Dimension(240, 36));
        gbc.gridx = 1;
        gbc.gridy = 0;
        centerPanel.add(txtUsername, gbc);

        // Password Label & Field
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(lblPass, gbc);

        txtPassword = new JPasswordField(18);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(240, 36));
        gbc.gridx = 1;
        gbc.gridy = 1;
        centerPanel.add(txtPassword, gbc);

        // Button Panel
        JPanel btnPanel = new JPanel(new GridBagLayout());
        btnPanel.setOpaque(false);
        GridBagConstraints bgbc = new GridBagConstraints();
        bgbc.insets = new Insets(10, 10, 0, 10);
        bgbc.gridy = 0;

        JButton btnLogin = HomeFrame.createStyledButton("Login", new Color(40, 167, 69), Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(120, 40));
        bgbc.gridx = 0;
        btnPanel.add(btnLogin, bgbc);

        JButton btnBack = HomeFrame.createStyledButton("Back", new Color(108, 117, 125), Color.WHITE);
        btnBack.setPreferredSize(new Dimension(120, 40));
        bgbc.gridx = 1;
        btnPanel.add(btnBack, bgbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        centerPanel.add(btnPanel, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Action Listener: Login
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Allow pressing Enter key in password field to trigger login
        txtPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Action Listener: Back
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminLoginFrame.this.dispose();
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                }
            }
        });

        // Window closing restore parent
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

        // Validation
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }

        // Database validation via AdminDAO
        boolean isValid = adminDAO.validateAdmin(username, password);

        if (isValid) {
            JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + username + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
            AdminDashboardFrame dashboard = new AdminDashboardFrame();
            dashboard.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Invalid username or password!\nPlease try again.\n(Default: admin / admin123)",
                "Authentication Failed",
                JOptionPane.ERROR_MESSAGE
            );
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }
}
