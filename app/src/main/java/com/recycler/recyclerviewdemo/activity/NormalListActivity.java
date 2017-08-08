package com.recycler.recyclerviewdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.recycler.recyclerviewdemo.R;
import com.recycler.recyclerviewdemo.adapter.NormalListAdapter;
import com.recycler.recyclerviewdemo.bean.ImageData;
import com.recycler.recyclerviewdemo.http.ApiService;
import com.recycler.recyclerviewdemo.http.RetrofitUtil;
import com.recycler.recyclerviewdemo.view.ItemDecoration1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYu on 2017/7/27.
 */

public class NormalListActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TAG";
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private int page=1;
    private ArrayList<ImageData.ResultsBean> imagelist;
//    private NormalListAdapter adapter;
    private boolean canRefresh;
    private NormalListAdapter adapter2;
    private Button bt_staggered;
    private Button bt_grid;
    private Button bt_ll;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normallist);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        bt_staggered = (Button) findViewById(R.id.bt_staggeredGrid);
        bt_grid = (Button) findViewById(R.id.bt_gridlayout);
        bt_ll = (Button) findViewById(R.id.bt_linearlayout);
        bt_staggered.setOnClickListener(this);
        bt_grid.setOnClickListener(this);
        bt_ll.setOnClickListener(this);

        bt_staggered.performClick();

        adapter2 = new NormalListAdapter(this, null, true);
        adapter2.setLoadingView(R.layout.load_loading_layout);
        adapter2.setOnItemClickListener((viewHolder, girlItemData, position) -> {
            Toast.makeText(this, position+"个元素", Toast.LENGTH_SHORT).show();
        });

        adapter2.setOnLoadMoreListener(isReload -> {
//            if ( !isReload) {
//                return;
//            }
//            isLoadMore = true;
//            PAGE_COUNT = mTempPageCount;
            getImage(++page);
        });
        recyclerView.setAdapter(adapter2);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemDecoration1());
        itemTouchHelper.attachToRecyclerView(recyclerView);
        getImage(page);
    }

    ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags=0;
            if(recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager){
                dragFlags=ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
            }
            return makeMovementFlags(dragFlags,0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //这里分别获取拖拽的对象和目标位置,进行数据的转移
            int from=viewHolder.getAdapterPosition();
            int to=target.getAdapterPosition();
            List<ImageData.ResultsBean> datalist = adapter2.getData();
            ImageData.ResultsBean bean = datalist.get(from);
            datalist.remove(from);
            datalist.add(to, bean);
            adapter2.notifyItemMoved(from, to);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }
    });

    public void getImage(int page) {
        canRefresh=false;
        RetrofitUtil.getApiService(ApiService.class)
                .getImageData(12, page)
                .compose(RetrofitUtil.getComposer())
                .subscribe(imageData -> {
                    canRefresh=true;
                    if (page == 1) {
                        adapter2.setNewData(imageData.getResults());
                    } else {
                        if (imageData.getResults().size() == 0) {
//                            adapter2.setLoadEndView(R.layout.load_end_layout);
                        } else {
                            adapter2.setLoadMoreData(imageData.getResults());
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_staggeredGrid:
                staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
                recyclerView.setLayoutManager(staggeredGridLayoutManager);
                break;
            case R.id.bt_gridlayout:
                GridLayoutManager gridLayoutManager = new GridLayoutManager(NormalListActivity.this, 2);
                recyclerView.setLayoutManager(gridLayoutManager);
                break;
            case R.id.bt_linearlayout:
                LinearLayoutManager layoutManager = new LinearLayoutManager(NormalListActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                break;
        }
    }
}
