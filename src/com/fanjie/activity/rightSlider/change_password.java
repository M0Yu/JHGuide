package com.fanjie.activity.rightSlider;

import org.json.JSONException;
import org.json.JSONObject;

import jhunplay.com.fanjie.tool.Msharepreference;
import jhunplay.com.fanjie.tool.Tool;

import com.block.jhguide.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class change_password extends Activity implements OnClickListener {

	private Button my43button1;
	private EditText my43edit1, my43edit2, my43edit3;
	private Msharepreference msharepreference;
	private TextView my43button2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.change_password);
		msharepreference = new Msharepreference(this, "information");
		my43button1 = (Button) findViewById(R.id.l43_button1);
		my43button2 =  (TextView) findViewById(R.id.l43_button2);
		my43edit1 = (EditText) findViewById(R.id.l43_editText1);
		my43edit2 = (EditText) findViewById(R.id.l43_editText2);
		my43edit3 = (EditText) findViewById(R.id.l43_editText3);
		my43button1.setOnClickListener(this);
		my43button2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.l43_button1:
			finish();
			break;
		case R.id.l43_button2:
			String old_pass = my43edit1.getText().toString().trim();
			String password = my43edit2.getText().toString().trim();
			String password2 = my43edit3.getText().toString().trim();
			if (password.equals(password2)) {
				JSONObject jsonObject = Tool.getJSONObject(0, "ids",
						"update_password", "login_id",
						msharepreference.getStringProperty("login_id")+"",
						"password", password, "oldpassword", old_pass);
				ChangeUser user = new ChangeUser();
				user.execute(jsonObject);
			}
			break;

		default:
			break;
		}
	}

	public class ChangeUser extends AsyncTask<JSONObject, Integer, String> {

		@Override
		protected String doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub
			String result = Tool.changePassword(params[0]);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				String success = (String) new JSONObject(result).get("msg");
				if (success.equals("执行成功")) {
					Toast.makeText(change_password.this, success, 0).show();
					finish();
				} else {
					Toast.makeText(change_password.this, success, 0).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		finish();
		return true;
	}

}
