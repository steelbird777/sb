package com.example.sb;

public class Enrollment {
    private String userId;
    private String subjectId;
    private String subjectName;
    private String enrollDate;

    public Enrollment() {
        // Required empty public constructor for Firestore
    }

    public Enrollment(String userId, String subjectId, String subjectName, String enrollDate) {
        this.userId = userId;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.enrollDate = enrollDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(String enrollDate) {
        this.enrollDate = enrollDate;
    }
}

