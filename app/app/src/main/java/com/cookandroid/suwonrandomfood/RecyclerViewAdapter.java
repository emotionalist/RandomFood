package com.cookandroid.suwonrandomfood;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    ArrayList<Store> stores;

    public RecyclerViewAdapter(ArrayList<Store> stores){ this.stores = stores; }

    public void setFilteredList(ArrayList<Store> filteredList){
        this.stores = filteredList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, desc, star;
        public ImageView img;
        public LinearLayout container;

        public MyViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.listtitle);
            desc = (TextView) view.findViewById(R.id.listdescribe);
            star = (TextView) view.findViewById(R.id.liststar);
            img = (ImageView) view.findViewById(R.id.listimage);
            container = (LinearLayout) view.findViewById(R.id.layout_container);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(holder.itemView).load(stores.get(position).getImg()).into(holder.img);
        holder.name.setText(stores.get(position).getName());
        holder.desc.setText(stores.get(position).getDesc());
        holder.star.setText(stores.get(position).getStar());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mPosition = holder.getAdapterPosition();
                Context context = v.getContext();

                Intent intent = new Intent(context, StoreActivity.class);
                intent.putExtra("sname", stores.get(mPosition).name);
                intent.putExtra("sdesc", stores.get(mPosition).desc);
                intent.putExtra("snum", stores.get(mPosition).num);
                intent.putExtra("sstar", stores.get(mPosition).star);
                intent.putExtra("simg", stores.get(mPosition).img);
                intent.putExtra("ec", stores.get(mPosition).ec);
                intent.putExtra("wc", stores.get(mPosition).wc);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return stores.size(); }
}

