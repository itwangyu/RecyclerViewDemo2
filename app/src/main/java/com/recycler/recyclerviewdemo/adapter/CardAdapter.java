package com.recycler.recyclerviewdemo.adapter;

import android.content.Context;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.recycler.recyclerviewdemo.R;
import com.recycler.recyclerviewdemo.bean.ImageData;
import com.recycler.recyclerviewdemo.view.ScaleImageView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by WangYu on 2017/7/28.
 */

public class CardAdapter extends BaseAdapter<ImageData.ResultsBean> {
    private HashMap<Integer,Double> heightmap;
    private final int width;
    private static final String TAG = "wangyu";
    public CardAdapter(Context context, List<ImageData.ResultsBean> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        heightmap = new HashMap<>();
    }

    @Override
    protected void convert(ViewHolder holder, ImageData.ResultsBean data,int position) {
        ScaleImageView iv = holder.getView(R.id.imageview);
        //这里进行ui显示
        Glide.with(mContext)
                .load(data.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_normallist;
    }

}
