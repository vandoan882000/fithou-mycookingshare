package com.nvd.mycookingshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyStepListAdapter extends BaseAdapter {
    List<MyStep> listData;
    Context context;

    public MyStepListAdapter(List<MyStep> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imgBuoc;
        TextView txtBuoc,txtMieuTa;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        MyStepListAdapter.ViewHolder holder = new MyStepListAdapter.ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_layout, null);
            holder.imgBuoc = (ImageView) convertView.findViewById(R.id.img1);
            holder.txtMieuTa = (TextView) convertView.findViewById(R.id.txtBuocShow);
            holder.txtBuoc = (TextView) convertView.findViewById(R.id.txtSoBuoc);
            int buoc = position +1 ;
            holder.txtBuoc.setText("- Bước "+buoc+" :");
            convertView.setTag(holder);
        } else {
            holder = (MyStepListAdapter.ViewHolder) convertView.getTag();
        }
        holder.txtMieuTa.setText(listData.get(position).step);
        //holder.anhhorder.setImageResource(R.drawable.cookbook_logo);
        Picasso.get().load(listData.get(position).imgStep).into(holder.imgBuoc);

        return convertView;
    }

}