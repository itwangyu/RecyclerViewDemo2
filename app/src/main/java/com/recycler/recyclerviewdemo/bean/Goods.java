package com.recycler.recyclerviewdemo.bean;

/**
 * Created by WangYu on 2017/8/2.
 */

public class Goods {
    public static final int TYPE1 = 1,TYPE2=2,TYPE3=3,TYPE4=4;

    public int type;//商品所属的类型

    public Goods(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public String name;//商品名称
}
