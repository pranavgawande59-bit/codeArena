package com.mcq.model;

import java.sql.Timestamp;

/**
 * CodingProblem.java
 * Model class representing a programming problem in the Problem Bank,
 * complete with description, constraints, sample I/O, hints/explanations, and starter code.
 */
public class CodingProblem {
    private int id;
    private String title;
    private String language;
    private String topic;
    private String difficulty; // "Beginner", "Intermediate", "Advanced"
    private String description;
    private String constraints;
    private String sampleInput;
    private String sampleOutput;
    private String explanation;
    private String starterCode;
    private Timestamp createdAt;

    // Transient student progress status ("Solved", "Attempted", "Unsolved")
    private String studentStatus = "Unsolved";
    private String savedUserCode = null;

    public CodingProblem() {
    }

    public CodingProblem(String title, String language, String topic, String difficulty,
                         String description, String constraints, String sampleInput, String sampleOutput,
                         String explanation, String starterCode) {
        this.title = title;
        this.language = (language != null && !language.trim().isEmpty()) ? language : "Java";
        this.topic = (topic != null && !topic.trim().isEmpty()) ? topic : "Arrays";
        this.difficulty = (difficulty != null && !difficulty.trim().isEmpty()) ? difficulty : "Beginner";
        this.description = description;
        this.constraints = constraints;
        this.sampleInput = sampleInput;
        this.sampleOutput = sampleOutput;
        this.explanation = explanation;
        this.starterCode = starterCode;
    }

    public CodingProblem(int id, String title, String language, String topic, String difficulty,
                         String description, String constraints, String sampleInput, String sampleOutput,
                         String explanation, String starterCode, Timestamp createdAt) {
        this(title, language, topic, difficulty, description, constraints, sampleInput, sampleOutput, explanation, starterCode);
        this.id = id;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConstraints() {
        return constraints;
    }

    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

    public String getSampleInput() {
        return sampleInput;
    }

    public void setSampleInput(String sampleInput) {
        this.sampleInput = sampleInput;
    }

    public String getSampleOutput() {
        return sampleOutput;
    }

    public void setSampleOutput(String sampleOutput) {
        this.sampleOutput = sampleOutput;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getStarterCode() {
        return starterCode;
    }

    public void setStarterCode(String starterCode) {
        this.starterCode = starterCode;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(String studentStatus) {
        this.studentStatus = studentStatus;
    }

    public String getSavedUserCode() {
        return savedUserCode;
    }

    public void setSavedUserCode(String savedUserCode) {
        this.savedUserCode = savedUserCode;
    }

    @Override
    public String toString() {
        return "CodingProblem [id=" + id + ", title=" + title + ", topic=" + topic + ", difficulty=" + difficulty + "]";
    }
}
