package com.recycler.recyclerviewdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.recycler.recyclerviewdemo.R;
import com.recycler.recyclerviewdemo.adapter.StikyHeaderAdapter;
import com.recycler.recyclerviewdemo.bean.Goods;
import com.recycler.recyclerviewdemo.view.StickyDecoration1;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by WangYu on 2017/8/2.
 */

public class StickyHeaderActivity extends AppCompatActivity {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stikyheader);
        //首先造数据,假设有四类商品在一个列表里面,每种商品有四个,循环添加
        ArrayList<Goods> dataList = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 8; j++) {
                dataList.add(new Goods(i,"类型"+i+",商品"+j));
            }
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setAdapter(new StikyHeaderAdapter(this,dataList,false));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new StickyDecoration1(pos -> dataList.get(pos).type));
    }
}
