package com.example.testwebview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.TextView;

public class NativeActivity extends Activity {
	private TextView mBitmapSize;
	@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_native);
		mBitmapSize = (TextView)findViewById(R.id.bitmap_size);
		String imageLocation = getIntent().getStringExtra("IMAGE_LOCATION");
		if(imageLocation != null){
			
			if(imageLocation.equalsIgnoreCase("xhdpi")){
				mBitmapSize.setBackgroundResource(R.drawable.img_2260_1506_xhdpi);
			}
			else if(imageLocation.equalsIgnoreCase("hdpi")){
				mBitmapSize.setBackgroundResource(R.drawable.img_2260_1506_hdpi);
			}
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		BitmapDrawable drawable = (BitmapDrawable)mBitmapSize.getBackground();
		mBitmapSize.setText("background image size is: "+drawable.getBitmap().getByteCount());
		
	}
}
