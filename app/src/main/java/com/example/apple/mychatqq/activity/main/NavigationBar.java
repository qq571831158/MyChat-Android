package com.example.apple.mychatqq.activity.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.View;
import android.widget.TextView;

import com.example.apple.mychatqq.R;

/**
 * Created by apple on 2017/3/30.
 */

public class NavigationBar extends RelativeLayout implements View.OnClickListener {
    public NavigationBar(Context context){
        this(context,null);
    }
    private ImageView searchBtn ;
    private ImageView addBtn;
    private TextView textView;
    public NavigationBar(Context context, AttributeSet attrs){
        super(context,attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.customactionbar,this,true);
        searchBtn = (ImageView)findViewById(R.id.searchBtn);
        addBtn = (ImageView)findViewById(R.id.addBtn);
        textView = (TextView)findViewById(R.id.appname);
        searchBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
    }

    public ImageView getSearchBtn(){
        return searchBtn;
    }

    public ImageView getAddBtn(){
        return addBtn;
    }

    public TextView getTextView(){
        return textView;
    }

    public void setTitle(String title) {
        this.textView.setText(title);
    }

    private ClickCallback callback;

    public void setCliclCallback(ClickCallback cliclCallback){
        this.callback = cliclCallback;
    }
    public static interface ClickCallback{
        void onSearchClick();
        void onAddClick();
    }
    @Override
    public void onClick(View view){
        int id = view.getId();
        if (id == R.id.searchBtn){
            callback.onSearchClick();
            return;
        }
        if (id == R.id.addBtn){
            callback.onAddClick();
            return;
        }
    }
}
