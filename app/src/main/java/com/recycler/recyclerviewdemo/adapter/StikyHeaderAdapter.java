package com.recycler.recyclerviewdemo.adapter;

import android.content.Context;
import android.widget.TextView;

import com.recycler.recyclerviewdemo.R;
import com.recycler.recyclerviewdemo.bean.Goods;

import java.util.List;

/**
 * Created by WangYu on 2017/8/2.
 */

public class StikyHeaderAdapter extends BaseAdapter<Goods> {

    public StikyHeaderAdapter(Context context, List<Goods> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected void convert(ViewHolder holder, Goods data, int position) {
        TextView tv = holder.getView(R.id.tv);
        tv.setText(data.name);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_stikyheader;
    }
}
