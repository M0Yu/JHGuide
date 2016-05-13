package com.wudi.comment.activity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jhunplay.com.fanjie.Constant.Constant;
import jhunplay.com.fanjie.adapter.ImageShowAdapter;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.block.activity.map.OverlayDemo;
import com.block.jhguide.R;
import com.fanjie.activity.rightSlider.Login;
import com.lidroid.xutils.BitmapUtils;

public class StoreDetailActivity extends Activity {
	private ImageView storeimageView;
	private TextView storename;
	private TextView score;
	private TextView zannum;
	private TextView storead;
	private ImageButton scButton;
	private ImageButton zButton;
	private ImageButton mapButton;
	private ImageButton moreButton;
	private RatingBar selfRatingBar;
	private ListView listView;
	private MyAdapter myAdapter;
	private BitmapUtils bitmapUtils;
	private Tool tool;
	private String collection;
	private String praise;
	private int id;
	private Msharepreference msharepreference;
	private ArrayList<HashMap<String, Object>> storeInfoList = new ArrayList<HashMap<String, Object>>();
	private ArrayList<HashMap<String, Object>> userCommentList = new ArrayList<HashMap<String, Object>>();

	private GridView mGridView;
	private ImageShowAdapter imageShowAdapter;
	private Button storeDetailBack;
	private TextView storeDetail;
	private ImageButton phoneButton;
	private TextView phoneNumber;
	private TextView activityContent;
	private ScrollView storeDetailScrollView;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private int currentPosition;
	private static int IO_BUFFER_SIZE = 2 * 1024;
	private Intent intent;
	private boolean sign = false;

