package com.example.apple.mychatqq.activity.contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.mychatqq.R;
import com.example.apple.mychatqq.model.ChattingModel;
import com.example.apple.mychatqq.model.UserinfoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2017/4/4.
 */

public class ChattingAdapter extends BaseAdapter {
    private static List<ChattingModel>list = new ArrayList<>();
    public static final int FROM_WHO =1;
    public static final int TO_OTHER = 0;
    public static final String TAG ="ChattingAdapter";
    private Context context;
    public ChattingAdapter(Context context, ChattingModel as) {
        this.context = context;
        list.add(as);
    }

    @Override
    public int getItemViewType(int position) {
        int result = 0;
        if (list.get(position).getType()== FROM_WHO){
            result = 1;
        }
        else if (list.get(position).getType() == TO_OTHER){
            result = 0;
        }
        return result;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 viewHolder1 = null;
        ViewHolder2 viewHolder2 = null;
        int type = getItemViewType(position);
        if (convertView==null) {
            viewHolder1 = new ViewHolder1();
            viewHolder2 = new ViewHolder2();
            switch (type) {
                case TO_OTHER:
                    convertView = View.inflate(context, R.layout.chattingcelltoother, null);
                    viewHolder1.textView = (TextView) convertView.findViewById(R.id.layout_chattingcelltoother_text);
                    viewHolder1.imageView = (ImageView) convertView.findViewById(R.id.layout_chattingcelltoother_image);
                    convertView.setTag(R.id.tag_first, viewHolder1);
                    break;
                case FROM_WHO:
                    convertView = View.inflate(context, R.layout.chattingcellfromwho, null);
                    viewHolder2.textView = (TextView) convertView.findViewById(R.id.layout_chattingcellfromwho_text);
                    viewHolder2.imageView = (ImageView) convertView.findViewById(R.id.layout_chattingcellfromwho_image);
                    convertView.setTag(R.id.tag_second, viewHolder2);
                    break;
            }
        }else {
            switch (type) {
                case TO_OTHER:
                    viewHolder1 = (ViewHolder1) convertView.getTag(R.id.tag_first);
                    break;
                case FROM_WHO:
                    viewHolder2 = (ViewHolder2) convertView.getTag(R.id.tag_second);
                    break;
            }
        }
        ChattingModel chattingModel = list.get(position);
        switch (type){
            case TO_OTHER:
                viewHolder1.textView.setText(chattingModel.getMessage());
                viewHolder1.imageView.setImageBitmap(BitmapFactory.decodeFile(chattingModel.getImage()));
                break;
            case FROM_WHO:
                viewHolder2.textView.setText(chattingModel.getMessage());
                viewHolder2.imageView.setImageBitmap(BitmapFactory.decodeFile(chattingModel.getImage()));
                break;
        }
        return convertView;
    }

    private static class ViewHolder1 {
        TextView textView;
        ImageView imageView;
    }

    /**
     * item B 的Viewholder
     */
    private static class ViewHolder2 {
        TextView textView;
        ImageView imageView;
    }

    public static class ViewDismissReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            list.clear();
            Log.e(TAG,"数据源清理完毕");
        }
    }

}
