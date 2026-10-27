package com.mcq.ui;

import com.mcq.dao.CodingProblemDAO;
import com.mcq.model.CodingProblem;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * AddEditProblemDialog.java
 * Dialog for creating or modifying a complete coding problem in the Problem Bank.
 */
public class AddEditProblemDialog extends JDialog {

    private final CodingProblemDAO problemDAO;
    private CodingProblem problem;
    private boolean saved = false;

    // Form inputs
    private JTextField txtTitle;
    private JComboBox<String> cmbLanguage;
    private JComboBox<String> cmbTopic;
    private JComboBox<String> cmbDifficulty;
    private JTextArea txtDescription;
    private JTextArea txtConstraints;
    private JTextArea txtSampleInput;
    private JTextArea txtSampleOutput;
    private JTextArea txtExplanation;
    private JTextArea txtStarterCode;

    public AddEditProblemDialog(JFrame parent, CodingProblem problem) {
        super(parent, (problem == null ? "Add New Coding Problem" : "Edit Coding Problem"), true);
        this.problemDAO = new CodingProblemDAO();
        this.problem = problem;
        initUI();
        if (problem != null) {
            populateFields();
        }
    }

    private void initUI() {
        setSize(780, 750);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout(0, 4));
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel((problem == null ? "ADD CODING PROBLEM" : "EDIT CODING PROBLEM"), SwingConstants.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Specify title, language, topic, difficulty, description, constraints, and test I/O", SwingConstants.LEFT);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(210, 225, 245));

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Center Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 247, 250));
        formPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Title
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Problem Title:"), gbc);
        gbc.gridx = 1;
        txtTitle = new JTextField();
        txtTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTitle.setPreferredSize(new Dimension(480, 30));
        formPanel.add(txtTitle, gbc);

        // 2. Language & Difficulty (Row)
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Lang & Difficulty:"), gbc);
        gbc.gridx = 1;
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row2.setOpaque(false);

        List<String> langs = problemDAO.getAvailableLanguages();
        cmbLanguage = new JComboBox<>(langs.toArray(new String[0]));
        cmbLanguage.setEditable(true);
        cmbLanguage.setPreferredSize(new Dimension(140, 30));
        row2.add(cmbLanguage);

        row2.add(new JLabel("Topic:"));
        List<String> topics = problemDAO.getAvailableTopics();
        cmbTopic = new JComboBox<>(topics.toArray(new String[0]));
        cmbTopic.setEditable(true);
        cmbTopic.setPreferredSize(new Dimension(140, 30));
        row2.add(cmbTopic);

        row2.add(new JLabel("Level:"));
        cmbDifficulty = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advanced"});
        cmbDifficulty.setPreferredSize(new Dimension(120, 30));
        row2.add(cmbDifficulty);

        formPanel.add(row2, gbc);

        // 3. Description
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        txtDescription = createTextArea(4);
        formPanel.add(new JScrollPane(txtDescription), gbc);

        // 4. Constraints
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Constraints:"), gbc);
        gbc.gridx = 1;
        txtConstraints = createTextArea(2);
        formPanel.add(new JScrollPane(txtConstraints), gbc);

        // 5. Sample Input
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Sample Input:"), gbc);
        gbc.gridx = 1;
        txtSampleInput = createTextArea(2);
        formPanel.add(new JScrollPane(txtSampleInput), gbc);

        // 6. Sample Output
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Sample Output:"), gbc);
        gbc.gridx = 1;
        txtSampleOutput = createTextArea(2);
        formPanel.add(new JScrollPane(txtSampleOutput), gbc);

        // 7. Explanation
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Explanation:"), gbc);
        gbc.gridx = 1;
        txtExplanation = createTextArea(2);
        formPanel.add(new JScrollPane(txtExplanation), gbc);

        // 8. Starter Code
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Starter Code:"), gbc);
        gbc.gridx = 1;
        txtStarterCode = createTextArea(4);
        txtStarterCode.setFont(new Font("Consolas", Font.PLAIN, 12));
        formPanel.add(new JScrollPane(txtStarterCode), gbc);

        JScrollPane mainScroll = new JScrollPane(formPanel);
        mainScroll.setBorder(null);
        add(mainScroll, BorderLayout.CENTER);

        // Bottom buttons
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        bottomBar.setBackground(new Color(235, 240, 248));

        JButton btnSave = HomeFrame.createStyledButton("Save Problem", new Color(40, 167, 69), Color.WHITE);
        btnSave.setPreferredSize(new Dimension(130, 36));
        bottomBar.add(btnSave);

        JButton btnCancel = HomeFrame.createStyledButton("Cancel", new Color(108, 117, 125), Color.WHITE);
        btnCancel.setPreferredSize(new Dimension(100, 36));
        bottomBar.add(btnCancel);

        add(bottomBar, BorderLayout.SOUTH);

        // Listeners
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSave();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private JTextArea createTextArea(int rows) {
        JTextArea ta = new JTextArea(rows, 25);
        ta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        return ta;
    }

    private void populateFields() {
        txtTitle.setText(problem.getTitle());
        cmbLanguage.setSelectedItem(problem.getLanguage());
        cmbTopic.setSelectedItem(problem.getTopic());
        cmbDifficulty.setSelectedItem(problem.getDifficulty());
        txtDescription.setText(problem.getDescription());
        txtConstraints.setText(problem.getConstraints());
        txtSampleInput.setText(problem.getSampleInput());
        txtSampleOutput.setText(problem.getSampleOutput());
        txtExplanation.setText(problem.getExplanation());
        txtStarterCode.setText(problem.getStarterCode());
    }

    private void performSave() {
        String title = txtTitle.getText().trim();
        Object langObj = cmbLanguage.getSelectedItem();
        String language = (langObj != null) ? langObj.toString().trim() : "Java";
        Object topObj = cmbTopic.getSelectedItem();
        String topic = (topObj != null) ? topObj.toString().trim() : "General";
        String difficulty = (String) cmbDifficulty.getSelectedItem();

        String desc = txtDescription.getText().trim();
        String constraints = txtConstraints.getText().trim();
        String sampleIn = txtSampleInput.getText().trim();
        String sampleOut = txtSampleOutput.getText().trim();
        String explanation = txtExplanation.getText().trim();
        String starter = txtStarterCode.getText().trim();

        if (title.isEmpty() || desc.isEmpty() || sampleIn.isEmpty() || sampleOut.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title, Description, Sample Input, and Sample Output are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (problem == null) {
            problem = new CodingProblem(title, language, topic, difficulty, desc, constraints, sampleIn, sampleOut, explanation, starter);
            boolean ok = problemDAO.addProblem(problem);
            if (ok) {
                saved = true;
                JOptionPane.showMessageDialog(this, "Problem successfully added to bank!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to insert problem into database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            problem.setTitle(title);
            problem.setLanguage(language);
            problem.setTopic(topic);
            problem.setDifficulty(difficulty);
            problem.setDescription(desc);
            problem.setConstraints(constraints);
            problem.setSampleInput(sampleIn);
            problem.setSampleOutput(sampleOut);
            problem.setExplanation(explanation);
            problem.setStarterCode(starter);

            boolean ok = problemDAO.updateProblem(problem);
            if (ok) {
                saved = true;
                JOptionPane.showMessageDialog(this, "Problem updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update problem.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean isSaved() {
        return saved;
    }
}
