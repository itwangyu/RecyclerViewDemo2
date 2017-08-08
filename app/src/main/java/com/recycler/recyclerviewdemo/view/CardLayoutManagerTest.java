package com.recycler.recyclerviewdemo.view;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by WangYu on 2017/8/7.
 */

public class CardLayoutManagerTest extends RecyclerView.LayoutManager {
    private static final String TAG="wangyu";

    public CardLayoutManagerTest(RecyclerView recyclerView, ItemTouchHelper itemTouchHelper) {
        this.recyclerView = recyclerView;
        this.itemTouchHelper = itemTouchHelper;
    }

    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * notifychange的时候就会调用,所以当上面的一个被移出的时候,只要配合数据变化,那么所有的view就会重新layout添加
     * @param recycler
     * @param state
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        int itemcount=getItemCount();
        Log.w(TAG, "itemcount: "+itemcount );
        if (itemcount<=0) {return; }
        detachAndScrapAttachedViews(recycler);
        //遍历view倒序添加,因为第一个view要显示在最上面,从性能考虑,我们永远只添加最多4个,也就是list里面的0123
        if (itemcount > 3) {
            for (int i = 3; i >= 0; i--) {
                View view = layoutView(recycler, i);
                //要显示出层叠的效果,所以要对view进行缩小和错位
                if (i == 3 || i == 2) {
                    view.setScaleX(0.8f);
                    view.setScaleY(0.8f);
                    view.setTranslationY(getDecoratedMeasuredHeight(view) / 16.0f * 2);
                } else if (i == 1) {
                    view.setScaleY(0.9f);
                    view.setScaleX(0.9f);
                    view.setTranslationY(getDecoratedMeasuredHeight(view) / 16.0f);
                }
            }
        } else {
            for (int i = itemcount-1; i >=0 ; i--) {
                View view = layoutView(recycler, i);
                view.setScaleX(1 - 0.1f * i);
                view.setScaleY(1-0.1f*i);
                view.setTranslationY(getDecoratedMeasuredHeight(view)/16.0f*i);
            }
        }
    }

    private View layoutView(RecyclerView.Recycler recycler, int i) {
        //获取到当前位置的view
        View view = recycler.getViewForPosition(i);
        addView(view);//添加
        //下面要测量view的宽高,计算得出坐上右下的位置,以便设置卡片居中
        measureChildWithMargins(view, 0, 0);
        int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
        int heightSpach = getHeight() - getDecoratedMeasuredHeight(view);
        Log.i(TAG, "widthSpace: " + widthSpace + "  heightSpach: " + heightSpach);
        int left = widthSpace / 2;
        int top = heightSpach / 2;
        int right = left + getDecoratedMeasuredWidth(view);
        int bottom = top + getDecoratedMeasuredHeight(view);
        //设置view的位置,要居中
        layoutDecoratedWithMargins(view, left, top, right, bottom);
        if (i == 0) {
            view.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    itemTouchHelper.startSwipe(recyclerView.getChildViewHolder(view));
                }
                return false;
            });
        }
        return  view;
    }

}
