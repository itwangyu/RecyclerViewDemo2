package com.recycler.recyclerviewdemo.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

/**
 * Created by WangYu on 2017/8/2.
 */

public class StikyDecoration extends RecyclerView.ItemDecoration {
    CallBack callBack;
    private final Paint textPaint;
    private static final String TAG = "TAG";
    private final Paint paint;
    private Paint.FontMetrics fontMetrics;
    private int topGap;
    public interface CallBack {
        int getTypeId(int pos);
    }

    public StikyDecoration(CallBack callBack) {
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
        topGap = 80;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
//        Log.i(TAG, "onDraw: :"+parent.getChildCount());
//        float left=parent.getPaddingLeft();
//        float right=parent.getWidth()-parent.getPaddingRight();
//        for (int i = 0; i < parent.getChildCount(); i++) {
//            if (isHead(i)) {
//                float bottom=parent.getChildAt(i).getTop();
//                float top=bottom+40;
//                c.drawRect(left, top, right, bottom, bg);
//                c.drawText(callBack.getTypeId(i)+"位置",10,5,textPaint);
//            }
//        }
    }

    /**
     * 在view的上面画,是整个recyclerview,并不局限在item,所以要为每个item分别添加什么tag,需要获取他们的位置
     * 随着view滚动会不断调用,因为view的位置是不断改变的,所以我们画的tag一定要根据item获取位置,然后画出
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int itemCount = state.getItemCount();//这个是数据的总个数
        int childCount = parent.getChildCount();//这个是recyclerview所填充的view个数,大概是当前屏幕所显示出来的数量+1
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        float lineHeight = textPaint.getTextSize() + fontMetrics.descent;

        long preGroupId, groupId = -1;
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);


            /**
             * 这里的赋值加上下面的判断,确保同一组我们只在显示出来的第一个view上绘制
             这里不要陷入误区,我们并不是在每组的第一个上面画,而是在每组展示出来的第一个上面画
             * 拿第一组来说:一开始是画在第一条上面,随着屏幕向上滚动,当第一条view消失在屏幕上的时候,
             * 上面通过 parent.getChildAt(0);所得到的view其实是原来的第二个view,所以当我们通过
             * parent.getChildAdapterPosition(view)获取到他所在的位置的时候,会发现position是2,
             * 但是当前其实是第一次循环,i=0,我们知道,当i=0的时候是一定会在这个view上画的.所以....
             * 以此类推,第二个消失的时候,我们画在了第三个上,第四个,一直到第一组的最后一个,这里是第8个,
             * 这里会有一个变化,涉及到跟下一个画布交替.
             *  看下一段
             */
            preGroupId = groupId;
            groupId = callBack.getTypeId(position);
            if (groupId < 0 || groupId == preGroupId) continue;

            String textLine = groupId + "组";
            if (TextUtils.isEmpty(textLine)) continue;
            /**
             * viewBottom标记当前view的底边,当来到第一组最后一个view的时候,他的底边其实就是下一个tag的顶端
             * textY表示tag的底端,正常情况下tag的低端就是view的顶端,但是为了实现在滚动的时候,tag能够停留在最上端,
             * 所以要跟taoGap取最大值,因为当view向上慢慢移出屏幕的时候,getTop得到的值会慢慢变小,最后到达-topGap,为了
             * 保证tag停留在顶端,textY最小的高度就是tag的高度80.
             */
            int viewBottom = view.getBottom();
            float textY = Math.max(topGap, view.getTop());
            Log.i(TAG,"pos:"+position+"  top:"+view.getTop()+"  textY:"+textY +" viewBottom:"+viewBottom);
            /**
             * 这里对边界判断一下
             * 然后获取下一个view的type,与当前type做对比,如果不同,那么就说明当前view是本组的最后一个view
             * 这时候为了展现下个tag把当前tag顶上去的效果,我们比较viewBtoom和textY的大小,
             * viewBottom是当前view的最下端,textY是tag的最下端,当viewBottom<textY,也就是组内最后一个view进入了header
             * 那么tag就要跟随这最后一个view向上慢慢移出屏幕,因此, textY = viewBottom
             */
            if (position + 1 < itemCount) { //下一个和当前不一样移动当前
                long nextGroupId = callBack.getTypeId(position + 1);
                if (nextGroupId != groupId && viewBottom < textY ) {//组内最后一个view进入了header
                    textY = viewBottom;
                }
            }
            c.drawRect(left, textY - topGap, right, textY, paint);
            c.drawText(textLine, left, textY-20, textPaint);
        }
    }

    /**
     * 填充每个item的时候都会调用一次,之后不在调用
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //这里只是为一开始的显示留出位置,判断的确实是每组的第一个view
        int pos = parent.getChildAdapterPosition(view);
        if (isHead(pos)) {
            outRect.top = 80;
        } else {
            outRect.top=0;
        }
        
    }

    public boolean isHead(int pos) {
        if (pos == 0) {
            return true;
        }
        int preId = callBack.getTypeId(pos - 1);
        int id = callBack.getTypeId(pos);
        return preId!=id;
    }
}
