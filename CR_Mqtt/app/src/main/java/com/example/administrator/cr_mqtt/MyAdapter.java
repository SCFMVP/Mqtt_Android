package com.example.administrator.cr_mqtt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Bean> mDatas;
//自定义适配器，继承自BaseAdapter
    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public MyAdapter(Context context, List<Bean> datas) {

        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    //返回数据集的长度
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //这个方法才是重点，我们要为它编写一个ViewHolder
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_listview, parent, false); //加载布局
            holder = new ViewHolder();

            holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
            holder.descTv = (TextView) convertView.findViewById(R.id.descTv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
            holder.phoneTv = (TextView) convertView.findViewById(R.id.phoneTv);

            convertView.setTag(holder);
        } else {   //else里面说明，convertView已经被复用了，说明convertView中已经设置过tag了，即holder
            holder = (ViewHolder) convertView.getTag();
        }

        Bean bean = mDatas.get(position);
        holder.titleTv.setText(bean.getTitle());
        holder.descTv.setText(bean.getDesc());
        holder.timeTv.setText(bean.getTime());
        holder.phoneTv.setText(bean.getPhone());

        return convertView;
    }

    //这个ViewHolder只能服务于当前这个特定的adapter，因为ViewHolder里会指定item的控件，不同的ListView，item可能不同，所以ViewHolder写成一个私有的类
    private class ViewHolder {
        TextView titleTv;
        TextView descTv;
        TextView timeTv;
        TextView phoneTv;
    }
}