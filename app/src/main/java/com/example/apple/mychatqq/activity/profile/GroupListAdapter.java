package com.example.apple.mychatqq.activity.profile;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.mychatqq.R;

import java.util.List;
import java.util.Map;

/**
 * Created by apple on 2017/4/6.
 */

public class GroupListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Map<String, Object>> listData;
    private List<Map<String, Object>> splitData;

    public GroupListAdapter(Context context, List<Map<String, Object>> listData, List<Map<String, Object>> splitData) {

        this.mInflater = LayoutInflater.from(context);

        this.listData = listData;

        this.splitData = splitData;

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

    @Override

    public boolean isEnabled(int position) {

        if (splitData.contains(listData.get(position))) {

            return false;

        }

        return super.isEnabled(position);

    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {

        if (splitData.contains(listData.get(position))) {
            convertView = mInflater.inflate(R.layout.tag, null);

        } else {
            convertView = mInflater.inflate(R.layout.profilecell, null);
            TextView textView = (TextView) convertView.findViewById(R.id.layout_profilecell_text);
            textView.setText(listData.get(position).get("title").toString());
            ImageView imageView = (ImageView) convertView.findViewById(R.id.layout_profilecell_image);
            imageView.setImageResource(Integer.parseInt(listData.get(position).get("image").toString()));
        }
        return convertView;
    }
}
