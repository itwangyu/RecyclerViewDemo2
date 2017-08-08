package com.recycler.recyclerviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.recycler.recyclerviewdemo.activity.CardActivity;
import com.recycler.recyclerviewdemo.activity.NormalListActivity;
import com.recycler.recyclerviewdemo.activity.StickyHeaderActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //主要练习三种layoutmanager
        findViewById(R.id.bt_1).setOnClickListener(view ->startActivity(new Intent(MainActivity.this, NormalListActivity.class)));
        //练习decoration
        findViewById(R.id.bt_2).setOnClickListener(view ->startActivity(new Intent(MainActivity.this, StickyHeaderActivity.class)));
        //自定义manager
        findViewById(R.id.bt_3).setOnClickListener(view ->startActivity(new Intent(MainActivity.this, CardActivity.class)));
    }

}
