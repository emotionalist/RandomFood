package com.cookandroid.suwonrandomfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StoreAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Menu> menu;

    public StoreAdapter(Context context, ArrayList<Menu> data){
        mContext = context;
        menu = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return menu.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return menu.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.item_store_menu, null);
        TextView name = view.findViewById(R.id.item_menu_name);
        TextView price = view.findViewById(R.id.item_menu_price);
        name.setText(menu.get(position).getName());
        price.setText(menu.get(position).getPrice());

        return view;
    }
}
