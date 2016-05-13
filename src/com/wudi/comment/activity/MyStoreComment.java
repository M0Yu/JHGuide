package com.wudi.comment.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jhunpaly.com.zmj.network.judge.NetWorkJudgeTool;
import jhunplay.com.fanjie.Constant.Constant;
import jhunplay.com.fanjie.tool.Msharepreference;
import jhunplay.com.fanjie.tool.Tool;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.view.titlebar.AbTitleBar;
import com.block.jhguide.R;
import com.fanjie.activity.rightSlider.Login;

@SuppressLint("NewApi")
public class MyStoreComment extends Activity {

	private RatingBar myCommentRatingBar;
	private AbTitleBar abTitleBar = null;
	private EditText myStoreCommentEditText;
	private String sendString;
	private float sendScore;
	private Msharepreference sharedPreferences;
	private Tool tool;
	private Button backToUserCommentButton;
	private Button sendButton;
	private NetWorkJudgeTool myStoreCommentnetJudge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wudi_store_mycomment_titlebar_detial);
		tool = new Tool(this);
		myStoreCommentEditText = (EditText) findViewById(R.id.mySotrecommentEditText);
		myCommentRatingBar = (RatingBar) findViewById(R.id.myCommentRatingBar);


		backToUserCommentButton = (Button) findViewById(R.id.backToStoreCommentButton);
		sendButton = (Button) findViewById(R.id.sendCommentButton);

		backToUserCommentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MyStoreComment.this.finish();
			}
		});

		myCommentRatingBar
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar arg0, float arg1,
							boolean arg2) {
						// TODO Auto-generated method stub
						sendScore = arg1;
					}
				});

		//给发送按钮添加一个网络判断
		myStoreCommentnetJudge = new NetWorkJudgeTool();
		if(myStoreCommentnetJudge.isNetWorkConnected(MyStoreComment.this)){
			
			if(!myStoreCommentnetJudge.isMobileConnected(MyStoreComment.this)
					&& !myStoreCommentnetJudge.isWifiConnected(MyStoreComment.this)){
				Toast.makeText(MyStoreComment.this, "当前网络连接不可用", Toast.LENGTH_SHORT);
				}else {
					sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// 获取 user_id 和 user_name
				sharedPreferences = new Msharepreference(MyStoreComment.this,
						"information");
				String sessionkey = sharedPreferences
						.getStringProperty("sessionkey");
				if (sessionkey == "") {
					tool.showDialog(Login.class, "您未登入", "请您登入或注册");
					return;

				}
				if (sharedPreferences.getStringProperty("user_id").equals("")) {
					UUID uuid = UUID.randomUUID();
					String user_id = uuid.toString();
					sharedPreferences.setStringProperty("user_id", user_id);
					Constant.user = true;
				}
				String user_name = sharedPreferences.getStringProperty("name");
				String user_id = sharedPreferences.getStringProperty("user_id");
				String content = myStoreCommentEditText.getText().toString();
				float level = sendScore;

				// 从 comment获取 Store_id 为 excute的参数 做准备
				Intent intentFromCommentDetail = MyStoreComment.this
						.getIntent();
				Bundle bundleFromCommentDetail = new Bundle();
				bundleFromCommentDetail = intentFromCommentDetail
						.getBundleExtra("storeIdBundle");
				int store_id = bundleFromCommentDetail.getInt("store_id");

				ZmjUserCommentUpdateAsyncTask updateComment = new ZmjUserCommentUpdateAsyncTask();
				JSONObject js = new JSONObject();
				try {
					js.put("store_id", store_id);
					js.put("user_id", user_id + "");
					js.put("user_name", user_name + "");
					js.put("content", content + "");
					js.put("level", level + "");
					js.put("type", 0);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				updateComment.execute("commont_store", js.toString());

			}
		});
					}
			}else{
				tool.showNetDialog("未连接网络", "是否连接网络(亲~没有网络您的评论将不能被发送哦~)");
				}
		}

	public class ZmjUserCommentUpdateAsyncTask extends
			AsyncTask<String, Integer, String> {

		private String url = "http://61.142.71.63:9090/VCIS/controller.do?execute";

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlResult = null;
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost post = new HttpPost(url);
				List<NameValuePair> parms = new ArrayList<NameValuePair>();
				parms.add(new BasicNameValuePair("ids", params[0]));
				parms.add(new BasicNameValuePair("params", params[1]));
				parms.add(new BasicNameValuePair("register", "true"));
				post.setEntity(new UrlEncodedFormEntity(parms, HTTP.UTF_8));
				HttpResponse response = httpClient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					urlResult = EntityUtils.toString(response.getEntity(),
							"UTF-8");

				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return urlResult;

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {
			Boolean success = null;

			try {
				success = (Boolean) new JSONObject(result).get("success");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (success) {

				Toast.makeText(getApplicationContext(), "已发送", 1000).show();
				// 返回评论界面 之所以要 intent 是为了 在 comment中重新 调用 refresh（） 重新刷新数据
				Intent intent = new Intent();
				intent.setClass(MyStoreComment.this, Commentdetail.class);
				MyStoreComment.this.setResult(1, intent);
				MyStoreComment.this.finish();

			}

		}

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (tool.haveShowDialog()) {
			tool.removeDialog();
		}
	}

}
