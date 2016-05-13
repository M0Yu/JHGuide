package com.fanjie.activity.rightSlider;

import jhunplay.com.fanjie.tool.Tool;

import org.casey.utils.Msharepreference;
import org.json.JSONObject;

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

public class change_name extends Activity implements OnClickListener {

	private Button my42button1;
	private EditText my42edit;
	private Msharepreference msharepreference;
	private TextView my42button2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.change_name);
		msharepreference = new Msharepreference(this, "information");
		my42button1 = (Button) findViewById(R.id.l42_button1);
		my42button2 = (TextView) findViewById(R.id.l42_button2);
		my42edit = (EditText) findViewById(R.id.l42_editText1);
		my42button1.setOnClickListener(this);
		my42button2.setOnClickListener(this);
	}

	private String new_name;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.l42_button1:
			finish();
			break;
		case R.id.l42_button2:
			new_name = my42edit.getText().toString().trim();
			ChangeName changeName = new ChangeName();
			JSONObject jsonObject = Tool.getJSONObject(0, "name", new_name,
					"login_id", msharepreference.getStringProperty("login_id"),
					"ids", "update_name");
			changeName.execute(jsonObject);
			break;

		default:
			break;
		}
	}

	public class ChangeName extends AsyncTask<JSONObject, Integer, String> {

		@Override
		protected String doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub
			String result = Tool.registPost(params[0]);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (result.contains("昵称修改成功")) {
				msharepreference.setStringProperty("name", new_name);
				finish();
			} else {
				Toast.makeText(change_name.this, "修改失败", 0).show();
			}
			super.onPostExecute(result);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		finish();
		return true;
	}

}
