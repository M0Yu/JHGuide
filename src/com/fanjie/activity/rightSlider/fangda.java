package com.fanjie.activity.rightSlider;

import com.block.jhguide.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class fangda extends Activity implements OnClickListener{

	private ImageView myimage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fangda);
		myimage = (ImageView)findViewById(R.id.fangda_imageView1);
		myimage.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}

}
