package com.example.kcb;

import androidx.appcompat.app.AppCompatActivity;

import com.example.course.Course;
import com.example.course.EmptyClassroom;
import com.example.paqu.CdutHttpHelper;
import com.example.db.CourseDataBase;
import com.example.db.DataBaseHelper;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public CourseDataBase courseDataBase;
    public SQLiteDatabase db;
    public DataBaseHelper dbh;
    public CdutHttpHelper chh;
    private Button my_button ;
    public TextView userAcount;
    public TextView passWord;
    private CheckBox cb_choose;
    private SharedPreferences config;
    private int state;
    private ArrayList<Course >list;
    SharedPreferences.Editor edit;
    String useracount;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userAcount=findViewById(R.id.et_username);
        passWord=findViewById(R.id.et_password);

        cb_choose=findViewById(R.id.checkbox);
        my_button=findViewById(R.id.btn_login);
        config=getSharedPreferences("config", MODE_PRIVATE);
        edit= config.edit();

        courseDataBase=new CourseDataBase(MainActivity.this);
        db=courseDataBase.getWritableDatabase();
        dbh=new DataBaseHelper();
        chh=CdutHttpHelper.getCdutHttpHelper();

        my_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //是否记住了密码，初始化为false
                boolean isCheck=config.getBoolean("isCheck", false);
                if(isCheck){
                    userAcount.setText(config.getString("username", ""));
                    passWord.setText(config.getString("password", ""));
                    cb_choose.setChecked(isCheck);
                }

                useracount=userAcount.getText().toString();
                password=passWord.getText().toString();

                if(useracount.isEmpty()||password.isEmpty()){
                    Toast.makeText(MainActivity.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    //存储CheckBox的状态
                    edit.putBoolean("isCheck", isCheck);
                    if (isCheck) {
                        edit.putString("username", useracount).putString("password", password);
                    } else {
                        edit.remove("username").remove("password");
                    }
                    //提交到本地
                    login(useracount, password);
//                    int state=Integer.parseInt(chh.getState());
//                    System.out.println(state);
                }
            }
        });

    }

    public void login(final String userAcount, final String passWord){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                loginAndDB(userAcount,passWord);
                Looper.loop();
            }
        }).start();
    }

    public void loginAndDB(String userAcount,String passWord){

        list=dbh.messageIntoDB(userAcount, passWord);
        state= Integer.parseInt(chh.getState());

//        String []week={"5"};
//        List<Course> listweek=courseDataBase.selectedOneWeek(db,week);
//        for(Course c:listweek){
//            System.out.println(c.toString());
//        }
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                otherFun();
            }
        });
    }

    public void otherFun(){
        if(state!=0){
            System.out.println("2.chh.getState(): "+state);
            Toast.makeText(MainActivity.this, "账号或密码不正确", Toast.LENGTH_SHORT).show();
        }else{
            if(courseDataBase.selectAll(db).isEmpty()) {
                int id=0;
                for(Course c:list){
                    c.setId(id);
                    courseDataBase.insertCourse(db,c);
                    ++id;
                }
            }

            edit.commit();
            Intent intent=new Intent(MainActivity.this,First.class);
            Bundle bundle=new Bundle();
            bundle.putString("userAcount",useracount);
            bundle.putString("passWord",password);
            intent.putExtra("Message",bundle);
            startActivity(intent);
        }
    }
    public void showNotification(View view){
        //获得一个通知管理器
        NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        //建立一个通知
        Notification notification=null;
        String id="mchannel";
        String name="下一节课通道";

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            //Log.i("hello",mChannel.toString());

            notificationManager.createNotificationChannel(mChannel);

            notification=new Notification.Builder(this,id)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.drawable.tongzhilan_big_icon))
                    .setSmallIcon(null)
                    //.setStyle(new Notification.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.tongzhilan_big_icon)))
                    .setContentTitle("下一节课")
                    .setContentText("")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(false)

                    //绑定对应的Activity
                    .setContentIntent(PendingIntent.getActivity(this,1,new Intent(this,Notification.class),PendingIntent.FLAG_CANCEL_CURRENT))
                    .build();
        }else if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            notification=new Notification.Builder(this)
                    .setContentTitle("下一节课")
                    .setContentText("")
                    .setSmallIcon(R.drawable.tongzhilan_big_icon)
                    .setOngoing(true)
                    .setContentIntent(PendingIntent.getActivity(this,1,new Intent(this,Notification.class),PendingIntent.FLAG_CANCEL_CURRENT))
                    .build();
    }

        //发出通知
        notificationManager.notify(0,notification);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showTips(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("退出程序")
                .setMessage("是否退出程序")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        courseDataBase.deleteTable(db,"course");
                        System.out.println("Delete table course");
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){ return; }
                }).create(); //创建对话框
        alertDialog.show(); // 显示对话框
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
            this.showTips();
            return false;
        }
        return false;
    }
}
