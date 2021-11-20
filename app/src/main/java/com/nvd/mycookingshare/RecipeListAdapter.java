package com.nvd.mycookingshare;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;



import java.util.List;

public class RecipeListAdapter extends BaseAdapter {
    List<Recipe> listData;
    Context context;

    public RecipeListAdapter(List<Recipe> listData,  Context context) {
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
        ImageView anhhorder;
        TextView countryNameView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder = new ViewHolder();
        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.list_item_layout,null);
            holder.anhhorder = (ImageView) convertView.findViewById(R.id.img1);
            holder.countryNameView = (TextView) convertView.findViewById(R.id.txtBuocShow);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();

        }


        holder.countryNameView.setText(listData.get(position).ten);
        //holder.anhhorder.setImageResource(R.drawable.cookbook_logo);
        Picasso.get().load(listData.get(position).img).into(holder.anhhorder);
        return convertView;
    }



}
