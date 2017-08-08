package com.recycler.recyclerviewdemo.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;

/**
 * Created by WangYu on 2017/8/2.
 */

public class StickyDecoration1 extends RecyclerView.ItemDecoration {
    CallBack1 callBack;
    Paint paint;
    Paint textPaint;
    private final Paint.FontMetrics fontMetrics;
    private final int tagHeight;

    public interface CallBack1 {
        int getTypeId(int pos);
    }
    public StickyDecoration1(StickyDecoration1.CallBack1 callBack) {
        super();
        this.callBack=callBack;
        paint = new Paint();
        paint.setColor(0xffff0000);

        textPaint = new TextPaint();
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(50);
        textPaint.setColor(Color.BLACK);
        fontMetrics = new Paint.FontMetrics();
        textPaint.getFontMetrics(fontMetrics);
        textPaint.setTextAlign(Paint.Align.LEFT);
        tagHeight = 80;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //这里判断一下,为每组第一个留出位置
        int position = parent.getChildAdapterPosition(view);
        if (isHead(position)) {
            //是组中的第一个view的时候,上面要留出tag的位置
            outRect.top = tagHeight;
        } else {
            outRect.top=0;
        }
    }

    public boolean isHead(int pos) {
        if (pos == 0) {return true;}
        int preposType = callBack.getTypeId(pos - 1);//上一个类型
        int curposType = callBack.getTypeId(pos);//当前view的类型
        return preposType!=curposType;//不一致的时候就说明这个位置的view是这个组中的第一个
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int totalcount=state.getItemCount();//所有数据总数
        int childcount=parent.getChildCount();//recyclerview所填充的view数量
        //计算左边界和有边界
        float left=parent.getPaddingLeft();
        float right=parent.getWidth()-parent.getPaddingRight();
        int preType=-1,curType=-1;
        for (int i = 0; i < childcount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            //根据位置获取到当前view的type
            curType = callBack.getTypeId(position);
            if (curType == preType) {
                continue;
            }
            preType=curType;
            //上面的判断确保每种类型只在最开始走一次
            float bottom = Math.max(tagHeight, view.getTop());
            float top=bottom-tagHeight;
            //下面开始判断当走到每组最后一个的时候的情况,需要判断当前view与下一个view的类型
            if (position + 1 < totalcount) {
                int nextType = callBack.getTypeId(position + 1);
                if (curType!=nextType&&view.getBottom()<bottom) {//本组的最后一个view缩进head中
                    bottom=view.getBottom();
                }
            }
            c.drawRect(left, top, right, bottom, paint);
            c.drawText(callBack.getTypeId(position)+"组",left,bottom-20,textPaint);
        }
    }

}
