package com.recycler.recyclerviewdemo.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by WangYu on 2017/8/1.
 */

public class ItemDecoration1 extends RecyclerView.ItemDecoration {

    private final Paint paint;
    private final int dividerHeight=20;
    public ItemDecoration1() {
        super();
        paint = new Paint();
        paint.setColor(0x000000);
    }

    /**
     * 画item的背景
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //要画分割线就是画一个矩形,需要确定上下左右的位置
        //所有分割线左右的位置是固定的,直接获取
        float left=parent.getPaddingLeft();
        float right = parent.getRight() - parent.getPaddingRight();
        //每个分割线上下的位置是不固定的,需要遍历获取
        //parent.getChildCount()-1 最后一个不需要分割线
        for (int i = 0; i < parent.getChildCount()-1; i++) {
            float top=parent.getChildAt(i).getBottom();
            float bottom=top+dividerHeight;
            //画矩形
            c.drawRect(left, top, right, bottom, paint);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    /**
     * 这里用于设置item的上下左右边界距离,露出来的边界默认会显现recyclerview的北京颜色或者上层布局的颜色
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom=dividerHeight;
    }
}
