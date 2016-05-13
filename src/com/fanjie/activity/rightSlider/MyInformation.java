package com.fanjie.activity.rightSlider;

import java.security.PublicKey;

import jhunplay.com.fanjie.tool.Msharepreference;
import jhunplay.com.fanjie.tool.Tool;

import org.json.JSONException;
import org.json.JSONObject;

import com.block.jhguide.R;
import com.block.jhguide.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyInformation extends Activity implements OnClickListener {

	private RelativeLayout layout;
	private Msharepreference msharepreference;
	private Button my3button;
	private ImageView my3image;
	private TextView my3text;
	private LinearLayout my3linear1, my3linear2, my3linear3, my3linear4,
			my3linear5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.person_information);
		msharepreference = new Msharepreference(this, "information");

		LayoutInflater inflater = LayoutInflater.from(this);
		my3button = (Button) findViewById(R.id.l3_button1);
		my3image = (ImageView) findViewById(R.id.l3_imageView1);
		my3text = (TextView) findViewById(R.id.l3_textView7);

		my3linear1 = (LinearLayout) findViewById(R.id.l3_linearLayout1);
		my3linear2 = (LinearLayout) findViewById(R.id.l3_linearLayout2);
		my3linear3 = (LinearLayout) findViewById(R.id.l3_linearLayout3);
		my3linear4 = (LinearLayout) findViewById(R.id.l3_linearLayout4);
		my3linear5 = (LinearLayout) findViewById(R.id.l3_linearLayout5);
		my3button.setOnClickListener(this);
//		my3image.setOnLongClickListener(this);
		my3linear1.setOnClickListener(this);
		my3linear2.setOnClickListener(this);
		my3linear3.setOnClickListener(this);
		my3linear4.setOnClickListener(this);
		my3linear5.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.l3_button1:
			finish();
			overridePendingTransition(R.anim.right_in, R.anim.right_out);
			break;
		case R.id.l3_linearLayout1:
			Intent it = new Intent(MyInformation.this, Msc_Comment.class);
			startActivity(it);
			overridePendingTransition(R.anim.in_scale_x, R.anim.out_scale_x);
			break;
		case R.id.l3_linearLayout2:
			Intent mystoreintent = new Intent(MyInformation.this,
					Msc_MyStore_MainActivity.class);
			startActivity(mystoreintent);
			break;
		case R.id.l3_linearLayout3:
			Intent intent2 = new Intent(MyInformation.this,
					Msc_MyPhotos_MainActivity.class);
			startActivity(intent2);
			break;
		case R.id.l3_linearLayout5:
			msharepreference.setStringProperty("sessionkey", "");// sessionkey
			msharepreference.setStringProperty("user_id", "");
			String a = msharepreference.getStringProperty("sessionkey");
			if (msharepreference.getStringProperty("sessionkey").equals("")
					&& msharepreference.getStringProperty("user_id").equals("")) {
				Toast.makeText(this, "注销成功", 0).show();
				Intent it1 = new Intent(MyInformation.this, Login.class);
				startActivity(it1);
				finish();
			}
			break;

		case R.id.l3_linearLayout4:
			intent.setClass(MyInformation.this, Change.class);
			startActivity(intent);
			finish();
			break;
		}

		// activity3.this.startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.right_in, R.anim.right_out);
		}

		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		String name = msharepreference.getStringProperty("name");
		my3text.setText(name);
	}

}
