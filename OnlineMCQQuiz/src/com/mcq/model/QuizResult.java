package com.mcq.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * QuizResult.java
 * Model class storing the outcome of a student's completed quiz attempt,
 * including questions and student choices for review and persistent tracking.
 */
public class QuizResult {
    private String language;
    private String topic;
    private String difficulty;
    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;
    private int skipped;
    private int score;
    private int totalMarks;
    private double percentage;
    private String feedbackMessage;

    // References for in-memory review and database synchronization
    private List<Question> questionList;
    private Map<Integer, String> selectedAnswers;
    private int savedAttemptId = -1;

    public QuizResult(String language, int totalQuestions, int correctAnswers, int wrongAnswers, int skipped) {
        this(language, "All", "All", totalQuestions, correctAnswers, wrongAnswers, skipped, correctAnswers, totalQuestions, null, null);
    }

    public QuizResult(String language, String topic, String difficulty,
                      int totalQuestions, int correctAnswers, int wrongAnswers, int skipped,
                      int score, int totalMarks,
                      List<Question> questionList, Map<Integer, String> selectedAnswers) {
        this.language = language;
        this.topic = (topic != null && !topic.trim().isEmpty()) ? topic : "All";
        this.difficulty = (difficulty != null && !difficulty.trim().isEmpty()) ? difficulty : "All";
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.skipped = skipped;
        this.score = score;
        this.totalMarks = (totalMarks > 0) ? totalMarks : Math.max(totalQuestions, 1);
        this.questionList = questionList;
        this.selectedAnswers = selectedAnswers;

        // Calculate percentage: (Score / Total Marks) * 100
        if (this.totalMarks > 0) {
            this.percentage = ((double) this.score / this.totalMarks) * 100.0;
        } else {
            this.percentage = 0.0;
        }

        // Determine performance feedback message:
        // 80–100% -> Excellent!
        // 60–79%  -> Good!
        // 40–59%  -> Keep Practicing!
        // Below 40% -> Need More Practice!
        if (this.percentage >= 80.0) {
            this.feedbackMessage = "Excellent!";
        } else if (this.percentage >= 60.0) {
            this.feedbackMessage = "Good!";
        } else if (this.percentage >= 40.0) {
            this.feedbackMessage = "Keep Practicing!";
        } else {
            this.feedbackMessage = "Need More Practice!";
        }
    }

    public String getLanguage() {
        return language;
    }

    public String getTopic() {
        return topic;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public int getSkipped() {
        return skipped;
    }

    public int getScore() {
        return score;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getFeedbackMessage() {
        return feedbackMessage;
    }

    public List<Question> getQuestionList() {
        return (questionList != null) ? questionList : Collections.emptyList();
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public Map<Integer, String> getSelectedAnswers() {
        return (selectedAnswers != null) ? selectedAnswers : Collections.emptyMap();
    }

    public void setSelectedAnswers(Map<Integer, String> selectedAnswers) {
        this.selectedAnswers = selectedAnswers;
    }

    public int getSavedAttemptId() {
        return savedAttemptId;
    }

    public void setSavedAttemptId(int savedAttemptId) {
        this.savedAttemptId = savedAttemptId;
    }

    @Override
    public String toString() {
        return "QuizResult [language=" + language + ", topic=" + topic + ", difficulty=" + difficulty 
                + ", score=" + score + "/" + totalMarks + ", percentage=" + String.format("%.1f", percentage) 
                + "%, feedback=" + feedbackMessage + "]";
    }
}
