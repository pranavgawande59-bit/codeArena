package com.mcq.model;

/**
 * AttemptAnswer.java
 * Records the student's selected answer for a specific question in a quiz attempt,
 * including correctness and marks earned.
 */
public class AttemptAnswer {
    private int id;
    private int attemptId;
    private int questionId;
    private String selectedOption; // "A", "B", "C", "D", or null if skipped
    private boolean isCorrect;
    private int marksObtained;

    // Optional Question reference for full details during Quiz Review
    private Question question;

    public AttemptAnswer() {
    }

    public AttemptAnswer(int attemptId, int questionId, String selectedOption, boolean isCorrect, int marksObtained) {
        this.attemptId = attemptId;
        this.questionId = questionId;
        this.selectedOption = selectedOption;
        this.isCorrect = isCorrect;
        this.marksObtained = marksObtained;
    }

    public AttemptAnswer(int id, int attemptId, int questionId, String selectedOption, boolean isCorrect, int marksObtained) {
        this.id = id;
        this.attemptId = attemptId;
        this.questionId = questionId;
        this.selectedOption = selectedOption;
        this.isCorrect = isCorrect;
        this.marksObtained = marksObtained;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(int attemptId) {
        this.attemptId = attemptId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public int getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(int marksObtained) {
        this.marksObtained = marksObtained;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
