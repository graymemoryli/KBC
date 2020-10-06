package com.example.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.course.Course;

public class CourseDataBase extends SQLiteOpenHelper{
    private static final String DBNAME="course.db";

    public CourseDataBase(Context context){
        super(context,DBNAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql ="create table course(id integer primary key autoincrement,courseName text not null," +
                "classRoom text,teacher text,week integer not null," +
                "day integer not null,classStart integer not null,classEnd integer not null)";
        db.execSQL(sql);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    public List<Course> selectAll(SQLiteDatabase db){
        List<Course> list=new ArrayList<Course>();
        Cursor cursor=db.query("course",null,null,null,null,null,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            Course course=new Course();
            course.setId(cursor.getInt(0));
            course.setCourseName(cursor.getString(1));
            course.setClassRoom(cursor.getString(2));
            course.setTeacher(cursor.getString(3));
            course.setWeek(cursor.getInt(4));
            course.setDay(cursor.getInt(5));
            course.setClassStart(cursor.getInt(6));
            course.setClassEnd(cursor.getInt(7));
            //course.setColor(cursor.getString(8));
            list.add(course);
        }
        return list;
    }

    public void insertCourse(SQLiteDatabase db,Course course){
        ContentValues values=new ContentValues();
        //values.put("id",course.getId());
        //System.out.println(course.getId());
        values.put("courseName",course.getCourseName());
        values.put("classRoom",course.getClassRoom());
        values.put("teacher",course.getTeacher());
        values.put("week",course.getWeek());
        values.put("day",course.getDay());
        values.put("classStart",course.getClassStart());
        values.put("classEnd",course.getClassEnd());
        db.insert("course",null,values);
    }

    public List<Course> selectedOneWeek(SQLiteDatabase db,String[] week){
        List<Course> list=new ArrayList<Course>();
        Cursor cursor=db.rawQuery("select * from course where week=?",week);
        if(cursor.getCount()>0){
            for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                Course course=new Course(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(5),cursor.getInt(6),cursor.getInt(7));
                course.setId(cursor.getInt(0));
                list.add(course);
            }
        }else return null;
        return list;
    }

    public int[] weekNameList(SQLiteDatabase db){
        int[] weekList;
        Cursor cursor=db.rawQuery("select distinct week from course",null);
        weekList=new int[cursor.getCount()];
        cursor.moveToFirst();
        for (int i=0;i<cursor.getCount();i++){
            weekList[i]=cursor.getInt(0);
            cursor.moveToNext();
        }
        return weekList;
    }
    public Course selectedCourse(int id,SQLiteDatabase db){
        String sql="select * from course where id='"+id+"';";
        Cursor cursor=db.rawQuery(sql,null);
        Course course=new Course();
        course.setId(cursor.getInt(0));
        course.setCourseName(cursor.getString(1));
        course.setClassRoom(cursor.getString(2));
        course.setTeacher(cursor.getString(3));
        course.setWeek(cursor.getInt(4));
        course.setDay(cursor.getInt(5));
        course.setClassStart(cursor.getInt(6));
        course.setClassEnd(cursor.getInt(7));

        return course;
    }

    public void deleteTable(SQLiteDatabase db,String tableName){
        String sql ="DELETE FROM '"+tableName+"'";
        db.execSQL(sql);
    }
}
