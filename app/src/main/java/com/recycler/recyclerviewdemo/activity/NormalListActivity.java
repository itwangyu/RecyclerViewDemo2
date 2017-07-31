package com.recycler.recyclerviewdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.recycler.recyclerviewdemo.R;
import com.recycler.recyclerviewdemo.adapter.NormalListAdapter;
import com.recycler.recyclerviewdemo.bean.ImageData;
import com.recycler.recyclerviewdemo.http.ApiService;
import com.recycler.recyclerviewdemo.http.RetrofitUtil;

import java.util.ArrayList;

/**
 * Created by WangYu on 2017/7/27.
 */

public class NormalListActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager layoutManager;
    private int page=1;
    private ArrayList<ImageData.ResultsBean> imagelist;
//    private NormalListAdapter adapter;
    private boolean canRefresh;
    private NormalListAdapter adapter2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normallist);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);

        adapter2 = new NormalListAdapter(this, null, true);
        adapter2.setLoadingView(R.layout.load_loading_layout);
        adapter2.setOnItemClickListener((viewHolder, girlItemData, position) -> {
//            Intent intent = new Intent(mActivity, GirlDetailActivity.class);
//            intent.putExtra("girl_item_data", girlItemData);
//            startActivity(intent);
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
        getImage(page);
    }

    public void getImage(int page) {
        canRefresh=false;
        RetrofitUtil.getApiService(ApiService.class)
                .getImageData(12, page)
                .compose(RetrofitUtil.getComposer())
                .subscribe(imageData -> {
                    canRefresh=true;
//                    imagelist.addAll(imageData.getResults());
                    Log.i("wangyu", "page"+page);
//                    adapter.notifyItemInserted(12 * (page-1));
//                    adapter2.notifyDataSetChanged();
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
}
