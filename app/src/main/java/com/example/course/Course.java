package com.example.course;

import java.io.Serializable;

public class Course implements Serializable {
    private int id;//数据库中的id
    private String courseName;//名称
    private String classRoom;//教室
    private String teacher;//教师
    private int week;//周次
    private int day;//星期
    private int classStart;//开始节次
    private int classEnd;//结束节次

    public Course() {
    }

    public Course(String courseName, String teacher, String classRoom, int week, int day, int classStart, int classEnd){
        this.courseName=courseName;
        this.teacher=teacher;
        this.classRoom=classRoom;
        this.week=week;
        this.day=day;
        this.classStart=classStart;
        this.classEnd=classEnd;
        //this.color=color;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getTeacher() {
        return teacher;
    }
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
    public String getClassRoom() {
        return classRoom;
    }
    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }
    public int getWeek() {
        return week;
    }
    public void setWeek(int week) {
        this.week = week;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int getClassStart() {
        return classStart;
    }
    public void setClassStart(int classStart) {
        this.classStart = classStart;
    }
    public int getClassEnd() {
        return classEnd;
    }
    public void setClassEnd(int classEnd) {
        this.classEnd = classEnd;
    }


    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", teacher='" + teacher + '\'' +
                ", classRoom='" + classRoom + '\'' +
                ", week=" + week +
                ", day=" + day +
                ", classStart=" + classStart +
                ", classEnd=" + classEnd +
                '}';
    }
}

