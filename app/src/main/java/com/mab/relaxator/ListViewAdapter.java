package com.mab.relaxator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private String[] name;
    private String[] link;
    private LayoutInflater inflater;

    public ListViewAdapter(Context a_context , String[] a_name , String[] a_link){
        this.context = a_context;
        this.name = a_name;
        this.link = a_link;

    }

    @Override
    public int getCount() {

        return name.length;

    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        TextView tv_name;         //adi_textview
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.menusatir, parent , false);

        tv_name = (TextView) itemView.findViewById(R.id.menuName);
        tv_name.setText(name[position]);

        return itemView;
    }
}
