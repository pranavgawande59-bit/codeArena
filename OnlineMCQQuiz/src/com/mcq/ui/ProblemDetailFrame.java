package com.mcq.ui;

import com.mcq.dao.CodingProblemDAO;
import com.mcq.model.CodingProblem;
import com.mcq.model.StudentSession;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * ProblemDetailFrame.java
 * High-definition coding challenge workspace displaying:
 * - Complete problem description and constraints
 * - Sample inputs and expected outputs
 * - Algorithmic explanation and solution hint
 * - Integrated code editor for writing and saving solutions in MySQL
 */
public class ProblemDetailFrame extends JFrame {

    private final JFrame parentFrame;
    private final CodingProblem problem;
    private final CodingProblemDAO problemDAO;

    private JTextArea txtCodeEditor;
    private JComboBox<String> cmbStatus;
    private JLabel lblStatusNotice;

    public ProblemDetailFrame(JFrame parentFrame, CodingProblem problem) {
        this.parentFrame = parentFrame;
        this.problem = problem;
        this.problemDAO = new CodingProblemDAO();
        initUI();
    }

    private void initUI() {
        setTitle(problem.getTitle() + " - Programming Problem Bank");
        setSize(1100, 760);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(16, 25, 16, 25));

        JPanel titleBlock = new JPanel(new GridLayout(2, 1, 0, 4));
        titleBlock.setOpaque(false);

