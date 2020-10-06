package com.example.course;

public class CourseInfo {
    private String courseName;
    private String teacher;
    private String color;

    public CourseInfo(String courseName,String teacher,String color){
        this.courseName=courseName;
        this.teacher=teacher;
        this.color=color;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getColor() {
        return color;
    }
}
