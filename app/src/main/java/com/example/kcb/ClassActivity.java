package com.example.kcb;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.course.Course;
import com.example.db.CourseDataBase;
import com.example.db.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;


public class ClassActivity extends AppCompatActivity {
    private Spinner spinner;
    private CourseDataBase courseDataBase;
    private SQLiteDatabase sqLiteDatabase;
    private CourseTableView courseTableView;
    List<Course > list;

    private int weekNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        courseDataBase=new CourseDataBase(ClassActivity.this);
        sqLiteDatabase=courseDataBase.getWritableDatabase();
        spinner = findViewById(R.id.week);
        courseTableView=findViewById(R.id.courseTable);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                //获取被选择项的值
                weekNumber=spinner.getSelectedItemPosition()+1;
                String num=String.valueOf(weekNumber);
                String weekNum[]={num};
                System.out.println("spinner.getSelectedItemPosition()："+spinner.getSelectedItemPosition());

                if(!list.isEmpty()){
                    list.clear();
                }else{
                    list=courseDataBase.selectedOneWeek(sqLiteDatabase,weekNum);
                    for(Course c:list){
                        System.out.println(c.getCourseName()+" "+c.getClassRoom()+" "+c.getTeacher());
                    }
                    courseTableView.setTimeTable(list);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }
}

