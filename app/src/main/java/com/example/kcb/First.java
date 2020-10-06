package com.example.kcb;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class First extends FragmentActivity {
    private Intent intent;
    private fragment2 fragment2;
    private fragment1 fragment1;
    private ImageButton btn_home;
    private ImageButton btn_set;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        btn_home=findViewById(R.id.home);
        btn_set=findViewById(R.id.set);
        fragment2=new fragment2();
        fragment1=new fragment1();

        intent=getIntent();
        Bundle bundle=intent.getBundleExtra("Message");
        final String userAcount=bundle.getString("userAcount");
        final String passWord=bundle.getString("passWord");
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_f,fragment1).commitAllowingStateLoss();
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_f,fragment1).commitAllowingStateLoss();
                Intent in=new Intent(First.this,fragment1.class);
                Bundle bun=new Bundle();
                bun.putString("userAcount",userAcount);
                bun.putString("passWord",passWord);
                in.putExtra("Message",bun);
                startActivity(in);
            }
        });
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_f,fragment2).commitAllowingStateLoss();
            }
        });

    }

}