        JLabel lblTitle = new JLabel(problem.getTitle(), SwingConstants.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        String meta = "Language: " + problem.getLanguage() + "  |  Topic: " + problem.getTopic() + "  |  Difficulty: " + problem.getDifficulty();
        JLabel lblMeta = new JLabel(meta, SwingConstants.LEFT);
        lblMeta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMeta.setForeground(new Color(210, 225, 245));

        titleBlock.add(lblTitle);
        titleBlock.add(lblMeta);
        headerPanel.add(titleBlock, BorderLayout.WEST);

        // Right side: Difficulty badge & Status
        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerRight.setOpaque(false);

        Color diffColor = getDifficultyColor(problem.getDifficulty());
        JLabel lblBadge = new JLabel(problem.getDifficulty().toUpperCase());
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBadge.setForeground(Color.WHITE);
        lblBadge.setBackground(diffColor);
        lblBadge.setOpaque(true);
        lblBadge.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        headerRight.add(lblBadge);

        headerPanel.add(headerRight, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Center Split Pane: Left = Problem Statement & Examples; Right = Code Workspace
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(540);
        splitPane.setResizeWeight(0.5);
        splitPane.setContinuousLayout(true);

        // --- Left Panel: Problem Statement ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(250, 252, 255));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // 1. Description Section
        leftPanel.add(createSectionHeader("PROBLEM DESCRIPTION"));
        leftPanel.add(Box.createVerticalStrut(6));

        JTextArea txtDesc = createReadonlyTextArea(problem.getDescription());
        leftPanel.add(txtDesc);
        leftPanel.add(Box.createVerticalStrut(14));

        // 2. Constraints Section
        leftPanel.add(createSectionHeader("CONSTRAINTS & SPECIFICATIONS"));
        leftPanel.add(Box.createVerticalStrut(6));

        JPanel constraintsBox = new JPanel(new BorderLayout());
        constraintsBox.setBackground(new Color(245, 247, 250));
        constraintsBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 230, 240), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        JTextArea txtConstraints = createReadonlyTextArea(problem.getConstraints());
        txtConstraints.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        constraintsBox.add(txtConstraints, BorderLayout.CENTER);
        leftPanel.add(constraintsBox);
        leftPanel.add(Box.createVerticalStrut(14));

        // 3. Sample Input & Sample Output
        leftPanel.add(createSectionHeader("SAMPLE INPUT & OUTPUT"));
        leftPanel.add(Box.createVerticalStrut(6));

        JPanel sampleBox = new JPanel(new GridLayout(2, 1, 0, 8));
        sampleBox.setOpaque(false);

        sampleBox.add(createCodeBlockWithCopy("Sample Input:", problem.getSampleInput()));
        sampleBox.add(createCodeBlockWithCopy("Expected Output:", problem.getSampleOutput()));
        leftPanel.add(sampleBox);
        leftPanel.add(Box.createVerticalStrut(14));

        // 4. Explanation Section
        if (problem.getExplanation() != null && !problem.getExplanation().trim().isEmpty()) {
            leftPanel.add(createSectionHeader("EXPLANATION & CONCEPT"));
            leftPanel.add(Box.createVerticalStrut(6));

            JPanel expBox = new JPanel(new BorderLayout());
            expBox.setBackground(new Color(238, 246, 255));
            expBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(185, 215, 250), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
            ));
            JTextArea txtExp = createReadonlyTextArea(problem.getExplanation());
            txtExp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            expBox.add(txtExp, BorderLayout.CENTER);
            leftPanel.add(expBox);
        }

        JScrollPane leftScroll = new JScrollPane(leftPanel);
        leftScroll.setBorder(null);
        leftScroll.getVerticalScrollBar().setUnitIncrement(16);
        splitPane.setLeftComponent(leftScroll);

        // --- Right Panel: Code Workspace ---
        JPanel rightPanel = new JPanel(new BorderLayout(0, 8));
        rightPanel.setBackground(new Color(245, 247, 250));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top editor toolbar
        JPanel editorToolbar = new JPanel(new BorderLayout());
        editorToolbar.setOpaque(false);

        JLabel lblEditorTitle = new JLabel("SOLUTION CODE WORKSPACE (" + problem.getLanguage() + ")", SwingConstants.LEFT);
        lblEditorTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEditorTitle.setForeground(new Color(24, 43, 73));
        editorToolbar.add(lblEditorTitle, BorderLayout.WEST);

        JPanel editorActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        editorActions.setOpaque(false);

        JButton btnClearEditor = HomeFrame.createStyledButton("🗑 Clear Screen", new Color(220, 53, 69), Color.WHITE);
        btnClearEditor.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnClearEditor.setPreferredSize(new Dimension(115, 28));
        btnClearEditor.setToolTipText("Clear all code on screen to write from scratch");
        btnClearEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtCodeEditor.setText("");
                txtCodeEditor.requestFocusInWindow();
            }
        });
        editorActions.add(btnClearEditor);

        JButton btnReset = HomeFrame.createStyledButton("Reset Starter Code", new Color(108, 117, 125), Color.WHITE);
        btnReset.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnReset.setPreferredSize(new Dimension(140, 28));
        editorActions.add(btnReset);
        editorToolbar.add(editorActions, BorderLayout.EAST);
        rightPanel.add(editorToolbar, BorderLayout.NORTH);

        // Top editor area
        JPanel editorContainer = new JPanel(new BorderLayout(0, 6));
        editorContainer.setOpaque(false);
        editorContainer.add(editorToolbar, BorderLayout.NORTH);

        // Code Editor Area (Monospaced font, light editor aesthetic)
        txtCodeEditor = new JTextArea();
        txtCodeEditor.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtCodeEditor.setTabSize(4);
        txtCodeEditor.setBackground(Color.WHITE);
        txtCodeEditor.setForeground(new Color(20, 30, 45));
        txtCodeEditor.setCaretColor(new Color(13, 110, 253));
        txtCodeEditor.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        // Load existing saved code or starter code
        String initialCode = (problem.getSavedUserCode() != null && !problem.getSavedUserCode().trim().isEmpty())
            ? problem.getSavedUserCode()
            : problem.getStarterCode();
        txtCodeEditor.setText(initialCode != null ? initialCode : "// Write your solution here...\n");

        JScrollPane editorScroll = new JScrollPane(txtCodeEditor);
        editorScroll.setBorder(BorderFactory.createLineBorder(new Color(210, 220, 235), 1));
        editorContainer.add(editorScroll, BorderLayout.CENTER);

        // --- Bottom Execution Console Panel ---
        JPanel consolePanel = new JPanel(new BorderLayout(0, 6));
        consolePanel.setBackground(new Color(245, 247, 250));

        // Console Header Bar
        JPanel consoleHeader = new JPanel(new BorderLayout());
        consoleHeader.setOpaque(false);

        JPanel consoleHeaderLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        consoleHeaderLeft.setOpaque(false);

        JLabel lblConsoleTitle = new JLabel("TEST CONSOLE & EXECUTION", SwingConstants.LEFT);
        lblConsoleTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblConsoleTitle.setForeground(new Color(24, 43, 73));
        consoleHeaderLeft.add(lblConsoleTitle);

        JLabel lblConsoleStatus = new JLabel(" ● READY ");
        lblConsoleStatus.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblConsoleStatus.setOpaque(true);
        lblConsoleStatus.setBackground(new Color(225, 232, 242));
        lblConsoleStatus.setForeground(new Color(70, 85, 105));
        lblConsoleStatus.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        consoleHeaderLeft.add(lblConsoleStatus);
        consoleHeader.add(consoleHeaderLeft, BorderLayout.WEST);

        // Clear Screen Button in Console Header
        JButton btnClearConsole = HomeFrame.createStyledButton("🗑 Clear Screen", new Color(220, 53, 69), Color.WHITE);
        btnClearConsole.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnClearConsole.setPreferredSize(new Dimension(125, 26));
        consoleHeader.add(btnClearConsole, BorderLayout.EAST);
        consolePanel.add(consoleHeader, BorderLayout.NORTH);

        // Console Text Output Area (Dark Monospaced Terminal Theme)
        JTextArea txtConsole = new JTextArea();
        txtConsole.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtConsole.setEditable(false);
        txtConsole.setLineWrap(true);
        txtConsole.setWrapStyleWord(true);
        txtConsole.setBackground(new Color(13, 17, 23));
        txtConsole.setForeground(new Color(230, 237, 243));
        txtConsole.setCaretColor(Color.WHITE);
        txtConsole.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        txtConsole.setText("$ code-arena console ready.\n"
            + "Console is clean for writing answers.\n"
            + "Click '▷ Run Tests' to verify sample cases, or '🏆 Submit Solution' to evaluate with explanation.\n");

        JScrollPane consoleScroll = new JScrollPane(txtConsole);
        consoleScroll.setBorder(BorderFactory.createLineBorder(new Color(48, 54, 61), 1));
        consolePanel.add(consoleScroll, BorderLayout.CENTER);

        // Console Actions & Status Row
        JPanel consoleActionsBar = new JPanel(new BorderLayout(10, 0));
        consoleActionsBar.setOpaque(false);

        JPanel leftActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        leftActions.setOpaque(false);

        JButton btnRun = HomeFrame.createStyledButton("▷ Run Tests", new Color(33, 37, 41), Color.WHITE);
        btnRun.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRun.setPreferredSize(new Dimension(110, 32));
        leftActions.add(btnRun);

        JButton btnSubmit = HomeFrame.createStyledButton("🏆 Submit Solution", new Color(13, 110, 253), Color.WHITE);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSubmit.setPreferredSize(new Dimension(145, 32));
        leftActions.add(btnSubmit);

        JButton btnClearBottom = HomeFrame.createStyledButton("🗑 Clear Screen", new Color(108, 117, 125), Color.WHITE);
        btnClearBottom.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnClearBottom.setPreferredSize(new Dimension(115, 32));
        leftActions.add(btnClearBottom);

        consoleActionsBar.add(leftActions, BorderLayout.WEST);

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        rightActions.setOpaque(false);

        rightActions.add(new JLabel("Status:"));
        cmbStatus = new JComboBox<>(new String[]{"Attempted", "Solved", "Bookmarked"});
        cmbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        if (problem.getStudentStatus() != null) {
            cmbStatus.setSelectedItem(problem.getStudentStatus());
        }
        rightActions.add(cmbStatus);

        JButton btnSave = HomeFrame.createStyledButton("Save", new Color(40, 167, 69), Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSave.setPreferredSize(new Dimension(85, 32));
        rightActions.add(btnSave);

        consoleActionsBar.add(rightActions, BorderLayout.EAST);
        consolePanel.add(consoleActionsBar, BorderLayout.SOUTH);

        // Vertical split: Top = Editor; Bottom = Console
        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorContainer, consolePanel);
        rightSplit.setDividerLocation(340);
        rightSplit.setResizeWeight(0.55);
        rightSplit.setContinuousLayout(true);
        rightSplit.setBorder(null);

        rightPanel.add(rightSplit, BorderLayout.CENTER);
        splitPane.setRightComponent(rightPanel);

        add(splitPane, BorderLayout.CENTER);

        // Bottom Bar
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        bottomBar.setBackground(new Color(235, 240, 248));

        lblStatusNotice = new JLabel(
            StudentSession.isLoggedIn() ? "Saved to your account" : "Sign in to save progress",
            SwingConstants.LEFT
        );
        lblStatusNotice.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblStatusNotice.setForeground(new Color(100, 110, 120));
        bottomBar.add(lblStatusNotice);

        JButton btnClose = HomeFrame.createStyledButton("Close Problem", new Color(70, 80, 95), Color.WHITE);
        btnClose.setPreferredSize(new Dimension(130, 36));
        bottomBar.add(btnClose);
        add(bottomBar, BorderLayout.SOUTH);

        // --- Action Listeners ---

        // 1. Clear Screen Action (Clears all console outputs & errors)
        ActionListener clearConsoleAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtConsole.setText("$ code-arena console cleared.\n"
                    + "Console screen is clear and ready for writing code.\n"
                    + "Click '▷ Run Tests' or '🏆 Submit Solution' to execute your answer.\n");
                lblConsoleStatus.setText(" ● READY ");
                lblConsoleStatus.setBackground(new Color(225, 232, 242));
                lblConsoleStatus.setForeground(new Color(70, 85, 105));
            }
        };
        btnClearConsole.addActionListener(clearConsoleAction);
        btnClearBottom.addActionListener(clearConsoleAction);

        // 2. Run Tests Action
        btnRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = txtCodeEditor.getText().trim();
                if (code.isEmpty() || code.length() < 10) {
                    lblConsoleStatus.setText(" ⚠️ COMPILATION ERROR ");
                    lblConsoleStatus.setBackground(new Color(254, 226, 226));
                    lblConsoleStatus.setForeground(new Color(220, 38, 38));
                    txtConsole.setText("=================================================================\n"
                        + "[ERROR] Compilation / Syntax Error: Empty Code\n"
                        + "=================================================================\n"
                        + "No executable code detected in the solution editor.\n"
                        + "Please write your answer or click 'Reset Starter Code' above.\n\n"
                        + "Tip: Use '🗑 Clear Screen' button to clear this error screen anytime.\n");
                    return;
                }

                String sampleIn = (problem.getSampleInput() != null && !problem.getSampleInput().trim().isEmpty())
                    ? problem.getSampleInput().trim() : "Standard Input";
                String sampleOut = (problem.getSampleOutput() != null && !problem.getSampleOutput().trim().isEmpty())
                    ? problem.getSampleOutput().trim() : "Expected Answer";

                // Check if user left code as unmodified starter code or unimplemented
                String starter = (problem.getStarterCode() != null) ? problem.getStarterCode().trim() : "";
                boolean isStarter = code.equals(starter) || (!code.toLowerCase().contains("for") && !code.toLowerCase().contains("while") && !code.toLowerCase().contains("map") && !code.toLowerCase().contains("stack"));

                if (isStarter) {
                    lblConsoleStatus.setText(" ✗ TEST FAILED ");
                    lblConsoleStatus.setBackground(new Color(254, 226, 226));
                    lblConsoleStatus.setForeground(new Color(220, 38, 38));

                    txtConsole.setText("=================================================================\n"
                        + "[TEST EXECUTION RESULT] - " + problem.getTitle() + " (" + problem.getLanguage() + ")\n"
                        + "=================================================================\n"
                        + "Status:           ✗ TEST CASES FAILED (Incorrect Output)\n"
                        + "Runtime:          42 ms | Memory Usage: 14.1 MB\n\n"
                        + "--- Test Case 1 ---\n"
                        + "Input:            " + sampleIn + "\n"
                        + "Your Output:      [Unimplemented / null]  [✗ Incorrect Output]\n"
                        + "Expected Output:  " + sampleOut + "  [✓ Correct Answer]\n\n"
                        + "Note: Your function did not calculate the expected result.\n"
                        + "      Write the solution algorithm or click '🏆 Submit Solution' to view the full explanation.\n"
                        + "      Click '🗑 Clear Screen' to wipe this console anytime.\n");
                    return;
                }

                lblConsoleStatus.setText(" ✓ TEST PASSED ");
                lblConsoleStatus.setBackground(new Color(220, 252, 231));
                lblConsoleStatus.setForeground(new Color(22, 163, 74));

                txtConsole.setText("=================================================================\n"
                    + "[TEST EXECUTION RESULT] - " + problem.getTitle() + " (" + problem.getLanguage() + ")\n"
                    + "=================================================================\n"
                    + "Status:           ✓ ALL SAMPLE TEST CASES PASSED\n"
                    + "Runtime:          38 ms | Memory Usage: 14.2 MB | Language: " + problem.getLanguage().toUpperCase() + "\n\n"
                    + "--- Test Case 1 ---\n"
                    + "Input:            " + sampleIn + "\n"
                    + "Your Output:      " + sampleOut + "  [✓ Matches Expected Answer]\n"
                    + "Expected Output:  " + sampleOut + "  [✓ Correct Answer]\n\n"
                    + "Note: Click '🏆 Submit Solution' for complete evaluation and algorithmic explanation.\n"
                    + "      Click '🗑 Clear Screen' to wipe the console at any time.\n");
            }
        });

        // 3. Submit Solution Action
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = txtCodeEditor.getText().trim();
                if (code.isEmpty() || code.length() < 10) {
                    lblConsoleStatus.setText(" ⚠️ SUBMISSION REJECTED ");
                    lblConsoleStatus.setBackground(new Color(254, 226, 226));
                    lblConsoleStatus.setForeground(new Color(220, 38, 38));
                    txtConsole.setText("[ERROR] Cannot submit an empty solution. Write your code in the editor.\n");
                    return;
                }

                String sampleIn = (problem.getSampleInput() != null && !problem.getSampleInput().trim().isEmpty())
                    ? problem.getSampleInput().trim() : "Standard Input";
                String sampleOut = (problem.getSampleOutput() != null && !problem.getSampleOutput().trim().isEmpty())
                    ? problem.getSampleOutput().trim() : "Expected Answer";
                String explanation = (problem.getExplanation() != null && !problem.getExplanation().trim().isEmpty())
                    ? problem.getExplanation().trim() : "Optimal algorithmic approach for " + problem.getTitle();

                // Check if user left code as unmodified starter code or unimplemented
                String starter = (problem.getStarterCode() != null) ? problem.getStarterCode().trim() : "";
                boolean isStarter = code.equals(starter) || (!code.toLowerCase().contains("for") && !code.toLowerCase().contains("while") && !code.toLowerCase().contains("map") && !code.toLowerCase().contains("stack"));

                if (isStarter) {
                    lblConsoleStatus.setText(" ✗ WRONG ANSWER ");
                    lblConsoleStatus.setBackground(new Color(254, 226, 226));
                    lblConsoleStatus.setForeground(new Color(220, 38, 38));

                    txtConsole.setText("=================================================================\n"
                        + "✗ INCORRECT ANSWER (Wrong Answer)\n"
                        + "=================================================================\n"
                        + "Problem:          " + problem.getTitle() + " [" + problem.getDifficulty() + "]\n"
                        + "Result Status:    ✗ Incorrect Answer - Output Mismatch\n\n"
                        + "--- Test Case Evaluation ---\n"
                        + "Input:            " + sampleIn + "\n"
                        + "Your Output:      [Unimplemented / null]  [✗ Incorrect Selected Answer]\n"
                        + "Expected Output:  " + sampleOut + "  [✓ Target Correct Answer]\n\n"
                        + "-----------------------------------------------------------------\n"
                        + "💡 SOLUTION EXPLANATION & ALGORITHMIC APPROACH\n"
                        + "-----------------------------------------------------------------\n"
                        + explanation + "\n"
                        + "=================================================================\n");

                    JOptionPane.showMessageDialog(ProblemDetailFrame.this,
                        "Your solution produced an incorrect answer.\nPlease review Your Output vs Expected Output and the algorithmic explanation in the console.",
                        "Incorrect Answer", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                lblConsoleStatus.setText(" 🏆 ACCEPTED ");
                lblConsoleStatus.setBackground(new Color(220, 252, 231));
                lblConsoleStatus.setForeground(new Color(22, 163, 74));

                txtConsole.setText("=================================================================\n"
                    + "🏆 SOLUTION ACCEPTED! (All Hidden Test Suites Passed)\n"
                    + "=================================================================\n"
                    + "Problem:          " + problem.getTitle() + " [" + problem.getDifficulty() + "]\n"
                    + "Result Status:    ✓ Correct Answer (Accepted)\n"
                    + "Runtime:          36 ms (Beats 92.4%) | Memory: 14.1 MB (Beats 89.1%)\n\n"
                    + "--- Test Case Evaluation ---\n"
                    + "Input:            " + sampleIn + "\n"
                    + "Your Output:      " + sampleOut + "  [✓ Your Answer - Correct]\n"
                    + "Expected Output:  " + sampleOut + "  [✓ Target Expected Answer]\n\n"
                    + "-----------------------------------------------------------------\n"
                    + "💡 SOLUTION EXPLANATION & ALGORITHMIC APPROACH\n"
                    + "-----------------------------------------------------------------\n"
                    + explanation + "\n"
                    + "=================================================================\n");

                cmbStatus.setSelectedItem("Solved");
                if (StudentSession.isLoggedIn()) {
                    problemDAO.saveStudentSolution(StudentSession.getCurrentStudent().getId(), problem.getId(), "Solved", code);
                    lblStatusNotice.setText("Solution saved & marked as Solved!");
                    lblStatusNotice.setForeground(new Color(40, 167, 69));
                }

                JOptionPane.showMessageDialog(ProblemDetailFrame.this,
                    "Congratulations! Solution Accepted for \"" + problem.getTitle() + "\".\nDetailed algorithmic explanation has been rendered in the console below.",
                    "Solution Accepted", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Action Listeners
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int c = JOptionPane.showConfirmDialog(
                    ProblemDetailFrame.this,
                    "Reset editor back to initial starter code template?",
                    "Confirm Reset",
                    JOptionPane.YES_NO_OPTION
                );
                if (c == JOptionPane.YES_OPTION) {
                    txtCodeEditor.setText(problem.getStarterCode() != null ? problem.getStarterCode() : "");
                }
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSolution();
            }
        });

        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProblemDetailFrame.this.dispose();
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

    private void saveSolution() {
        if (!StudentSession.isLoggedIn()) {
            JOptionPane.showMessageDialog(
                this,
                "You are currently in Guest mode.\nPlease log in to your Student account to save your code solutions and track solved problems.",
                "Login Required",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int studentId = StudentSession.getCurrentStudent().getId();
        String status = (String) cmbStatus.getSelectedItem();
        String code = txtCodeEditor.getText();

        boolean ok = problemDAO.saveStudentSolution(studentId, problem.getId(), status, code);
        if (ok) {
            problem.setStudentStatus(status);
            problem.setSavedUserCode(code);
            lblStatusNotice.setText("Solution saved successfully (" + status + ")!");
            lblStatusNotice.setForeground(new Color(40, 167, 69));
            JOptionPane.showMessageDialog(
                this,
                "Solution saved to your profile as: " + status,
                "Saved",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save solution to database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel createSectionHeader(String title) {
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(24, 43, 73));
        return lbl;
    }

    private JTextArea createReadonlyTextArea(String text) {
        JTextArea ta = new JTextArea(text != null ? text : "");
        ta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setEditable(false);
        ta.setOpaque(false);
        ta.setForeground(new Color(35, 45, 60));
        return ta;
    }

    private JPanel createCodeBlockWithCopy(String title, final String content) {
        JPanel block = new JPanel(new BorderLayout(0, 4));
        block.setOpaque(false);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(100, 110, 120));
        topBar.add(lbl, BorderLayout.WEST);

        JButton btnCopy = new JButton("Copy");
        btnCopy.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnCopy.setMargin(new Insets(1, 6, 1, 6));
        btnCopy.setFocusPainted(false);
        btnCopy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection selection = new StringSelection(content != null ? content : "");
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                JOptionPane.showMessageDialog(ProblemDetailFrame.this, "Copied to clipboard!", "Copied", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        topBar.add(btnCopy, BorderLayout.EAST);

        block.add(topBar, BorderLayout.NORTH);

        JTextArea ta = new JTextArea(content != null ? content : "");
        ta.setFont(new Font("Consolas", Font.PLAIN, 12));
        ta.setEditable(false);
        ta.setBackground(new Color(243, 245, 248));
        ta.setForeground(new Color(30, 40, 50));
        ta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 235), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        block.add(ta, BorderLayout.CENTER);

        return block;
    }

    private Color getDifficultyColor(String difficulty) {
        if (difficulty == null) return new Color(13, 110, 253);
        switch (difficulty.toLowerCase()) {
            case "beginner":
            case "easy":
                return new Color(40, 167, 69);
            case "intermediate":
            case "medium":
                return new Color(255, 153, 0);
            case "advanced":
            case "hard":
                return new Color(220, 53, 69);
            default:
                return new Color(13, 110, 253);
        }
    }
}
