package com.mcq.ui.chart;

import com.mcq.dao.QuizAttemptDAO.TopicStat;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import javax.swing.JPanel;

/**
 * BarChartPanel.java
 * High-performance Java2D horizontal bar chart visualizing topic accuracy percentages.
 * Color-codes performance tiers (Green for mastery >=80%, Blue for 60-79%, Amber for 40-59%, Red for <40%).
 */
public class BarChartPanel extends JPanel {

    private List<TopicStat> topicStats;
    private final String chartTitle;

    public BarChartPanel(String chartTitle, List<TopicStat> topicStats) {
        this.chartTitle = chartTitle;
        this.topicStats = topicStats;
        setBackground(Color.WHITE);
    }

    public void setData(List<TopicStat> topicStats) {
        this.topicStats = topicStats;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Chart Title
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.setColor(new Color(33, 43, 54));
        g2.drawString(chartTitle, 20, 24);

        if (topicStats == null || topicStats.isEmpty()) {
            g2.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            g2.setColor(new Color(130, 140, 150));
            String emptyMsg = "No topic data available yet. Complete topic-based quizzes to generate analytics!";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(emptyMsg, (width - fm.stringWidth(emptyMsg)) / 2, height / 2);
            g2.dispose();
            return;
        }

        int labelAreaWidth = 140;
        int paddingRight = 80;
        int paddingTop = 45;
        int paddingBottom = 25;

        int availableWidth = width - labelAreaWidth - paddingRight;
        int availableHeight = height - paddingTop - paddingBottom;

        if (availableWidth <= 20 || availableHeight <= 20) {
            g2.dispose();
            return;
        }

        int rowCount = Math.min(topicStats.size(), 7); // Show top 7 topics
        int barHeight = Math.min(22, (availableHeight / rowCount) - 10);
        int rowGap = (availableHeight - (rowCount * barHeight)) / (rowCount + 1);

        for (int i = 0; i < rowCount; i++) {
            TopicStat stat = topicStats.get(i);
            int y = paddingTop + rowGap + i * (barHeight + rowGap);

            // Topic Name Label
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.setColor(new Color(40, 50, 65));
            String topicName = stat.getTopic();
            if (topicName.length() > 16) {
                topicName = topicName.substring(0, 14) + "..";
            }
            FontMetrics lfm = g2.getFontMetrics();
            g2.drawString(topicName, labelAreaWidth - lfm.stringWidth(topicName) - 12, y + barHeight - 5);

            // Background track bar (light grey)
            g2.setColor(new Color(240, 243, 248));
            g2.fillRoundRect(labelAreaWidth, y, availableWidth, barHeight, 6, 6);

            // Colored Progress Bar
            double pct = Math.max(0, Math.min(100, stat.getAveragePercentage()));
            int barPixelWidth = (int) ((pct / 100.0) * availableWidth);

            Color barColor;
            if (pct >= 80.0) {
                barColor = new Color(40, 167, 69); // Green
            } else if (pct >= 60.0) {
                barColor = new Color(13, 110, 253); // Blue
            } else if (pct >= 40.0) {
                barColor = new Color(255, 153, 0); // Amber
            } else {
                barColor = new Color(220, 53, 69); // Red
            }

            g2.setColor(barColor);
            g2.fillRoundRect(labelAreaWidth, y, Math.max(barPixelWidth, 4), barHeight, 6, 6);

            // Percentage & attempt count label next to bar
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.setColor(new Color(30, 40, 55));
            String valLabel = String.format("%.0f%% (%d)", stat.getAveragePercentage(), stat.getTotalAttempts());
            g2.drawString(valLabel, labelAreaWidth + barPixelWidth + 8, y + barHeight - 5);
        }

        g2.dispose();
    }
}
