package com.recycler.recyclerviewdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.recycler.recyclerviewdemo.R;
import com.recycler.recyclerviewdemo.ScaleImageView;
import com.recycler.recyclerviewdemo.bean.ImageData;

import java.util.HashMap;
import java.util.List;

/**
 * Created by WangYu on 2017/7/28.
 */

public class NormalListAdapter extends BaseAdapter<ImageData.ResultsBean> {
    private HashMap<Integer,Double> heightmap;
    private final int width;
    private static final String TAG = "wangyu";
    public NormalListAdapter(Context context, List<ImageData.ResultsBean> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        heightmap = new HashMap<>();
    }

    @Override
    protected void convert(ViewHolder holder, ImageData.ResultsBean data,int position) {
        ScaleImageView iv = holder.getView(R.id.imageview);
        if (heightmap.containsKey(position)) {
            iv.setInitSize(width/2,(int) (width / 2 / heightmap.get(position)));
        }
        //这里进行ui显示
        Glide.with(mContext)
                .load(data.getUrl())
                .asBitmap()
                .placeholder(R.mipmap.empty)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (!heightmap.containsKey(position)) {
                            int width = resource.getWidth();
                            int height = resource.getHeight();
                            double bili=width*1.0f/height;
//                            Log.i("wangyu", position+":"+bili);
                            heightmap.put(position,bili);
                            iv.setInitSize(width/2,(int) (width / 2 / bili));
                        }
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 2) {
            TextView tv= ((ViewHolder) holder).getView(R.id.tv);
            tv.setText("这是第"+position+"个数据");
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            View inflate = View.inflate(mContext, R.layout.item_type2, null);
            return new MyViewHolder(inflate);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_normallist;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.i("wangyu", "onViewAttachedToWindow: "+holder.getItemId());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.i(TAG, "onAttachedToRecyclerView: "+recyclerView.toString());
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0&&!isFooterView(position)) {
            return 2;
        }
        return super.getItemViewType(position);
    }

    public class MyViewHolder extends ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

}
