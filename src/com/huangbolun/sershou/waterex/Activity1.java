package com.huangbolun.sershou.waterex;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.w3c.dom.Text;

import com.block.jhguide.R;
import com.lidroid.xutils.BitmapUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity1 extends Activity {
	TextView ViewObject_name;
	TextView ViewType;
	TextView ViewMsg;
	TextView ViewTel_type;
	TextView ViewTel;
	ImageView show;
	Button backButton;
	private BitmapUtils bitmapUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.huangbolun_sundries_detail);

		ViewObject_name = (TextView) findViewById(R.id.sundriesName);
		ViewType = (TextView) findViewById(R.id.sundriesType);
		ViewMsg = (TextView) findViewById(R.id.sundries_detail);
		ViewTel_type = (TextView) findViewById(R.id.tel_type);
		ViewTel = (TextView) findViewById(R.id.telphone);
		show = (ImageView) findViewById(R.id.sundriesImage);
		backButton = (Button) findViewById(R.id.sundriesDetailback);
		Intent intent = getIntent();
		String msg = intent.getStringExtra("msg");
		String object_name = intent.getStringExtra("object_name");
		String tel = intent.getStringExtra("tel");
		String tel_type = intent.getStringExtra("tel_type");
		String type = intent.getStringExtra("type");
		String sub_time = intent.getStringExtra("sub_time");
		String img = intent.getStringExtra("img");
		ViewObject_name.setText(object_name);
		ViewType.setText(type);
		ViewMsg.setText(msg);
		ViewTel_type.setText(tel_type);
		ViewTel.setText(tel);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		bitmapUtils = new BitmapUtils(Activity1.this);
		bitmapUtils.display(show, img);

	}

}
