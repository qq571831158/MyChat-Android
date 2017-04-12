package com.example.apple.mychatqq.activity.discover;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.apple.mychatqq.R;
import com.example.apple.mychatqq.utils.Download;

public class DiscoverFragment extends Fragment {
	private Download download = new Download();
	ImageView imageView = null;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (msg.what ==1){
				Log.e("a","here");
				Bitmap bitmap= (Bitmap)msg.obj;
				imageView.setImageBitmap(BitmapFactory.decodeFile("/storage/emulated/0/friend//my.jpg"));

			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("yao", this.getClass().getName() + " onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e("yao", this.getClass().getName() + " onCreateView");
		return inflater.inflate(R.layout.tab03, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
//		imageView = (ImageView)getActivity().findViewById(R.id.myImage);
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Bitmap bitmap = download.GetImageInputStream("http://182.254.152.99:8080/MyChat1/test/test4.jpg");
//					String a = download.SavaImage(bitmap, Environment.getExternalStorageDirectory()+"/friend/","my");
//					Log.e("a",a);
//					Message message = Message.obtain();
//					message.what = 1;
//					message.obj = bitmap;
//					handler.sendMessage(message);
//				}
//				catch (Exception e){
//					e.printStackTrace();
//				}
//			}
//		}).start();
	}
}
