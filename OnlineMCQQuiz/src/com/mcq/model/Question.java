package com.mcq.model;

/**
 * Question.java
 * Model class representing a Multiple Choice Question (MCQ)
 * with topic classification, difficulty level, marks weightage, and detailed explanation.
 */
public class Question {
    private int id;
    private String language;
    private String topic;
    private String difficulty; // "Beginner", "Intermediate", "Advanced"
    private int marks;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer; // "A", "B", "C", or "D"
    private String explanation;

    public Question() {
        this.topic = "General";
        this.difficulty = "Beginner";
        this.marks = 1;
        this.explanation = "";
    }

    /**
     * Constructor without id (used when inserting a new question into database)
     */
    public Question(String language, String topic, String difficulty, int marks,
                    String question, String optionA, String optionB, String optionC, String optionD,
                    String correctAnswer, String explanation) {
        this.language = language;
        this.topic = (topic != null && !topic.trim().isEmpty()) ? topic.trim() : "General";
        this.difficulty = (difficulty != null && !difficulty.trim().isEmpty()) ? difficulty.trim() : "Beginner";
        this.marks = (marks > 0) ? marks : 1;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.explanation = (explanation != null) ? explanation.trim() : "";
    }

    /**
     * Legacy constructor without topic/difficulty/marks/explanation for backwards compatibility
     */
    public Question(String language, String question, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        this(language, "General", "Beginner", 1, question, optionA, optionB, optionC, optionD, correctAnswer, "");
    }

    /**
     * Legacy constructor with id
     */
    public Question(int id, String language, String question, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        this(language, "General", "Beginner", 1, question, optionA, optionB, optionC, optionD, correctAnswer, "");
        this.id = id;
    }

    /**
     * Full constructor with id and all metadata
     */
    public Question(int id, String language, String topic, String difficulty, int marks,
                    String question, String optionA, String optionB, String optionC, String optionD,
                    String correctAnswer, String explanation) {
        this(language, topic, difficulty, marks, question, optionA, optionB, optionC, optionD, correctAnswer, explanation);
        this.id = id;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @Override
    public String toString() {
        return "Question [id=" + id + ", language=" + language + ", topic=" + topic 
                + ", difficulty=" + difficulty + ", marks=" + marks + ", question=" + question 
                + ", correctAnswer=" + correctAnswer + "]";
    }
}
