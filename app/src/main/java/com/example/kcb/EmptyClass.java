package com.example.kcb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
public class EmptyClass extends AppCompatActivity {
    private TextView getTime;
    private Calendar calendar;// 用来装日期的
    private DatePickerDialog dialog;
    private Spinner spinner;
    private String selectedAddr;

    private DatePickerDialog date;
    private Button btn;
    private  Toast toast;
    private  Toast toasta;
    private Spinner s_elements;
    private int s_position;//记录选择的位置
    private String element;
    private Button bn_select3;
    private TextView timeText;
    int year;
    int data;
    int month;
    private String dateStr=null;//日期字符串
    String x;
    String s;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_class);
        spinner=findViewById(R.id.spacer1);
        getTime = (TextView) findViewById(R.id.btn_Date);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                //拿到被选择项的值
                selectedAddr = (String) spinner.getSelectedItem();
                System.out.println("选中"+selectedAddr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        getTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                dialog = new DatePickerDialog(EmptyClass.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                System.out.println("年-->" + year + "月-->"
                                        + monthOfYear + "日-->" + dayOfMonth);
                                getTime.setText(year + "/" + monthOfYear + "/"
                                        + dayOfMonth);
                            }
                        }, year=calendar.get(Calendar.YEAR),
                        month=calendar.get(Calendar.MONTH),
                        data=calendar.get(Calendar.DAY_OF_MONTH));
                // year = calendar.get(calendar.YEAR);
                // month = calendar.get(calendar.MONTH)+1;
                // data = calendar.get(calendar.DAY_OF_MONTH);
                dateStr = year+"-"+month+"-"+data;
                System.out.println(dateStr);
                dialog.show();
            }
        });
    }

}