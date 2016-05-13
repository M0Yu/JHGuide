package com.fanjie.activity.rightSlider;

import java.util.ArrayList;
import java.util.List;

import jhunplay.com.fanjie.Constant.Constant;
import jhunplay.com.fanjie.tool.Msharepreference;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.block.jhguide.R;

public class SuggestionBack extends AbActivity {
	private ImageButton buttonBack;
	private EditText editTextContent;
	private Button buttonSend;
	private String textContent;
	private Msharepreference Ms;
	private String myText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.view_slider_menu_suggestionback);
		Ms = new Msharepreference(this, "information");
		initView();
		initContent();
	}

	private void initContent() {
		// TODO Auto-generated method stub
		editTextContent = (EditText) this.findViewById(R.id.editTextContent);

		buttonSend = (Button) this.findViewById(R.id.buttonSend);
		buttonSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				textContent = editTextContent.getText().toString();
				if (isNetworkConnected()) {
					if (textContent.equals("")) {
						Toast.makeText(SuggestionBack.this, Constant.SUGGESTION_EMPTY, Toast.LENGTH_LONG).show();
					} else if (!textContent.equals(myText) && !textContent.equals("")) {
						myText = textContent;
						SuggestUp myUp = new SuggestUp();
						myUp.execute(Constant.URL2);
					} else if (textContent.equals(myText)) {
						Toast.makeText(SuggestionBack.this, Constant.SUGGESTION_REPET, Toast.LENGTH_SHORT).show();
					}
					;
				} else {
					Toast.makeText(SuggestionBack.this, "当前网络无法连接", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	// 判断网络是否连接
	public boolean isNetworkConnected() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) this.getSystemService(SuggestionBack.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			return true;
		}
		return false;
	}

	public class SuggestUp extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			String offerSuggestWeb = params[0];
			String result = null;
			String suMsg = null;
			HttpPost httpPost = new HttpPost(offerSuggestWeb);
			List<NameValuePair> parms = new ArrayList<NameValuePair>();

			parms.add(new BasicNameValuePair("ids", "my_opinions"));
			JSONObject json = new JSONObject();
			try {
				json.put("user_id", Ms.getStringProperty("user_id"));
				json.put("opinions", textContent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			parms.add(new BasicNameValuePair("params", json.toString()));
			parms.add(new BasicNameValuePair("sessionkey", Ms.getStringProperty("sessionkey")));

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(parms, HTTP.UTF_8));
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse response = httpClient.execute(httpPost);
				if (response.getStatusLine().getStatusCode() == 200) {
					result = EntityUtils.toString(response.getEntity(), "UTF-8");
					suMsg = (String) new JSONObject(result).get("msg");

				}
				Message msg = new Message();
				msg.obj = suMsg;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			if (msg.obj.toString().equals("执行成功")) {
				Toast.makeText(SuggestionBack.this, Constant.SUGGESTION_SUBMIT_SUCCESS, Toast.LENGTH_LONG).show();
				finish();
				overridePendingTransition(R.anim.appear_in, R.anim.appear_out);
			} else {
				Toast.makeText(SuggestionBack.this, Constant.SUGGESTION_SUBMIT_FAIL, Toast.LENGTH_LONG).show();
			}
		}
	};

	// ------------------------鏈畬寰呯画------------------------------
	private void initView() {
		// TODO Auto-generated method stub
		// AbTitleBar mAbTitleBar = this.getTitleBar();
		// mAbTitleBar.setTitleText("鎰忚鍙嶉");
		// // mAbTitleBar.setLogo(R.drawable.return_lw_01);
		// mAbTitleBar.setLogo(R.drawable.button_selector_back);
		// mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		// // mAbTitleBar.setTitleBarBackground(R.drawable.navegationbar);
		// mAbTitleBar.setTitleTextMargin(140, 0, 0, 0);

		AbTitleBar mAbTitleBar = this.getTitleBar();
		View view = mInflater.inflate(R.layout.titlebar_lw, null);
		buttonBack = (ImageButton) view.findViewById(R.id.buttonBack);
		buttonBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.appear_in, R.anim.appear_out);
			}
		});
		mAbTitleBar.addView(view);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.appear_in, R.anim.appear_out);
		}
		return true;
	}

}
