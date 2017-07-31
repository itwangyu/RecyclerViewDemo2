package com.recycler.recyclerviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.recycler.recyclerviewdemo.activity.NormalListActivity;

public class MainActivity extends AppCompatActivity {


    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = (Button) findViewById(R.id.bt_normal);
        bt.setOnClickListener(view ->startActivity(new Intent(MainActivity.this, NormalListActivity.class)));
    }

}
