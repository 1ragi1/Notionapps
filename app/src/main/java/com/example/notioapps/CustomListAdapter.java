package com.example.notioapps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {

    private Context context;
    private List<ListItem> memoList;
    private int resourceLayout;

    public CustomListAdapter(Context context, List<ListItem> memoList, int resourceLayout) {
        this.context = context;
        this.memoList = memoList;
        this.resourceLayout = resourceLayout;
    }

    @Override
    public int getCount() {
        return memoList.size();
    }

    @Override
    public ListItem getItem(int position) {
        return memoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return memoList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resourceLayout, parent, false);
        }
        ListItem item = getItem(position);
        TextView titleView = convertView.findViewById(R.id.title);
        titleView.setText(item.getTitle());
        return convertView;
    }
}
