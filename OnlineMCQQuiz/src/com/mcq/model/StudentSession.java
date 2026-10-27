package com.mcq.model;

/**
 * StudentSession.java
 * Manages the active session for a logged-in student.
 */
public class StudentSession {

    private static Student currentStudent = null;

    public static synchronized void login(Student student) {
        currentStudent = student;
    }

    public static synchronized void logout() {
        currentStudent = null;
    }

    public static synchronized Student getCurrentStudent() {
        return currentStudent;
    }

    public static synchronized boolean isLoggedIn() {
        return currentStudent != null;
    }
}
