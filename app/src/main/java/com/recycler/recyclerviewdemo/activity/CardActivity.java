package com.recycler.recyclerviewdemo.activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.recycler.recyclerviewdemo.R;
import com.recycler.recyclerviewdemo.adapter.CardAdapter;
import com.recycler.recyclerviewdemo.http.ApiService;
import com.recycler.recyclerviewdemo.http.RetrofitUtil;
import com.recycler.recyclerviewdemo.view.CardLayoutManagerTest;

/**
 * Created by WangYu on 2017/8/3.
 */

public class CardActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private CardAdapter adapter;
    private static final String TAG = "wangyu";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardactivity);
        recyclerView = (RecyclerView) findViewById(R.id.cardrecyclerview);
        CardLayoutManagerTest manager = new CardLayoutManagerTest(recyclerView, itemTouchHelper);
        recyclerView.setLayoutManager(manager);
        adapter = new CardAdapter(this, null, false);
        recyclerView.setAdapter(adapter);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        getImage(1);
    }

    /**
     * itemtouchhelper是recyclerview的手势拖动辅助类,我们可以控制item的移动
     * 这里我们需要的是第一张卡片可以移动删除,所以需要的是swipeflag
     */
    public ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int swipeflag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            return makeMovementFlags(0, swipeflag);
        }

        /**
         * 这个方法是在dragflag不为0的情况下,view的posttion发生了调换的时候调用
         * @param recyclerView
         * @param viewHolder
         * @param target
         * @return
         */
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //当view判定被移出屏幕的时候会调用,我们适时的更新数据,从而触发layoutmanager的重绘,更新view
            adapter.removeData(viewHolder.getLayoutPosition());
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            //这里是控制item是否可拖动,我们这里返回false是为了控制只有最上面的view可以拖动
            return false;
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            //view被拖动的时候就会不断调用
//            Log.i("wangyu", dX+","+dY+","+actionState+","+isCurrentlyActive);
            float distance = Math.max(Math.abs(dX), Math.abs(dY));
            float roi = distance / (recyclerView.getWidth() * getSwipeThreshold(viewHolder));
            if (roi > 1) {
                roi = 1;
            }
//            Log.i("wangyu", "roi: "+roi);
//            Log.i("wangyu", "f: "+(1-1/10.0f*(1-roi)));

            int childcount = recyclerView.getChildCount();
            if (childcount == 4) {
                for (int i = childcount - 2; i > 0; i--) {
                    View view = recyclerView.getChildAt(i);
                    float a = (1 - 0.1f * (2 - i)) - 0.1f * (1 - roi);
                    if (i == 1 && childcount == 3) {
                        Log.i(TAG, "a: " + a);
                    }
                    view.setScaleX(a);
                    view.setScaleY(a);
                    view.setTranslationY(view.getHeight() / 16 * (childcount - 1 - i - roi));
                }
            } else if (childcount == 3) {
                View view1 = recyclerView.getChildAt(1);
                view1.setScaleX(1 - 0.1f * (1 - roi));
                view1.setScaleY(1 - 0.1f * (1 - roi));
                view1.setTranslationY(view1.getHeight() / 16 * (1 - roi));
                View view2=recyclerView.getChildAt(0);
                view2.setScaleX(0.9f- 0.1f * (1 - roi));
                view2.setScaleY(0.9f- 0.1f * (1 - roi));
                view2.setTranslationY(view1.getHeight() / 16 * (2 - roi));
            } else if (childcount == 2) {
                View view1 = recyclerView.getChildAt(0);
                view1.setScaleX(1 - 0.1f * (1 - roi));
                view1.setScaleY(1 - 0.1f * (1 - roi));
                view1.setTranslationY(view1.getHeight() / 16 * (1 - roi));
            }
            if (childcount>0) {
                View currentView = recyclerView.getChildAt(childcount - 1);
                currentView.setAlpha(1.5f-roi);
            }
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setAlpha(1.0f);
        }
    });


    public void getImage(int page) {
        RetrofitUtil.getApiService(ApiService.class)
                .getImageData(12, page)
                .compose(RetrofitUtil.getComposer())
                .subscribe(imageData -> {
                    if (page == 1) {
                        adapter.setNewData(imageData.getResults());
                    } else {
                        if (imageData.getResults().size() == 0) {
//                            adapter2.setLoadEndView(R.layout.load_end_layout);
                        } else {
                            adapter.setLoadMoreData(imageData.getResults());
                        }
                    }
                });
    }
}
