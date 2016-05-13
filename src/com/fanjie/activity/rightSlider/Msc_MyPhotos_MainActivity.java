package com.fanjie.activity.rightSlider;

import jhunplay.com.fanjie.ovrideLayout.MyImageView_collection;
import jhunplay.com.fanjie.ovrideLayout.MyImageView_collection.OnImageClick;
import jhunplay.com.fanjie.ovrideLayout.MyScrollView_collection;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.block.jhguide.R;
import com.fanjie.activity.thoseyear.ImageDetailsActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class Msc_MyPhotos_MainActivity extends AbActivity implements OnImageClick{

	private ImageButton returnbtn;
	private MyScrollView_collection imageView;
	private AbTitleBar mTitleBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setAbContentView(R.layout.msc_myphotos_activity_main);
		
		mTitleBar = this.getTitleBar();
		MyImageView_collection collection = new MyImageView_collection(this);
		View rightView = mInflater.inflate(R.layout.msc_title_photo, null);
		ImageButton returnBtn = (ImageButton) rightView.findViewById(R.id.returnbutton1);
		returnBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Msc_MyPhotos_MainActivity.this.finish();
			}
		});
		mTitleBar.addRightView(rightView);						
		imageView = (MyScrollView_collection) this.findViewById(R.id.my_scroll_view);		
//		returnbtn = (ImageButton) findViewById(R.id.returnbtn);
//
//		returnbtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Msc_MyPhotos_MainActivity.this.finish();
//			}
//		});
		
//		imageView.setOnImageClickListener(new OnImageClick() {
//
//			@Override
//			public void onImagClick() {
//				// TODO Auto-generated method stub
//
//				overridePendingTransition(R.anim.appear_bottom_right_in,
//						R.anim.disappear_top_left_out);
//			}
//		});

//		imageView.onGetName(new getMyActivity() {
//
//			@Override
//			public Class<? extends Activity> getName() {
//				// TODO Auto-generated method stub
//				return Msc_MyPhotos_ImageDetailsActivity.class;
//			}
//		});
	}

	@Override
	public void onImagClick(Intent intent) {
		// TODO Auto-generated method stub
		startActivity(intent);
		overridePendingTransition(R.anim.appear_bottom_right_in,
		R.anim.disappear_top_left_out);
		
	}


}
