package com.cx.second_deal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jhunplay.com.fanjie.tool.Msharepreference;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbBottomBar;
import com.ab.view.titlebar.AbTitleBar;
import com.block.jhguide.R;
import com.lidroid.xutils.BitmapUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SundriesDetail extends AbActivity {
	private ImageView sundriesImage;
	private TextView sundries_name;
	private TextView sundries_detail;
	private TextView telphone;
	private TextView sundriesType;
	private TextView tel_type;
	private ImageButton returnButton;
	private Button sundriesDelete;
	private ArrayList<HashMap<String, Object>> sundriesInfoList = new ArrayList<HashMap<String, Object>>();
	private String object_name;
	private String object_description;
	private String telphoneNumber;
	private String tel_Type;
	private String sundriestype;
	private BitmapUtils bitmapUtils;
	private Msharepreference msharepreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		msharepreference = new Msharepreference(this, "information");
		super.onCreate(savedInstanceState);
		initBasecView();
		// TODO Auto-generated method stub
		myBottomSet();
		titleBar();


	}
	
	private void myBottomSet() {
		// TODO Auto-generated method stub
		AbBottomBar mAbBottomBar = this.getBottomBar();
		View view = mInflater.inflate(R.layout.ls_bottombar, null);
		LinearLayout sundriesDelete = (LinearLayout) view.findViewById(R.id.delete_LinearLayout);

		sundriesDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {

					@Override
					public void run() {
						Looper.prepare();
						String id = msharepreference
								.getStringProperty("user_id");
						String result = null;
						String msg = null;
						JSONObject js = new JSONObject();
						try {
							js.put("id", id);
							js.put("object_name", object_name);
							result = postGround(
									"delete_personal_secondhand_info",
									js.toString());

//							JSONArray jsonArray = new JSONObject(result)
//									.getJSONArray("obj");
//							JSONObject jsonObject = (JSONObject) jsonArray
//									.opt(0);
						msg = (String) new JSONObject(result).get("msg");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (msg.equals("执行成功")) {
							Intent intent = new Intent(SundriesDetail.this,
									Sundries_deal_history.class);
							
							SundriesDetail.this.setResult(1,intent);
							SundriesDetail.this.finish();
							String name = (String) sundriesInfoList.get(0).get("img_url");
							PostGround(name);
							Toast.makeText(SundriesDetail.this, "删除成功",
									Toast.LENGTH_SHORT).show();
							

						} else {
							finish();
							Toast.makeText(SundriesDetail.this, "删除失败",
									Toast.LENGTH_SHORT).show();
						}
						Looper.loop();
					}
				}).start();
			}
		});
		mAbBottomBar.setBottomView(view);
	}

	public void initBasecView(){
		setAbContentView(R.layout.sundries_detail);
		 
		sundriesImage = (ImageView) findViewById(R.id.sundriesImage);
		sundries_name = (TextView) findViewById(R.id.sundriesName);
		sundries_detail = (TextView) findViewById(R.id.sundries_detail);
		sundriesType = (TextView) findViewById(R.id.sundriesType);
		telphone = (TextView) findViewById(R.id.telphone);
		tel_type = (TextView) findViewById(R.id.tel_type);
		

		Intent intent = getIntent();
		Bundle bundle = new Bundle();
		bundle = intent.getBundleExtra("bundlelist");
		ArrayList list = bundle.getParcelableArrayList("listinfo");
		sundriesInfoList = (ArrayList<HashMap<String, Object>>) list.get(0);

		object_name = (String) sundriesInfoList.get(0).get("object_name");
		object_description = (String) sundriesInfoList.get(0).get(
				"object_description");
		telphoneNumber = (String) sundriesInfoList.get(0).get("tel");
		tel_Type = (String) sundriesInfoList.get(0).get("tel_type");
		sundriestype = (String) sundriesInfoList.get(0).get("type");
		bitmapUtils = new BitmapUtils(SundriesDetail.this);
		bitmapUtils.display(sundriesImage, (String) sundriesInfoList.get(0)
				.get("sundriesImageUrl"));
		sundries_name.setText(object_name);
		sundries_detail.setText(object_description);
		telphone.setText(telphoneNumber);
		sundriesType.setText(sundriestype);
		tel_type.setText(tel_Type);
		sundriesImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SundriesDetail.this,
						SundriesImageZoomOut.class);
				intent.putExtra("sundriesImageUrl", (String) sundriesInfoList
						.get(0).get("sundriesImageUrl"));
				SundriesDetail.this.startActivity(intent);
				SundriesDetail.this.overridePendingTransition(
						R.anim.appear_top_left_in,
						R.anim.disappear_bottom_right_out);

			}
		});
		
	}
	private void titleBar(){		
		AbTitleBar mAbTitleBar = this.getTitleBar();
		View view = mInflater.inflate(R.layout.ls_title, null);
		ImageButton returnButton = (ImageButton) view.findViewById(R.id.returnButton);
		returnButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}

		});
		TextView mytext = (TextView) view.findViewById(R.id.textView1);
		mytext.setText("物品详情");
		mAbTitleBar.addView(view);
	}

	public String postGround(String... params) {

		HttpClient client = new DefaultHttpClient();
		// TODO Auto-generated method stub
		String urlString = null;
		try {
			HttpPost post = new HttpPost(Contstant.URL1);
			List<NameValuePair> parms = new ArrayList<NameValuePair>();
			parms.add(new BasicNameValuePair("ids", params[0]));
			parms.add(new BasicNameValuePair("params", params[1]));
			parms.add(new BasicNameValuePair("register", "true"));
			post.setEntity(new UrlEncodedFormEntity(parms, HTTP.UTF_8));
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				urlString = EntityUtils.toString(response.getEntity(), "UTF-8");
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return urlString;
	}
	public String PostGround(String params) {

		HttpClient client = new DefaultHttpClient();
		// TODO Auto-generated method stub
		String urlString = null;
		try {
			HttpPost post = new HttpPost(Contstant.URL3);
			List<NameValuePair> parms = new ArrayList<NameValuePair>();
			parms.add(new BasicNameValuePair("name", params));
			post.setEntity(new UrlEncodedFormEntity(parms, HTTP.UTF_8));
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				urlString = EntityUtils.toString(response.getEntity(), "UTF-8");
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return urlString;
	}

}
