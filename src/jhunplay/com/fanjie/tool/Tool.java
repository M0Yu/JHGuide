package jhunplay.com.fanjie.tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import jhunplay.com.fanjie.Constant.Constant;

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

import com.block.jhguide.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Tool {

	private Context context;
	private Msharepreference sharedPreferences;
	private Editor editor;
	private onGetFinish getFinish;
	private static ProgressDialog dialog1;

	public Tool(Context context) {
		this.context = context;
		dialog1 = new ProgressDialog(context);
	}

	public static JSONObject getJSONObject(int intcount, String... keyAndValue) {
		JSONObject jsonObject = new JSONObject();
		int j = 0;
		for (; j < intcount * 2; j = j + 2) {
			try {
				jsonObject.put(keyAndValue[j],
						Integer.parseInt(keyAndValue[j + 1]));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = intcount * 2; i < keyAndValue.length; i = i + 2) {
			try {
				jsonObject.put(keyAndValue[i], keyAndValue[i + 1]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jsonObject;
	}

	public static String httpPost(JSONObject... params) {
		// TODO Auto-generated method stub
		String result = null;
		try {
			List<NameValuePair> parms = new ArrayList<NameValuePair>();
			String sessionkey;
			String id = params[0].optString("ids");
			params[0].remove("ids");

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constant.URL2);
			sessionkey = params[0].optString("sessionkey");
			params[0].remove("sessionkey");

			parms.add(new BasicNameValuePair("sessionkey", sessionkey));
			parms.add(new BasicNameValuePair("ids", id));
			parms.add(new BasicNameValuePair("params", params[0].toString()));
			post.setEntity(new UrlEncodedFormEntity(parms, HTTP.UTF_8));
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// public static String registerPost(JSONObject jsonObject){
	// String result = null;
	// try {
	// List<NameValuePair> parms = new ArrayList<NameValuePair>();
	//
	// String id = jsonObject.optString("ids");
	// jsonObject.remove("ids");
	// HttpClient client = new DefaultHttpClient();
	// HttpPost post = new HttpPost(Constant.URL2);
	// parms.add(new BasicNameValuePair("register", "true"));
	// parms.add(new BasicNameValuePair("ids", id));
	// parms.add(new BasicNameValuePair("params", jsonObject.toString()));
	// post.setEntity(new UrlEncodedFormEntity(parms, HTTP.UTF_8));
	// HttpResponse response = client.execute(post);
	// if (response.getStatusLine().getStatusCode() == 200) {
	// result = EntityUtils.toString(response.getEntity());
	// }
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return result;
	// }

	public static String Register(JSONObject jsonObject) {
		String result = null;
		try {
			List<NameValuePair> parms = new ArrayList<NameValuePair>();
			String sessionkey;
			String id = jsonObject.optString("ids");
			jsonObject.remove("ids");
			jsonObject
					.put("password", Md5_16(jsonObject.getString("password")));
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constant.URL2);
			parms.add(new BasicNameValuePair("register", "true"));
			parms.add(new BasicNameValuePair("ids", id));
			parms.add(new BasicNameValuePair("params", jsonObject.toString()));
			post.setEntity(new UrlEncodedFormEntity(parms, HTTP.UTF_8));
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static String changePassword(JSONObject jsonObject) {
		String result = null;
		try {
			List<NameValuePair> parms = new ArrayList<NameValuePair>();
			String sessionkey;
			String id = jsonObject.optString("ids");
			jsonObject.remove("ids");
			jsonObject
					.put("password", Md5_16(jsonObject.getString("password")));
			jsonObject.put("oldpassword",
					Md5_16(jsonObject.getString("oldpassword")));
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constant.URL2);
			parms.add(new BasicNameValuePair("register", "true"));
			parms.add(new BasicNameValuePair("ids", id));
			parms.add(new BasicNameValuePair("params", jsonObject.toString()));
			post.setEntity(new UrlEncodedFormEntity(parms, HTTP.UTF_8));
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static String registPost(JSONObject jsonObject) {
		String result = null;
		try {
			List<NameValuePair> parms = new ArrayList<NameValuePair>();
			String id = jsonObject.optString("ids");
			jsonObject.remove("ids");
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constant.URL2);
			parms.add(new BasicNameValuePair("register", "true"));
			parms.add(new BasicNameValuePair("ids", id));
			String a = jsonObject.toString();
			parms.add(new BasicNameValuePair("params", jsonObject.toString()));
			post.setEntity(new UrlEncodedFormEntity(parms, HTTP.UTF_8));
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return result;
	}

	public static ArrayList<Map<String, Object>> Json(String result,
			String... key) {

		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(result == null){
			return list;
		}
		try {

			JSONArray jsonObjs = new JSONObject(result).getJSONArray("obj");
			for (int i = 0; i < jsonObjs.length(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject jsonObject1 = (JSONObject) jsonObjs.opt(i);
				for (int j = 0; j < key.length; j++) {
					map.put(key[j], jsonObject1.optString(key[j]));
				}
				list.add(map);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}

	public static String Md5_16(String plainText) {
		StringBuffer buf = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buf.toString().substring(8, 24);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 0) {
				getFinish = (onGetFinish) context;
				getFinish.onFinish();
			} else if (msg.what == 1) {
				reMoveMyDialog();
				Toast.makeText(context, "登录失败,账号或密码错误", 0).show();
			}
		}
	};

	public void Login(final String login_name, final String password) {
		new Thread() {
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpPost post = new HttpPost(Constant.URL);
					List<NameValuePair> parms = new ArrayList<NameValuePair>();
					parms.add(new BasicNameValuePair("id", "login"));
					JSONObject jsonObject0 = new JSONObject();
					jsonObject0.put("login_name", login_name);
					jsonObject0.put("password", Md5_16(password));
					parms.add(new BasicNameValuePair("params", jsonObject0
							.toString()));
					post.setEntity(new UrlEncodedFormEntity(parms, HTTP.UTF_8));
					HttpResponse response = client.execute(post);
					if (response.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(response
								.getEntity());
						String success = (String) new JSONObject(result)
								.get("msg");
						if (success.equals("登录成功")) {

							String sessionkey = (String) new JSONObject(result)
									.get("sessionkey");
							// JSONArray jsonObjs = new JSONObject(result)
							// .getJSONArray("obj");
							sharedPreferences = new Msharepreference(context,
									"information");
							// sharedPreferences.setStringProperty("phone_mac",
							// szImei);
							// sharedPreferences.setStringProperty("sessionkey",
							// sessionkey);
							// sharedPreferences.setStringProperty("user_id",
							// jsonObjs.optString(0));
							// sharedPreferences.setStringProperty("login_id",
							// jsonObjs.optString(1));
							// sharedPreferences.setStringProperty("name",
							// jsonObjs.optString(3));
							// String name = jsonObjs.optString(3);
							// sharedPreferences.setIntProperty("style",
							// jsonObjs.optInt(5));
							ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
							list = Json(result, "phone_mac", "user_id",
									"login_id", "name", "style");
							sharedPreferences.setStringProperty("phone_mac",
									list.get(0).get("phone_mac").toString());
							sharedPreferences.setStringProperty("sessionkey",
									sessionkey);
							sharedPreferences.setStringProperty("user_id", list
									.get(0).get("user_id").toString());
							sharedPreferences.setStringProperty("login_id",
									list.get(0).get("login_id").toString());
							sharedPreferences.setStringProperty("name", list
									.get(0).get("name").toString());
							sharedPreferences.setIntProperty(
									"style",
									Integer.parseInt(list.get(0).get("style")
											.toString()));

							handler.sendEmptyMessage(0);
						} else {
							handler.sendEmptyMessage(1);
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			};
		}.start();

	}

	private AlertDialog dialog;

	public void showDialog(final Class<? extends Activity> clazz, String title,
			String message) {
		AlertDialog.Builder builder;

		builder = new Builder(context);
		builder.setTitle(title).setMessage(message)
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, clazz);
						context.startActivity(intent);
					}
				}).setNeutralButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});

		dialog = builder.create();
		dialog.show();

	}

	public static void internetCheak(final JSONObject jsonObject) {

		dialog1.setTitle("网络连接异常请检查网络是否通常");
		dialog1.setButton("刷新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Register(jsonObject);
			}
		});
	}

	public void showDialog1(final Class<? extends Activity> clazz,
			String title, String message) {
		AlertDialog.Builder builder;

		builder = new Builder(context);
		builder.setTitle(title).setMessage(message)
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, clazz);
						context.startActivity(intent);
					}
				}).setNeutralButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});

		dialog = builder.create();
		dialog.show();

	}

	public boolean haveShowDialog() {
		return dialog == null ? false : true;
	}

	public void removeDialog() {
		dialog.dismiss();
	}

	public interface onGetFinish {
		public void onFinish();
	}

	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public void showNetDialog(String title, String message) {
		AlertDialog.Builder builder;

		builder = new Builder(context);
		builder.setTitle(title).setMessage(message)
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						context.startActivity(new Intent(
								android.provider.Settings.ACTION_WIRELESS_SETTINGS));
					}
				}).setNeutralButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});

		dialog = builder.create();
		dialog.show();

	}

	Dialog loadingDialog;

	public Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.lw_loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.lw_loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return loadingDialog;

	}

	public void reMoveMyDialog() {
		if (loadingDialog.isShowing()) {
			loadingDialog.dismiss();
		}
	}

}
