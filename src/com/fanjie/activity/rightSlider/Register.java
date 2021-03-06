package com.fanjie.activity.rightSlider;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import jhunplay.com.fanjie.tool.Msharepreference;
import jhunplay.com.fanjie.tool.Tool;
import jhunplay.com.fanjie.tool.Tool.onGetFinish;

import org.json.JSONException;
import org.json.JSONObject;

import com.block.jhguide.R;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Register extends Activity implements OnClickListener {

	private Button my2button1, my2button2;
	private EditText my2edit1, my2edit2, my2edit3, my2edit4;
	private String name, user1, pass1, pass2, a, b, c;
	private Msharepreference preferences;
	private Tool tool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.registe);
		tool = new Tool(this);
		preferences = new Msharepreference(this, "information");
		my2button1 = (Button) findViewById(R.id.l2_button1);
		my2button2 = (Button) findViewById(R.id.l2_button2);
		my2edit1 = (EditText) findViewById(R.id.l2_editText1);
		my2edit2 = (EditText) findViewById(R.id.l2_editText2);
		my2edit3 = (EditText) findViewById(R.id.l2_editText3);
		my2edit4 = (EditText) findViewById(R.id.l2_editText4);
		my2button1.setOnClickListener(this);
		my2button2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.l2_button1:
			finish();
			overridePendingTransition(R.anim.slide_down_in,
					R.anim.slide_down_out);
			break;
		case R.id.l2_button2:
			user1 = my2edit1.getText().toString();
			pass1 = my2edit2.getText().toString();
			pass2 = my2edit3.getText().toString();
			name = my2edit4.getText().toString();
			if (pass1.equals(pass2) && !pass1.equals("")) {
				Judge(user1, pass1, pass2);
			} else if (pass1.equals(pass2) && pass1 == "") {
				Toast.makeText(Register.this, "密码不能为空", 0).show();
			} else if (!pass1.equals(pass2)) {
				my2edit2.setText("");
				my2edit3.setText("");
				Toast.makeText(Register.this, "密码不一致", 0).show();
			}
			break;

		}

	}

	// user_id,login_id,phone_mac,name,password,style
	public void Judge(String a, String b, String c) {
		if (preferences.getStringProperty("user_id").equals("")) {
			UUID uuid = UUID.randomUUID();
			String user_id = uuid.toString();
			preferences.setStringProperty("user_id", user_id);
		}
		String user_id = preferences.getStringProperty("user_id");
		String phone_mac = preferences.getStringProperty("phone_mac");
		int style = 0;
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("ids", "registration");
			jsonObject.put("user_id", user_id);
			jsonObject.put("login_id", a);
			jsonObject.put("phone_mac", phone_mac);
			jsonObject.put("name", name);
			jsonObject.put("password", b);
			jsonObject.put("style", style);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		taskDown down = new taskDown();
		down.execute(jsonObject);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.slide_down_in,
					R.anim.slide_down_out);
		}
		return true;
	}

	public class taskDown extends AsyncTask<JSONObject, Integer, String> {

		@Override
		protected String doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub
			String resullt = Tool.Register(params[0]);
			return resullt;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			String success = null;
			try {
				success = (String) new JSONObject(result).get("msg");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (success.equals("执行成功")) {
				XGPushManager.registerPush(getApplicationContext(),
						new XGIOperateCallback() {

							@Override
							public void onFail(Object arg0, int arg1,
									String arg2) {
								// TODO Auto-generated method stub

								Toast.makeText(Register.this, "注册失败", 0).show();
							}

							@Override
							public void onSuccess(Object arg0, int arg1) {
								// TODO Auto-generated method stub
								finish();
								Toast.makeText(Register.this, "注册成功，请登入", 0)
										.show();
								overridePendingTransition(R.anim.slide_down_in,
										R.anim.slide_down_out);
							}

						});

			} else {
				Toast.makeText(Register.this, "注册失败，请更改用户名", 0).show();
			}
			super.onPostExecute(result);
		}

	}

}
