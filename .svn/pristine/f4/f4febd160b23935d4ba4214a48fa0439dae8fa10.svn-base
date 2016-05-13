package com.fanjie.activity.rightSlider;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.block.jhguide.R;

public class UserInfo extends AbActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.view_slider_menu_userinfo);
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.left_in);
		
		initView();
	}
	private void initView() {
		AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText("首页");
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);// TODO Auto-generated method stub
		
	}
}
