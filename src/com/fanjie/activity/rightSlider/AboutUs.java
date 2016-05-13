package com.fanjie.activity.rightSlider;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.block.jhguide.R;

public class AboutUs extends AbActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.view_slider_menu_aboutus);
		initView();
	}

	private void initView() {

		AbTitleBar mAbTitlerBar = this.getTitleBar();
		View view = mInflater.inflate(R.layout.titlebar_aboutus_lw, null);
		mAbTitlerBar.addView(view);
		ImageButton aboutUsBackButton = (ImageButton) this.findViewById(R.id.buttonBack_aboutUs);
		aboutUsBackButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.in_translate_top, R.anim.slide_down_out);
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.in_translate_top, R.anim.slide_down_out);
		}
		
		return super.onKeyDown(keyCode, event);
	}

}
