package com.nvd.mycookingshare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecipeListsAdapter extends RecyclerView.Adapter<RecipeListsAdapter.ItemHolder> {


    Context context;
    List<Recipe> listData;
    String uid;

    public RecipeListsAdapter(Context context, List<Recipe> listData, String uid) {
        this.context = context;
        this.listData = listData;
        this.uid = uid;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_view,null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Recipe re = listData.get(position);
        holder.txtTen.setText(re.ten);
        Picasso.get().load(listData.get(position).img).into(holder.imgRecipe);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick)
                {
                    Intent intent = new Intent(context, ViewRecipeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("img", re.img);
                    bundle.putString("pos", position+"");
                    bundle.putString("name", re.ten);
                    bundle.putString("uid", uid);
                    bundle.putString("id", re.id);
                    bundle.putStringArrayList("tp",re.chuanbi);
                    bundle.putSerializable("mystep",(Serializable) re.mystep);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(context, ViewRecipeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("img", re.img);
                    bundle.putString("name", re.ten);
                    bundle.putString("uid", uid);
                    bundle.putString("id", re.id);
                    bundle.putStringArrayList("tp",re.chuanbi);
                    bundle.putSerializable("mystep",(Serializable) re.mystep);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        public ImageView imgRecipe;
        public TextView txtTen;
        private ItemClickListener itemClickListener;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            imgRecipe = (ImageView)itemView.findViewById(R.id.imgRecipe);
            txtTen = (TextView)itemView.findViewById(R.id.txtRecipeName);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return true;
        }
    }




}