	// private int Page;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wudi_storedetail);
		tool = new Tool(this);
		storeDetailBack = (Button) findViewById(R.id.storeDetailback);
		storeimageView = (ImageView) findViewById(R.id.storeimageView);
		storename = (TextView) findViewById(R.id.storeName);
		storeDetail = (TextView) findViewById(R.id.storeDetail);
		score = (TextView) findViewById(R.id.scoretextView);
		zannum = (TextView) findViewById(R.id.zanNum);
		storead = (TextView) findViewById(R.id.storeaddress);
		scButton = (ImageButton) findViewById(R.id.storedetailcollectButton);
		zButton = (ImageButton) findViewById(R.id.zanbutton);
		mapButton = (ImageButton) findViewById(R.id.mapbutton);
		phoneButton = (ImageButton) findViewById(R.id.phonebutton);
		phoneNumber = (TextView) findViewById(R.id.phoneNumber);
		activityContent = (TextView) findViewById(R.id.activityContent);
		moreButton = (ImageButton) findViewById(R.id.lookbutton);
		selfRatingBar = (RatingBar) findViewById(R.id.totalRatingBar);
		listView = (ListView) findViewById(R.id.detaillistView);
		storeDetailScrollView = (ScrollView) findViewById(R.id.storeDetailScrollView);
		listView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {

					storeDetailScrollView
							.requestDisallowInterceptTouchEvent(false);
				} else {
					storeDetailScrollView
							.requestDisallowInterceptTouchEvent(true);
				}
				return false;
			}
		});
		msharepreference = new Msharepreference(this, "information");

		// 2条评论的 适配器
		myAdapter = new MyAdapter();
		listView.setAdapter(myAdapter);

		// 获取从MainActivity传递的listinfo
		intent = getIntent();
		Bundle bundle = new Bundle();
		// Bundle bundle1 = new Bundle();
		bundle = intent.getBundleExtra("bundlelist");

		ArrayList list = bundle.getParcelableArrayList("listinfo");
		storeInfoList = (ArrayList<HashMap<String, Object>>) list.get(0);// 强转成自己定义的list，这样storeInfoList就是你传过来的那个list;

		// 获取店铺界面初始化的 是否被点赞 和收藏
		collection = (String) storeInfoList.get(0).get("collection");
		praise = (String) storeInfoList.get(0).get("praise");
		id = (Integer) storeInfoList.get(0).get("id");
		currentPosition = (Integer) storeInfoList.get(0).get("currentPosition");
		// Page = (Integer) storeInfoList.get(0).get("currentPage");
		// 将StoreInfoList中的信息取出来 初始化控件信息
		bitmapUtils = new BitmapUtils(StoreDetailActivity.this);
		bitmapUtils.display(storeimageView,
				"http://61.142.71.63:9090/jhun_play/"
						+ storeInfoList.get(0).get("pic_url"));
		storename.setText((String) storeInfoList.get(0).get("name"));
		BigDecimal bd = new BigDecimal((Float) storeInfoList.get(0).get(
				"_level"));
		bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		score.setText(bd + "分");
		selfRatingBar.setRating((Float) storeInfoList.get(0).get("_level"));
		zannum.setText("" + storeInfoList.get(0).get("oknums"));
		storead.setText((String) storeInfoList.get(0).get("address"));

		// 异步线程 post
		ZmjUserCommentAsyncTask userCommentAsyscTask = new ZmjUserCommentAsyncTask();
		JSONObject js = new JSONObject();
		try {
			js.put("store_id", storeInfoList.get(0).get("id") + "");
			js.put("type", "0");
			js.put("min", "0");
			js.put("max", "2");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		userCommentAsyscTask.execute("get_commonts", js.toString());

		storeimageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// LayoutInflater inflater =
				// LayoutInflater.from(StoreDetailActivity.this);
				// View imgEntryView =
				// inflater.inflate(R.layout.dialog_photo_entry, null);
				// final AlertDialog dialog = new
				// AlertDialog.Builder(StoreDetailActivity.this).create();
				// ImageView img = (ImageView)
				// imgEntryView.findViewById(R.id.large_image);
				//
				// bitmapUtils.display(img,
				// "http://61.142.71.63:9090/jhun_play/"
				// + storeInfoList.get(0).get("pic_url"));
				//
				// dialog.setView(imgEntryView);
				// dialog.show();
				//
				// imgEntryView.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View paramView) {
				// // TODO Auto-generated method stub
				// dialog.cancel();
				// }
				// });

				Intent intent = new Intent(StoreDetailActivity.this,
						StoreImageZoomOut.class);
				intent.putExtra("image_path", Constant.URL3
						+ storeInfoList.get(0).get("pic_url"));
				StoreDetailActivity.this.startActivity(intent);

				StoreDetailActivity.this.overridePendingTransition(
						R.anim.appear_bottom_right_in,
						R.anim.disappear_top_left_out);

			}
		});

		storeDetailBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent = new Intent();
				// intent.setClass(StoreDetailActivity.this,
				// HaveFunListActivity.class);
				//

				Bundle bundle = new Bundle();
				intent.putExtra("praise", praise);
				intent.putExtra("collection", collection);
				intent.putExtra("currentPosition", currentPosition);
				StoreDetailActivity.this.setResult(1, intent);
				StoreDetailActivity.this.finish();

			}
		});

		mapButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				// store的定位 向 地图Activity 传递 店铺名字name 经纬度 xlong xlat
				Bundle bundle = new Bundle();
				bundle.putString("name",
						(String) storeInfoList.get(0).get("name"));
				bundle.putDouble("xlong",
						(Double) storeInfoList.get(0).get("xlong"));
				bundle.putDouble("xlat",
						(Double) storeInfoList.get(0).get("xlat"));
				bundle.putString("style", "-1");
				intent.putExtras(bundle);
				intent.setClass(StoreDetailActivity.this, OverlayDemo.class);
				StoreDetailActivity.this.startActivity(intent);
			}
		});

		storeDetail.setText((String) storeInfoList.get(0).get("details"));
		// 活动信息字段尚未添加 同样 数据源 需要改
		activityContent.setText((String) storeInfoList.get(0).get(
				"activity_info"));
		// 获取手机号 现在 playwithlife中没有获取该字段 havefunlist 也没有传递 该字段 服务器 数据库中没有定义该字段
		phoneNumber.setText((String) storeInfoList.get(0).get("phone"));

		phoneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Uri uri = Uri.parse("tel:" + phoneNumber.getText().toString());
				Intent intent = new Intent(Intent.ACTION_CALL, uri);
				StoreDetailActivity.this.startActivity(intent);

			}
		});

		moreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent(StoreDetailActivity.this,
						Commentdetail.class);
				Bundle bundle = new Bundle();
				bundle.putInt("store_id",
						(Integer) storeInfoList.get(0).get("id"));
				intent2.putExtra("storeCommentBundle", bundle);
				StoreDetailActivity.this.startActivity(intent2);
			}
		});

		if (!praise.equals("")) {
			zButton.setBackgroundResource(R.drawable.wudi_solid_praise);

		}
		if (!collection.equals("")) {
			scButton.setBackgroundResource(R.drawable.wudi_solid_star);

		}

		zButton.setOnClickListener(new dianzanListener());
		scButton.setOnClickListener(new shoucangListener());

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Bundle bundle = new Bundle();
			intent.putExtra("praise", praise);
			intent.putExtra("collection", collection);
			intent.putExtra("currentPosition", currentPosition);
			StoreDetailActivity.this.setResult(1, intent);
			StoreDetailActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	class shoucangListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (msharepreference.getStringProperty("sessionkey") == "") {
				tool.showDialog(Login.class, "您未登入", "请您登入或注册");
				return;
			}
			if (!collection.equals("")) {
				scButton.setBackgroundResource(R.drawable.wudi_hollow_star);

				JSONObject jsonObject = Tool.getJSONObject(2, "store_id", id
						+ "", "type", "0", "ids", "delete_collection",
						"user_id",
						msharepreference.getStringProperty("user_id"));
				CancelCollect cancelCollect = new CancelCollect();
				cancelCollect.execute(jsonObject);
			} else {

				scButton.setBackgroundResource(R.drawable.wudi_solid_star);

				JSONObject jsonObject = Tool.getJSONObject(2, "store_id", id
						+ "", "type", "0", "ids", "collection_store",
						"user_id",
						msharepreference.getStringProperty("user_id"));
				CollectStoreAsyncTask collectStoreAsyncTask = new CollectStoreAsyncTask();
				collectStoreAsyncTask.execute(jsonObject);

			}
		}

	}

	class dianzanListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (msharepreference.getStringProperty("sessionkey").equals("")) {
				tool.showDialog(Login.class, "未登入", "请先登入");
				return;
			}
			if (!praise.equals("")) {// praise !=0
				zButton.setBackgroundResource(R.drawable.wudi_hollow_praise);
				JSONObject jsonObject = Tool.getJSONObject(2, "store_id", id
						+ "", "type", "0", "ids", "delete_parise", "user_id",
						msharepreference.getStringProperty("user_id"));
				CancelPraise cancelPraise = new CancelPraise();
				cancelPraise.execute(jsonObject);
			} else {
				zButton.setBackgroundResource(R.drawable.wudi_solid_praise);
				JSONObject jsonObject = Tool.getJSONObject(2, "store_id", id
						+ "", "type", "0", "ids", "praise_store", "user_id",
						msharepreference.getStringProperty("user_id"));
				PraiseStoreAsyncTask praiseStoreAsyncTask = new PraiseStoreAsyncTask();
				praiseStoreAsyncTask.execute(jsonObject);

			}
		}
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return userCommentList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(StoreDetailActivity.this)
						.inflate(R.layout.wudi_detailsim, null);

				holder = new ViewHolder();
				holder.userName = (TextView) convertView
						.findViewById(R.id.username);
				holder.userComment = (TextView) convertView
						.findViewById(R.id.userComment);
				holder.userRatingBar = (RatingBar) convertView
						.findViewById(R.id.userRatingbar);
				holder.userName.setText((String) userCommentList.get(position)
						.get("user_name"));
				holder.userComment.setText((String) userCommentList.get(
						position).get("content"));
				holder.userRatingBar.setRating((Float) userCommentList.get(
						position).get("level"));

			}

			return convertView;

		}
	}

	public final class ViewHolder {

		public TextView userName;
		public TextView userComment;
		public RatingBar userRatingBar;

	}

	public class ZmjUserCommentAsyncTask extends
			AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlResult = null;
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost post = new HttpPost(Constant.URL2);
				List<NameValuePair> parms = new ArrayList<NameValuePair>();
				parms.add(new BasicNameValuePair("ids", params[0]));
				parms.add(new BasicNameValuePair("params", params[1]));
				parms.add(new BasicNameValuePair("register", "true"));
				post.setEntity(new UrlEncodedFormEntity(parms));
				HttpResponse response = httpClient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					urlResult = EntityUtils.toString(response.getEntity());

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
			// TODO Auto-generated method stub
			userCommentList = JsonAnalysis(result);
			myAdapter.notifyDataSetChanged();

		}

	}

	public ArrayList<HashMap<String, Object>> JsonAnalysis(String str) {

		ArrayList<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
		try {

			JSONArray jsonObjs = new JSONObject(str).getJSONArray("obj");

			for (int i = 0; i < jsonObjs.length(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				JSONObject jsonObj = (JSONObject) jsonObjs.opt(i);
				int id = jsonObj.getInt("id");// 评论数
				int store_id = jsonObj.getInt("store_id");
				String usr_id = jsonObj.getString("user_id");
				String user_name = jsonObj.getString("user_name");
				String content = jsonObj.getString("content");
				float level;
				if (jsonObj.optString("level").contains("null")) {
					level = 0;
				} else {
					level = (float) jsonObj.optDouble("level");
				}
				String commont_time = jsonObj.getString("commont_time");
				int type = jsonObj.getInt("type");
				map.put("id", id);
				map.put("store_id", store_id);
				map.put("user_name", user_name);
				map.put("content", content);
				map.put("level", level);
				map.put("commont_time", commont_time);
				map.put("type", type);
				dataList.add(map);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataList;

	}

	private class PraiseStoreAsyncTask extends
			AsyncTask<JSONObject, Integer, String> {

		@Override
		protected String doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub
			String string = Tool.registPost(params[0]);
			return string;
		}

		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				String success = (String) new JSONObject(result).get("msg");
				if (success.equals("执行成功")) {
					praise = "1";
					// zannum.setText(Integer
					// .parseInt(zannum.getText().toString()) + 1);
				} else {
					Toast.makeText(StoreDetailActivity.this, "点赞失败", 0).show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private class CollectStoreAsyncTask extends
			AsyncTask<JSONObject, Integer, String> {
		@Override
		protected String doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub
			String string = Tool.registPost(params[0]);
			return string;
		}

		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				String success = (String) new JSONObject(result).get("msg");

				if (success.equals("执行成功")) {
					collection = "1";
				} else {
					Toast.makeText(StoreDetailActivity.this, "收藏失败", 0).show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private class CancelPraise extends AsyncTask<JSONObject, Integer, String> {

		@Override
		protected String doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub
			String string = Tool.registPost(params[0]);
			return string;
		}

		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				String fail = (String) new JSONObject(result).get("msg");

				if (result.contains("执行成功")) {

					praise = "";

				} else {
					Toast.makeText(StoreDetailActivity.this, "取消点赞失败", 0)
							.show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private class CancelCollect extends AsyncTask<JSONObject, Integer, String> {

		@Override
		protected String doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub
			String string = Tool.registPost(params[0]);
			return string;
		}

		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				String fail = (String) new JSONObject(result).get("msg");

				if (result.contains("执行成功")) {

					collection = "";

				} else {
					Toast.makeText(StoreDetailActivity.this, "取消失败", 0).show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (tool.haveShowDialog()) {
			tool.removeDialog();
			finish();
		}

	}

}
