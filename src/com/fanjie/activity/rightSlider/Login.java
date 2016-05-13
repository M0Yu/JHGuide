package com.fanjie.activity.rightSlider;

import jhunplay.com.fanjie.ovrideLayout.MyScrollView;
import jhunplay.com.fanjie.tool.Msharepreference;
import jhunplay.com.fanjie.tool.Tool;
import jhunplay.com.fanjie.tool.Tool.onGetFinish;

import com.block.fragment.content.CommunityNews;
import com.block.jhguide.R;
import com.fanjie.activity.rightSlider.Login;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener, onGetFinish {

	private Button my1button1;
	private Button my1button2;
	private Button my1button3;
	private EditText my1edit1;
	private EditText my1edit2;
	private ImageView my1image1;
	private String user;
	private String pass;
	private RelativeLayout layout;
	private Tool tool;
	private Msharepreference preferences;
	private ImageView my1image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_log_in);
		// layout = (RelativeLayout) this.findViewById(R.id.login);
		// Animation anim = AnimationUtils.loadAnimation(this,
		// R.anim.flip_horizontal_in);
		// layout.startAnimation(anim);

		tool = new Tool(this);
		preferences = new Msharepreference(this, "information");
		my1button1 = (Button) findViewById(R.id.log_in1_button1);
		my1button2 = (Button) findViewById(R.id.log_in1_button2);
		my1button3 = (Button) findViewById(R.id.log_in1_button3);
		my1image = (ImageView) findViewById(R.id.log_in1_imageView1);
		my1edit1 = (EditText) findViewById(R.id.log_in1_editText1);
		my1edit2 = (EditText) findViewById(R.id.log_in1_editText2);
		my1button1.setOnClickListener(this);
		my1button2.setOnClickListener(this);
		my1button3.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.log_in1_button1:
			finish();
			overridePendingTransition(R.anim.flip_horizontal_in,
					R.anim.flip_horizontal_out);
			break;

		case R.id.log_in1_button2:
			tool.createLoadingDialog(Login.this, "正在登录中...").show();;
			String login_id = my1edit1.getText().toString().trim();
			String password = my1edit2.getText().toString().trim();
			log_in(login_id, password);
			break;

		case R.id.log_in1_button3:
			Intent intent = new Intent();
			intent.setClass(Login.this, Register.class);
			Login.this.startActivity(intent);
			overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
			break;

		default:
			break;
		}
	}

	public void log_in(String login_id, String password) {
		/* �ж��˺��������Ƿ��Ӧ */

		tool.Login(login_id, password);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.flip_horizontal_in,
					R.anim.flip_horizontal_out);
		}
		return true;
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		XGPushManager.registerPush(getApplicationContext(),
				new XGIOperateCallback() {

					@Override
					public void onFail(Object arg0, int arg1, String arg2) {
						// TODO Auto-generated method stub
						tool.reMoveMyDialog();
						Toast.makeText(getApplicationContext(), "登入失败", 0)
								.show();
					}

					@Override
					public void onSuccess(Object arg0, int arg1) {
						// TODO Auto-generated method stub
						tool.reMoveMyDialog();
						Intent intent = new Intent();
						intent.setClass(Login.this, MyInformation.class);
						Login.this.startActivity(intent);
						finish();
						overridePendingTransition(R.anim.left_in,
								R.anim.left_out);
					}

				});

	}

	public Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

}
