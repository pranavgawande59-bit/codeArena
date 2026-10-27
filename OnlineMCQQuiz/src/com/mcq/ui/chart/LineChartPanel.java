package com.mcq.ui.chart;

import com.mcq.dao.QuizAttemptDAO.ScoreTrendPoint;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.util.List;
import javax.swing.JPanel;

/**
 * LineChartPanel.java
 * High-performance Java2D custom chart rendering student quiz score trends over time.
 * Features anti-aliased curves, gradient area fill, data point markers, and gridlines.
 */
public class LineChartPanel extends JPanel {

    private List<ScoreTrendPoint> dataPoints;
    private final String chartTitle;

    public LineChartPanel(String chartTitle, List<ScoreTrendPoint> dataPoints) {
        this.chartTitle = chartTitle;
        this.dataPoints = dataPoints;
        setBackground(Color.WHITE);
    }

    public void setData(List<ScoreTrendPoint> dataPoints) {
        this.dataPoints = dataPoints;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable high-quality anti-aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Title
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.setColor(new Color(33, 43, 54));
        g2.drawString(chartTitle, 20, 24);

        if (dataPoints == null || dataPoints.isEmpty()) {
            g2.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            g2.setColor(new Color(130, 140, 150));
            String emptyMsg = "No quiz attempts recorded yet. Take a quiz to view your progress trend!";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(emptyMsg, (width - fm.stringWidth(emptyMsg)) / 2, height / 2);
            g2.dispose();
            return;
        }

        int paddingLeft = 50;
        int paddingRight = 30;
        int paddingTop = 45;
        int paddingBottom = 40;

        int plotWidth = width - paddingLeft - paddingRight;
        int plotHeight = height - paddingTop - paddingBottom;

        if (plotWidth <= 10 || plotHeight <= 10) {
            g2.dispose();
            return;
        }

        // Horizontal Gridlines & Y-Axis Labels (0%, 25%, 50%, 75%, 100%)
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        int[] ySteps = {0, 25, 50, 75, 100};
        for (int pct : ySteps) {
            int y = paddingTop + plotHeight - (int) ((pct / 100.0) * plotHeight);

            g2.setColor(new Color(230, 235, 245));
            g2.drawLine(paddingLeft, y, paddingLeft + plotWidth, y);

            g2.setColor(new Color(120, 130, 145));
            String label = pct + "%";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, paddingLeft - fm.stringWidth(label) - 8, y + 4);
        }

        // Calculate (x, y) coordinates for each data point
        int n = dataPoints.size();
        int[] xCoords = new int[n];
        int[] yCoords = new int[n];

        for (int i = 0; i < n; i++) {
            if (n == 1) {
                xCoords[i] = paddingLeft + (plotWidth / 2);
            } else {
                xCoords[i] = paddingLeft + (int) ((double) i / (n - 1) * plotWidth);
            }
            double pct = Math.max(0, Math.min(100, dataPoints.get(i).getPercentage()));
            yCoords[i] = paddingTop + plotHeight - (int) ((pct / 100.0) * plotHeight);
        }

        // Draw Soft Gradient Fill Under Polyline
        if (n > 1) {
            GeneralPath areaPath = new GeneralPath();
            areaPath.moveTo(xCoords[0], paddingTop + plotHeight);
            for (int i = 0; i < n; i++) {
                areaPath.lineTo(xCoords[i], yCoords[i]);
            }
            areaPath.lineTo(xCoords[n - 1], paddingTop + plotHeight);
            areaPath.closePath();

            GradientPaint gp = new GradientPaint(
                0, paddingTop, new Color(13, 110, 253, 70),
                0, paddingTop + plotHeight, new Color(13, 110, 253, 5)
            );
            g2.setPaint(gp);
            g2.fill(areaPath);
        }

        // Draw Line connecting points
        g2.setColor(new Color(13, 110, 253));
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < n - 1; i++) {
            g2.drawLine(xCoords[i], yCoords[i], xCoords[i + 1], yCoords[i + 1]);
        }

        // Draw Data Points, Tooltip Values, and X-Axis Labels
        for (int i = 0; i < n; i++) {
            ScoreTrendPoint pt = dataPoints.get(i);
            int cx = xCoords[i];
            int cy = yCoords[i];

            // Outer ring
            g2.setColor(Color.WHITE);
            g2.fillOval(cx - 6, cy - 6, 12, 12);

            // Inner circle
            g2.setColor(new Color(13, 110, 253));
            g2.fillOval(cx - 4, cy - 4, 8, 8);

            // Value text above dot
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.setColor(new Color(24, 43, 73));
            String valStr = String.format("%.0f%%", pt.getPercentage());
            FontMetrics vfm = g2.getFontMetrics();
            g2.drawString(valStr, cx - (vfm.stringWidth(valStr) / 2), cy - 9);

            // X-Axis date/attempt label
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2.setColor(new Color(100, 110, 120));
            String xLabel = pt.getDateLabel();
            FontMetrics xfm = g2.getFontMetrics();
            g2.drawString(xLabel, cx - (xfm.stringWidth(xLabel) / 2), paddingTop + plotHeight + 18);
        }

        g2.dispose();
    }
}
