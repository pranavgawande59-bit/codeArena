package com.mcq.model;

import java.sql.Timestamp;

/**
 * QuizAttempt.java
 * Model class representing a completed quiz session by a student.
 */
public class QuizAttempt {
    private int id;
    private int studentId;
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
    private Timestamp attemptDate;

    public QuizAttempt() {
    }

    public QuizAttempt(int studentId, String language, String topic, String difficulty,
                       int totalQuestions, int correctAnswers, int wrongAnswers, int skipped,
                       int score, int totalMarks, double percentage, String feedbackMessage) {
        this.studentId = studentId;
        this.language = language;
        this.topic = topic;
        this.difficulty = difficulty;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.skipped = skipped;
        this.score = score;
        this.totalMarks = totalMarks;
        this.percentage = percentage;
        this.feedbackMessage = feedbackMessage;
    }

    public QuizAttempt(int id, int studentId, String language, String topic, String difficulty,
                       int totalQuestions, int correctAnswers, int wrongAnswers, int skipped,
                       int score, int totalMarks, double percentage, String feedbackMessage, Timestamp attemptDate) {
        this(studentId, language, topic, difficulty, totalQuestions, correctAnswers, wrongAnswers, skipped, score, totalMarks, percentage, feedbackMessage);
        this.id = id;
        this.attemptDate = attemptDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(int wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getFeedbackMessage() {
        return feedbackMessage;
    }

    public void setFeedbackMessage(String feedbackMessage) {
        this.feedbackMessage = feedbackMessage;
    }

    public Timestamp getAttemptDate() {
        return attemptDate;
    }

    public void setAttemptDate(Timestamp attemptDate) {
        this.attemptDate = attemptDate;
    }
}
