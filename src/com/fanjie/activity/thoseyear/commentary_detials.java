package com.fanjie.activity.thoseyear;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import jhunplay.com.fanjie.Constant.Constant;
import jhunplay.com.fanjie.tool.Msharepreference;
import jhunplay.com.fanjie.tool.Tool;
import android.R.string;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.block.jhguide.R;
import com.fanjie.activity.rightSlider.Login;

public class commentary_detials extends AbActivity {

	private EditText comEditText;
	private Tool mTool;
	private Msharepreference msharepreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.lyz_commentary_detials);
		initview();
		TitleSet();

	}

	private void TitleSet() {
		AbTitleBar mAbTitleBar = this.getTitleBar();
		View view = mInflater.inflate(R.layout.lyz_titlebar_detial, null);
		mAbTitleBar.addView(view);

		ImageButton mButton = (ImageButton) view
				.findViewById(R.id.imagebutton);
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		TextView mButton2 = (TextView) view.findViewById(R.id.sendButton);
		mButton2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (msharepreference.getStringProperty("sessionkey").equals("")) {
					mTool.showDialog(Login.class, "您还未登入", "请您登入或注册");
					return;
				}
				String content = comEditText.getText().toString();
				ToolManager(content);

			}
		});
	}

	private String store_id;

	private void initview() {
		Intent intent = getIntent();
		store_id = intent.getStringExtra("store_id");
		mTool = new Tool(this);
		msharepreference = new Msharepreference(this, "information");
		comEditText = (EditText) findViewById(R.id.commentaryText);
	}

	public class MyTask extends AsyncTask<JSONObject, Void, String> {

		@SuppressWarnings("static-access")
		@Override
		protected String doInBackground(JSONObject... arg0) {
			// TODO Auto-generated method stub
			String result = mTool.registPost(arg0[0]);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				JSONObject jsobj = new JSONObject(result);
				if (jsobj.get("msg").equals("执行成功")) {
					Toast.makeText(getApplicationContext(), "已发送", 0).show();
					Intent intent = new Intent();
					intent.setClass(commentary_detials.this, commentary.class);
					commentary_detials.this.setResult(1, intent);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "发送失败", 0).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void ToolManager(String content) {
		if (msharepreference.getStringProperty("user_id").equals("")) {
			UUID uuid = UUID.randomUUID();
			String user_id = uuid.toString();
			msharepreference.setStringProperty("user_id", user_id);
			Constant.user = true;
		}
		MyTask task = new MyTask();
		JSONObject params = new JSONObject();
		try {
			
			params.put("ids", "commont_store");
			params.put("store_id", Integer.parseInt(store_id));
			params.put("user_id", msharepreference.getStringProperty("user_id"));
			params.put("user_name", msharepreference.getStringProperty("name"));
			params.put("content", content);
			params.put("level", 0);
			params.put("type", 1);
			task.execute(params);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
