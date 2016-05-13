package com.block.activity.main;

import java.util.Iterator;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import jhunpaly.com.zmj.network.judge.NetWorkJudgeTool;
import jhunplay.com.db.DBService;
import jhunplay.com.fanjie.Constant.Constant;
import jhunplay.com.fanjie.tool.Msharepreference;
import jhunplay.com.fanjie.tool.Tool;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.block.activity.map.OverlayDemo;
import com.block.jhguide.R;
import com.fanjie.activity.rightSlider.Login;
import com.tencent.android.tpush.XGPushManager;

public class WelcomeActivity extends Activity {

	private ImageView mShowPicture;
	/**
	 * 三个切换的动画
	 */
	private Animation mFadeIn;
	private Animation mFadeInScale;
	private Animation mFadeOut;
	private Msharepreference preferences;
	private Msharepreference msharepreference;
	private Drawable mPicture_1;
	public static String TAG = "InitActivity";
	private AbHttpUtil mAbHttpUtil = null;
	DBService dbService = new DBService();
	private Tool tool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_welcome_block);
		msharepreference = new Msharepreference(this, "flag");
		tool = new Tool(WelcomeActivity.this);

		init();
		new android.os.Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				preferences = new Msharepreference(WelcomeActivity.this,
						"information");
				if (preferences.getStringProperty("user_id").equals("")) {
					UUID uuid = UUID.randomUUID();
					String user_id = uuid.toString();
					preferences.setStringProperty("user_id", user_id);
					msharepreference.setBooleanProperty("flag", false);
				}
				if (preferences.getStringProperty("phone_mac").equals("")) {
					TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					String szImei = TelephonyMgr.getDeviceId();
					preferences.setStringProperty("phone_mac", szImei);
				}
				if (!(preferences.getStringProperty("sessionkey").equals(""))) {
					XGPushManager.registerPush(getApplicationContext());
				}
				toMainView();
				overridePendingTransition(R.anim.flip_vertical_in,
						R.anim.flip_vertical_out);
			}
		}, 3000);
	}

	private void toMainView() {
		Intent intent = new Intent(this, MainActivity.class);
		// Intent intent = new Intent(this, OverlayDemo.class);
		startActivity(intent);
		intent = null;
		finish();
	}

	private void init() {

		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mShowPicture = (ImageView) findViewById(R.id.guide_picture);
		mFadeIn = AnimationUtils.loadAnimation(WelcomeActivity.this,
				R.anim.flip_vertical_in);
		mFadeIn.setDuration(1000);
		mPicture_1 = getResources().getDrawable(R.drawable.home_picture);
		mShowPicture.setImageDrawable(mPicture_1);
		mPicture_1 = null;
		mShowPicture.startAnimation(mFadeIn);
		mFadeIn = null;
		AbRequestParams my_params = new AbRequestParams();
		my_params.put("ids", "school_map");
		my_params.put("params", "{}");
		my_params.put("register", "true");

		mAbHttpUtil.post(Constant.URL2, my_params,
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, content);
						Log.d("onSuccess", content);
						try {
							JSONObject jsonObject = new JSONObject(content);
							if (jsonObject.optString("success").equals("true")) {
								JSONArray jArray = jsonObject
										.getJSONArray("obj");
								for (int i = 0; i < jArray.length(); i++) {
									JSONObject json = jArray.getJSONObject(i);
									ContentValues values = new ContentValues();
									String key;
									String value;
									Iterator<String> keyIter = json.keys();
									while (keyIter.hasNext()) {
										key = (String) keyIter.next();
										value = json.getString(key);
										values.put(key, value);
									}
									try {
										dbService.insert(WelcomeActivity.this,
												values);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						} catch (Exception e) {
						}
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						Toast.makeText(WelcomeActivity.this, "地图数据初始化失败", 100)
								.show();
					}

					@Override
					public void onFinish() {
					};
				});

	}

	// mAbHttpUtil.post(Constant.URL2, my_params,
	// new AbStringHttpResponseListener() {
	//
	// @Override
	// public void onSuccess(int statusCode, String content) {
	// // TODO Auto-generated method stub
	// super.onSuccess(statusCode, content);
	// Log.d("onSuccess", content);
	// try {
	// JSONObject jsonObject = new JSONObject(content);
	// if (jsonObject.optString("success").equals("true")) {
	// JSONArray jArray = jsonObject
	// .getJSONArray("obj");
	// for (int i = 0; i < jArray.length(); i++) {
	// JSONObject json = jArray.getJSONObject(i);
	// ContentValues values = new ContentValues();
	// String key;
	// String value;
	// Iterator<String> keyIter = json.keys();
	// while (keyIter.hasNext()) {
	// key = (String) keyIter.next();
	// value = json.getString(key);
	// values.put(key, value);
	// }
	// try {
	// dbService.insert(WelcomeActivity.this,
	// values);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// } catch (Exception e) {
	// }
	// }
	//
	// @Override
	// public void onFailure(int statusCode, String content,
	// Throwable error) {
	// Toast.makeText(WelcomeActivity.this, "地图数据初始化失败", 100)
	// .show();
	// }
	//
	// @Override
	// public void onFinish() {
	// };
	// });

	/**
	 * 初始化动画
	 */

	/**
	 * 初始化图片
	 */
	private void initPicture() {
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (Constant.user) {
			msharepreference.setStringProperty("user_id", "");
		}
		mFadeIn = null;
		mFadeInScale = null;
		mFadeOut = null;
		preferences = null;
		msharepreference = null;
		mAbHttpUtil = null;
		tool = null;
		System.gc();
	}

}
