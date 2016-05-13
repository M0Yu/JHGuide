package com.fanjie.activity.rightSlider;

import jhunplay.com.fanjie.ovrideLayout.ZoomImageView;

import com.block.jhguide.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

public class Msc_MyPhotos_ImageDetailsActivity extends Activity {

	private ZoomImageView zoomImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.msc_photos_image_details);
		zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);
		String imagePath = getIntent().getStringExtra("image_path");
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

		zoomImageView.setImageBitmap(bitmap);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.appear_top_left_in, R.anim.disappear_bottom_right_out);
			
		}
		return true;
	}
}
