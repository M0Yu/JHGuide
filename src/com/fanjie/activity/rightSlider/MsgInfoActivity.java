package com.fanjie.activity.rightSlider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.block.jhguide.R;
import com.tencent.android.tpush.XGPushManager;

public class MsgInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_msginfo);
	   //设置标题
		ImageButton backButton = (ImageButton) this.findViewById(R.id.message_Info_Title_Backbar_one);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
			
		TextView titleTextView = (TextView)this.findViewById(R.id.message_Info_Title_TextView_one);
		titleTextView.setText("消息详情");
		

		Bundle xgnotification = this.getIntent().getExtras();
//		getSupportActionBar()
//				.setTitle("ID:" + xgnotification.getLong("msg_id"));
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().show();

		TextView textView = (TextView) this.findViewById(R.id.title);
		textView.setText(xgnotification.getString("title"));
		textView = (TextView) this.findViewById(R.id.content);
		textView.setText(xgnotification.getString("content"));
		textView = (TextView) this.findViewById(R.id.update_time);
		textView.setText("到达时间：" + xgnotification.getString("update_time"));
		textView = (TextView) this.findViewById(R.id.activityType);
		TextView textViewContent = (TextView) this
				.findViewById(R.id.activityContent);
		if (xgnotification.getInt("notificationActionType", 0) == 1) {
			textView.setText("特定页面：");
		} else if (xgnotification.getInt("notificationActionType", 0) == 2) {
			textView.setText(" URL：");
		} else if (xgnotification.getInt("notificationActionType", 0) == 3) {
			textView.setText("Intent:");
		}
		textViewContent.setText(xgnotification.getString("activity"));

	}

	@Override
	protected void onResume() {
		super.onResume();
		XGPushManager.onActivityStarted(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		XGPushManager.onActivityStoped(this);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.other, menu);
	// return true;
	// }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent mainIntent = new Intent(this, MessageActivity.class);
			mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startActivity(mainIntent);
			break;
		// case R.id.action_device_token:
		// Intent deviceIntent = new Intent(this, DeviceActivity.class);
		// this.startActivity(deviceIntent);
		// break;
		// case R.id.action_help_center:
		// Intent helpIntent = new Intent(this, HelpActivity.class);
		// this.startActivity(helpIntent);
		// break;
		// case R.id.action_about_us:
		// Intent aboutIntent = new Intent(this, AboutActivity.class);
		// this.startActivity(aboutIntent);
		// break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
