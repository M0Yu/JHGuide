package com.block.activity.play;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jhunplay.com.fanjie.Constant.Constant;
import jhunplay.com.fanjie.tool.Msharepreference;
import jhunplay.com.fanjie.tool.Tool;

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

import com.ab.bitmap.AbImageDownloader;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener;
import com.ab.util.AbImageUtil;
import com.ab.util.AbViewHolder;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.block.jhguide.R;
import com.fanjie.activity.rightSlider.Login;
import com.wudi.comment.activity.Commentdetail;
import com.wudi.comment.activity.StoreDetailActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class HaveFunListActivity extends Activity implements
		OnHeaderRefreshListener, OnFooterLoadListener {

	ListView listView;

	// listView 数据源
	// ArrayList<ListItem> listDate;
	// private AbTitleBar mAbTitleBar = null;
	private List<Map<String, Object>> list = null;
	// private List<Map<String, Object>> newList = null;
	private ArrayList<Map<String, Object>> jsonList = new ArrayList<Map<String, Object>>();
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private ImageListAdapter myListViewAdapter = null;
	private ListView mListView = null;
	private JSONArray jsonObjs;
	// private boolean sign;
	private int pageSize = 6;
	private int currentPage;
	// private int currentPageReturn;
	private ArrayList<Map<String, Object>> resource = new ArrayList<Map<String, Object>>();
	private Tool mTool;
	private int style;
	private Msharepreference msharepreference;
	public String Collection[];
	public String Praise[];
	public int Id[];
	private int count = 1;
	private ImageButton returnButton;
	// private EditText editText;
	private ImageButton searchButton;
	// private NetWorkJudgeTool netJudge;
	// private TextView textView1;
	// private TextView textView2;
	private Button button;
	// private int columnWidth;
	private AbImageDownloader abImageDownloader;

	// private int currentPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// Intent intent1 = new Intent();
		//
		// currentPageReturn =
		// (intent1.getBundleExtra("bundle").getInt("currentPageReturn"));
		//
		// currentPage = currentPageReturn;

		// TODO Auto-generated method stub
		// if (checkNetWorkStatus()==true) {

		// } else {
		// Toast.makeText(getApplicationContext(), "网络不可用",
		// Toast.LENGTH_SHORT).show();
		//
		// }

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		// netJudge = new NetWorkJudgeTool();
		msharepreference = new Msharepreference(this, "information");
		Intent intent = HaveFunListActivity.this.getIntent();
		Bundle styleBundle = intent.getBundleExtra("style");
		style = styleBundle.getInt("position");
		// if (netJudge.isNetWorkConnected(HaveFunListActivity.this)) {

		initBasicView();

		// if
		// (!netJudge.isMobileConnected(HaveFunListActivity.this)&&!netJudge.isWifiConnected(HaveFunListActivity.this)){
		//
		// Toast.makeText(HaveFunListActivity.this, "网络不可用",
		// Toast.LENGTH_SHORT).show();
		// }

		// if(!isNetworkAvailable(HaveFunListActivity.this)){
		// Toast.makeText(HaveFunListActivity.this, "网络不可用",
		// Toast.LENGTH_SHORT).show();
		//
		// }

		// } else {
		//
		// // 无网络连接的 时候 要搞 默认显示的东西
		// init();
		// Toast.makeText(HaveFunListActivity.this, "网络连接异常，请检查网络",
		// Toast.LENGTH_SHORT).show();
		// }

	}

	public boolean isNetworkAvailable(Activity activity) {
		Context context = activity.getApplicationContext();
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		}
		return true;
	}

	// public boolean checkNetWorkStatus(){
	// boolean result;
	// ConnectivityManager cm =
	// (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	// if (cm.getActiveNetworkInfo() != null &&
	// cm.getActiveNetworkInfo().isConnected()) {
	// result = true;
	// Log.i("NetStatus", "The net was connected");
	// } else {
	// result = false;
	// Log.i("NetStatus", "The net was bad!");
	//
	// }
	// return result;
	// }

//	public void init() {
//		setContentView(R.layout.nonetconnect);
//		// textView1 = (TextView) findViewById(R.id.textView1);
//		// textView2 = (TextView) findViewById(R.id.textView2);
//		button = (Button) findViewById(R.id.button1);
//		button.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.setClass(HaveFunListActivity.this,
//						HaveFunListActivity.class);
//				startActivity(intent);
//
//			}
//		});
//
//	}

	// public void restart(){
	// super.onStart();
	// }
	public void initBasicView() {

		setContentView(R.layout.view_list_play_detail);
		mTool = new Tool(this);
		// // mAbTitleBar = this.getTitleBar();
		// mAbTitleBar.setLogo(R.drawable.storelist_return_button);
		// mAbTitleBar
		// .setTitleBarBackground(R.drawable.storelist_titlebarbackground);
		// View rightViewSearchEditText = mInflater.inflate(
		// R.layout.item_list_titlebar_edittext, null);
		// View rightViewSearchButton = mInflater.inflate(
		// R.layout.item_list_titlebar_edittext, null);

		// mAbTitleBar.addRightView(rightViewSearchEditText);

		// EditText searchEditText = (EditText) rightViewSearchEditText
		// .findViewById(R.id.editText);
		// Button searchButton = (Button) rightViewSearchEditText
		// .findViewById(R.id.searchButton);
		returnButton = (ImageButton) findViewById(R.id.returnButton);
		// editText = (EditText) findViewById(R.id.editText);
		searchButton = (ImageButton) findViewById(R.id.searchButton);

		returnButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				System.gc();

			}
		});

		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// 填写点击 搜索框 搜索自己喜欢的店铺 执行 搜搜的 算法 和具体操作

			}
		});

		// 获取ListView对象
		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView = (ListView) findViewById(R.id.list_paly_detail);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				intent.setClass(HaveFunListActivity.this,
						StoreDetailActivity.class);
				Bundle bundle = new Bundle();
				ArrayList list1 = new ArrayList();// 这个list用于在budnle中传递
													// 需要传递的ArrayList<Object>

				ArrayList<HashMap<String, Object>> storeList = new ArrayList<HashMap<String, Object>>();
				HashMap<String, Object> storeMap = new HashMap<String, Object>();
				// 将 店铺列表的所点击的第 n个店铺的的信息 抽出来 抽取 回调 返回给 mainActivity 传递
				storeMap.put("id", resource.get(position).get("id"));
				storeMap.put("name", resource.get(position).get("name"));
				storeMap.put("details", resource.get(position).get("details"));
				storeMap.put("address", resource.get(position).get("address"));
				storeMap.put("xlong", resource.get(position).get("xlong"));
				storeMap.put("xlat", resource.get(position).get("xlat"));
				storeMap.put("oknums", resource.get(position).get("oknums"));
				storeMap.put("pic_url", resource.get(position).get("pic_url"));
				storeMap.put("_level", resource.get(position).get("_level"));
				storeMap.put("praise", Praise[position]);
				storeMap.put("style", resource.get(position).get("style"));
				storeMap.put("collection", Collection[position]);
				storeMap.put("activity_info",
						resource.get(position).get("activity_info"));
				storeMap.put("phone", resource.get(position).get("phone"));
//				storeMap.put("stylename",
//						resource.get(position).get("stylename"));
				storeMap.put("currentPosition", position);
				storeList.add(storeMap);

				list1.add(storeList);
				bundle.putParcelableArrayList("listinfo", list1);
				intent.putExtra("bundlelist", bundle);
				HaveFunListActivity.this.startActivityForResult(intent, 1);

			}
		});

		// 设置监听器
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);

		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));

		refreshTask();

		list = new ArrayList<Map<String, Object>>();

		// collection= new int [resource.size()];
		// id = new int [resource.size()];
		// praise = new int [resource.size()];

		// 使用自定义的adapter
		myListViewAdapter = new ImageListAdapter(this, list,
				R.layout.item_list_play_detail, new String[] { "storeImage",
						"storeName", "storeDescription", "ratingBar1",
						"pointButton", "collectButton", "commentButton",
						"collectTextview", "commentTextview", "pointTextview",
						"pointButtonView", "collectButtonView",
						"commentButtonView" }, new int[] { R.id.storeImage,
						R.id.storeName, R.id.storeDescription, R.id.ratingBar1,
						R.id.pointButton, R.id.collectButton,
						R.id.commentButton, R.id.collectTextview,
						R.id.commentTextview, R.id.pointTextview,
						R.id.pointButtonView, R.id.collectButtonView,
						R.id.commentButtonView });
		mListView.setAdapter(myListViewAdapter);

	}

	// sign=true;//标记 为 刚刚刷新
	// currentPage++;
	// newList=ToolManager((currentPage - 1)*pageSize, pageSize);

	@Override
	public void onFooterLoad(AbPullToRefreshView view) {
		// TODO Auto-generated method stub
		loadMoreTask();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView view) {
		// TODO Auto-generated method stub
		refreshTask();
	}

	public void refreshTask() {
		AbTask mAbTask = new AbTask();
		AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {
			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				List<Map<String, Object>> newList = null;
				try {

					currentPage = 1;
					newList = new ArrayList<Map<String, Object>>();
					newList = ToolManager((currentPage - 1) * pageSize,
							pageSize);

				} catch (Exception e) {

				}

				return newList;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub
				List<Map<String, Object>> newList = (List<Map<String, Object>>) paramList;
				list.clear();
				if (newList != null && newList.size() > 0) {
					list.addAll(newList);
					myListViewAdapter.notifyDataSetChanged();
					newList.clear();
				}

				mAbPullToRefreshView.onHeaderRefreshFinish();
			}

		});
		mAbTask.execute(item);

	}

	public void loadMoreTask() {
		AbTask mAbTask = new AbTask();
		AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub
				List<Map<String, Object>> newList = (List<Map<String, Object>>) paramList;

				if (newList != null && newList.size() > 0) {
					list.addAll(newList);
					myListViewAdapter.notifyDataSetChanged();
					newList.clear();
				}
				mAbPullToRefreshView.onFooterLoadFinish();
			}

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub

				List<Map<String, Object>> newList = null;
				try {
					newList = new ArrayList<Map<String, Object>>();
					if (list.size() >= currentPage * pageSize) {
						currentPage++;
						newList = ToolManager((currentPage - 1) * pageSize,
								pageSize);

					}
				} catch (Exception e) {
					// TODO: handle exception
					currentPage--;
					newList.clear();
				}
				return newList;
			};
		});
		mAbTask.execute(item);
	}

	public void refresh() {
		AbTask mAbTask = new AbTask();
		AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {
			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				List<Map<String, Object>> newList = null;
				try {
					newList = new ArrayList<Map<String, Object>>();
					newList = ToolManager2(0, myListViewAdapter.getCount());

				} catch (Exception e) {

				}

				return newList;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub
				ArrayList<Map<String, Object>> newList = (ArrayList<Map<String, Object>>) paramList;
				if (newList != null && newList.size() > 0) {
					list.clear();
					list.addAll(newList);
					newList.clear();
					myListViewAdapter.notifyDataSetChanged();
				}

			}

		});
		mAbTask.execute(item);

	}

	public ArrayList<Map<String, Object>> ToolManager(int min, int max) {
		// CXAsyncTask task = new CXAsyncTask();
		JSONObject js = new JSONObject();
		String result = null;
		String user_id = msharepreference.getStringProperty("user_id");
		try {
			js.put("user_id", user_id + "");

			js.put("style", style);// 暂时设为定值，要改为 oncreate中获取到的style；
			js.put("type", 0);
			js.put("min", min);
			js.put("max", max);
			result = postGround("search_store", js.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 要获取
		jsonList.clear();
		jsonList = JsonJx(result);

		ArrayList<Map<String, Object>> jsonList1 = new ArrayList<Map<String, Object>>();
		jsonList1 = setStoreList(jsonList);
		return jsonList1;

	}

	public ArrayList<Map<String, Object>> ToolManager2(int min, int max) {
		// CXAsyncTask task = new CXAsyncTask();
		JSONObject js = new JSONObject();
		String result = null;
		String user_id = msharepreference.getStringProperty("user_id");
		try {
			js.put("user_id", user_id + "");

			js.put("style", style);// 暂时设为定值，要改为 oncreate中获取到的style；
			js.put("type", 0);
			js.put("min", min);
			js.put("max", max);
			result = postGround("search_store", js.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 要获取

		ArrayList<Map<String, Object>> jsonList1 = new ArrayList<Map<String, Object>>();

		jsonList1 = JsonJx(result);
		Map<String, Object> storeMap = null;
		ArrayList<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < jsonList1.size(); i++) {
			storeMap = new HashMap<String, Object>();
			storeMap.put("storeImageUrl", "http://61.142.71.63:9090/jhun_play/"
					+ jsonList1.get(i).get("pic_url"));
			storeMap.put("storeName", jsonList1.get(i).get("name"));
			storeMap.put("storeDescription", jsonList1.get(i).get("details"));
			storeMap.put("storeStar", jsonList1.get(i).get("_level"));
			storeMap.put("storeIsPoint", jsonList1.get(i).get("praise"));
			storeMap.put("storeID", jsonList1.get(i).get("id"));
			storeMap.put("sotreIsCollect", jsonList1.get(i).get("collection"));
			list1.add(storeMap);
		}
		return list1;

	}

	public void initArray() {

		Collection = new String[1000];
		Id = new int[1000];
		Praise = new String[1000];

	}

	public ArrayList<Map<String, Object>> setStoreList(
			ArrayList<Map<String, Object>> resultList) {
		if (currentPage == 1) {
			resource.clear();
			resource.removeAll(resource);
		}
		resource.addAll(resultList);

		// resource=resultList;
		count++;
		if (count <= 2) {

			initArray();
		}
		ArrayList<Map<String, Object>> newList1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> storeMap = null;
		ArrayList<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < resultList.size(); i++) {
			storeMap = new HashMap<String, Object>();
			storeMap.put("storeImageUrl", "http://61.142.71.63:9090/jhun_play/"
					+ resultList.get(i).get("pic_url"));
			storeMap.put("storeName", resultList.get(i).get("name"));
			storeMap.put("storeDescription", resultList.get(i).get("details"));
			storeMap.put("storeStar", resultList.get(i).get("_level"));
			storeMap.put("storeIsPoint", resultList.get(i).get("praise"));
			storeMap.put("storeID", resultList.get(i).get("id"));
			storeMap.put("sotreIsCollect", resultList.get(i).get("collection"));
			list1.add(storeMap);
		}

		return list1;

	}

	public ArrayList<Map<String, Object>> JsonJx(String str) {
		ArrayList<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		try {
			jsonObjs = new JSONObject(str).getJSONArray("obj");
			for (int i = 0; i < jsonObjs.length(); i++) {
				JSONObject jsonObject = (JSONObject) jsonObjs.opt(i);
				HashMap<String, Object> jsonmap = new HashMap<String, Object>();
				// HashMap<String, String> map = new HashMap<String, String>();
				jsonmap.put("id", jsonObject.optInt("id"));
				jsonmap.put("name", jsonObject.getString("name"));
				jsonmap.put("details", jsonObject.optString("details"));
				jsonmap.put("address", jsonObject.optString("address"));
				jsonmap.put("xlong", jsonObject.optDouble("xlong"));
				jsonmap.put("xlat", jsonObject.optDouble("xlat"));
				jsonmap.put("oknums", jsonObject.getInt("oknums"));
				jsonmap.put("pic_url", jsonObject.optString("pic_url"));
				float _level;
				if (jsonObject.optString("_level").contains("null")) {
					_level = 0;
				} else {
					_level = (float) jsonObject.optDouble("_level");
				}
				jsonmap.put("_level", _level);

				String praise;

				if (jsonObject.optString("praise").contains("null")) {
					praise = "";
				} else {

					praise = jsonObject.optString("praise");
				}

				String collection;
				if (jsonObject.optString("collection").contains("null")) {
					collection = "";
				} else {
					collection = jsonObject.optString("collection");
				}
				jsonmap.put("praise", praise);
				jsonmap.put("style", jsonObject.optInt("style"));
				jsonmap.put("collection", collection);
				jsonmap.put("activity_info", jsonObject.opt("activity_info"));
				jsonmap.put("phone", jsonObject.opt("phone"));
				jsonmap.put("stylename", jsonObject.optString("stylename"));
				list1.add(jsonmap);

			}
			// for (int i = 0; i < list1.size(); i++) {
			// System.out.println("===============>"+ jsonList.get(i));
			//
			// }

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list1;

	}

	// public class CXAsyncTask extends AsyncTask<String, Integer, String>{

	// @Override
	public String postGround(String... params) {

		HttpClient client = new DefaultHttpClient();
		// TODO Auto-generated method stub
		String urlString = null;
		try {
			HttpPost post = new HttpPost(Constant.URL2);
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

	public class ImageListAdapter extends BaseAdapter {

		private Context mContext;
		// xml转View对象
		private LayoutInflater mInflater;
		// 单行的布局
		private int mResource;
		// 列表展现的数据
		private List mData;
		// Map中的key
		// private String[] mFrom;
		// view的id
		private int[] mTo;
		// 图片加载器
		// private BitmapUtils bitmapUtils;

		private String isPoint;
		private String isCollect;
		private int store_id;

		// // 自定义potinButton和collectButton的状态
		// private int pointButtonFlag;
		// private int collectButtonFlag;

		// 构造函数
		public ImageListAdapter(Context context, List data, int resource,
				String[] from, int[] to) {
			this.mContext = context;
			this.mData = data;
			this.mResource = resource;
			// this.mFrom = from;
			this.mTo = to;
			// 用于将xml转为View
			this.mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// 图片下载器
			abImageDownloader = new AbImageDownloader(mContext);
			abImageDownloader.setWidth(106);
			abImageDownloader.setHeight(80);
			abImageDownloader.setType(AbImageUtil.SCALEIMG);
			abImageDownloader.setLoadingImage(R.drawable.image_loading);
			abImageDownloader.setErrorImage(R.drawable.image_error);
			abImageDownloader.setNoImage(R.drawable.image_no);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub3
			// 使用自定义的list_items作为Layout
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}

			ImageView storeImage = AbViewHolder.get(convertView, mTo[0]);
			TextView storeName = AbViewHolder.get(convertView, mTo[1]);
			TextView storeDescription = AbViewHolder.get(convertView, mTo[2]);
			RatingBar ratingBar1 = AbViewHolder.get(convertView, mTo[3]);
			final Button pointButton = AbViewHolder.get(convertView, mTo[4]);
			final Button collectButton = AbViewHolder.get(convertView, mTo[5]);
			Button commentButton = AbViewHolder.get(convertView, mTo[6]);
			final TextView collectTextView = AbViewHolder.get(convertView,
					mTo[7]);
			TextView commentTextView = AbViewHolder.get(convertView, mTo[8]);
			final TextView pointTextView = AbViewHolder
					.get(convertView, mTo[9]);
			// 获取覆盖3个Button的空白 view的控件
			View pointButtonView = AbViewHolder.get(convertView, mTo[10]);
			View collectButtonView = AbViewHolder.get(convertView, mTo[11]);
			View commentButtonView = AbViewHolder.get(convertView, mTo[12]);
			// 获取改行 map数据
			final Map<String, Object> obj = (Map<String, Object>) mData
					.get(position);
			String storeImageUrl = (String) obj.get("storeImageUrl");
			// 每一行的店铺的信息的设置

			// 默认的店铺图片的加载 从本地 assets/image/ 加载

			abImageDownloader.display(storeImage, storeImageUrl);
			// columnWidth = storeImage.getWidth();
			storeName.setText((String) obj.get("storeName"));
			storeDescription.setText((String) obj.get("storeDescription"));
			ratingBar1.setRating((Float) obj.get("storeStar"));
			isPoint = (String) obj.get("storeIsPoint");
			isCollect = (String) obj.get("sotreIsCollect");
			store_id = (Integer) obj.get("storeID");

			pointButtonView.setTag(R.string.ispoint, isPoint);
			collectButtonView.setTag(R.string.iscollect, isCollect);
			Praise[position] = isPoint;
			Collection[position] = isCollect;
			Id[position] = store_id;

			if (/* !Praise[position].equals("") */!pointButtonView.getTag(
					R.string.ispoint).equals("")) {

				pointButton.setBackgroundResource(R.drawable.wudi_solid_praise);

				pointTextView.setText("已赞");

			} else {
				pointButton
						.setBackgroundResource(R.drawable.wudi_hollow_praise);

				pointTextView.setText("点赞");
			}
			if (/* !Collection[position].equals("") */!collectButtonView
					.getTag(R.string.iscollect).equals("")) {
				collectButton.setBackgroundResource(R.drawable.wudi_solid_star);

				collectTextView.setText("已收藏");

			} else {
				collectButton
						.setBackgroundResource(R.drawable.wudi_hollow_star);

				collectTextView.setText("收藏");
			}

			storeImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// ListItem item = listDate.get(arg0);
					Intent intent = new Intent();
					intent.setClass(HaveFunListActivity.this,
							StoreDetailActivity.class);
					Bundle bundle = new Bundle();
					ArrayList list = new ArrayList();// 这个list用于在budnle中传递
														// 需要传递的ArrayList<Object>

					ArrayList<HashMap<String, Object>> storeList = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> storeMap = new HashMap<String, Object>();
					// 将 店铺列表的所点击的第 n个店铺的的信息 抽出来 抽取 回调 返回给 mainActivity 传递
					storeMap.put("id", resource.get(position).get("id"));
					storeMap.put("name", resource.get(position).get("name"));
					storeMap.put("details",
							resource.get(position).get("details"));
					storeMap.put("address",
							resource.get(position).get("address"));
					storeMap.put("xlong", resource.get(position).get("xlong"));
					storeMap.put("xlat", resource.get(position).get("xlat"));
					storeMap.put("oknums", resource.get(position).get("oknums"));
					storeMap.put("pic_url",
							resource.get(position).get("pic_url"));
					storeMap.put("_level", resource.get(position).get("_level"));
					storeMap.put("praise", Praise[position]);
					storeMap.put("style", resource.get(position).get("style"));
					storeMap.put("collection", Collection[position]);
					storeMap.put("stylename",
							resource.get(position).get("stylename"));
					storeMap.put("currentPosition", position);

					storeList.add(storeMap);

					list.add(storeList);

					bundle.putParcelableArrayList("listinfo", list);
					// bundle.putLong("currentPage", currentPage);
					intent.putExtra("bundlelist", bundle);

					HaveFunListActivity.this.startActivityForResult(intent, 1);
				}
			});

			// 为点赞按钮添加监听事件 ，当点击时，进行 相应操作记录
			pointButtonView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// TODO Auto-generated method stub
					// Toast.makeText(HaveFunListActivity.this, "1", 1).show();
					// Log.e("click", "click1");
					if (msharepreference.getStringProperty("sessionkey") == "") {
						mTool.showDialog(Login.class, "您还未登入", "请登入或注册");
						return;
					}

					if (/* !Praise[position].equals("") */!v.getTag(
							R.string.ispoint).equals("")) {// 不为""
						// Praise[position]!=0

						pointButton
								.setBackgroundResource(R.drawable.wudi_hollow_praise);
						pointTextView.setText("点赞");

						// Praise[position] = "";
						obj.put("storeIsPoint", "");
						v.setTag(R.string.ispoint, "");
						JSONObject jsonObject = Tool.getJSONObject(2,
								"store_id", Id[position] + "", "type", "0",
								"ids", "delete_parise", "user_id",
								msharepreference.getStringProperty("user_id"));
						CancelPraise cancelPraise = new CancelPraise(position);
						cancelPraise.execute(jsonObject);

					} else {
						pointButton
								.setBackgroundResource(R.drawable.wudi_solid_praise);
						pointTextView.setText("已赞");

						// Praise[position] = "1";
						obj.put("storeIsPoint", "1");
						v.setTag(R.string.ispoint, "1");
						JSONObject jsonObject = Tool.getJSONObject(2,
								"store_id", Id[position] + "", "type", "0",
								"ids", "praise_store", "user_id",
								msharepreference.getStringProperty("user_id"));
						PraiseStoreAsyncTask praiseStoreAsyncTask = new PraiseStoreAsyncTask(
								position);
						praiseStoreAsyncTask.execute(jsonObject);

					}

				}
			});

			// 为收藏按钮添加监听事件，点击时，进行相应的数据操作
			collectButtonView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// TODO Auto-generated method stub

					if (msharepreference.getStringProperty("sessionkey") == "") {
						mTool.showDialog(Login.class, "您还未登入", "请登入或注册");
						return;
					}

					if (/* !Collection[position].equals("") */!v.getTag(
							R.string.iscollect).equals("")) {
						collectButton
								.setBackgroundResource(R.drawable.wudi_hollow_star);
						collectTextView.setText("收藏 ");

						// Collection[position] = "";
						obj.put("sotreIsCollect", "");
						v.setTag(R.string.iscollect, "");
						JSONObject jsonObject = Tool.getJSONObject(2,
								"store_id", Id[position] + "", "type", "0",

								"ids", "delete_collection", "user_id",
								msharepreference.getStringProperty("user_id"));
						CancelCollect cancelCollect = new CancelCollect(
								position);
						cancelCollect.execute(jsonObject);
					} else {
						collectButton
								.setBackgroundResource(R.drawable.wudi_solid_star);
						collectTextView.setText("已收藏");
						// Collection[position] = "1";
						obj.put("sotreIsCollect", "1");
						v.setTag(R.string.iscollect, "1");
						JSONObject jsonObject = Tool.getJSONObject(2,
								"store_id", Id[position] + "", "type", "0",

								"ids", "collection_store", "user_id",
								msharepreference.getStringProperty("user_id"));
						CollectStoreAsyncTask collectStoreAsyncTask = new CollectStoreAsyncTask(
								position);
						collectStoreAsyncTask.execute(jsonObject);

					}

				}
			});

			// 为评论按钮添加监听事件，进行相应的数据操作，跳转到 用户评论 界面
			commentButtonView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putInt("store_id", Id[position]);
					intent.putExtra("storeCommentBundle", bundle);
					intent.setClass(HaveFunListActivity.this,
							Commentdetail.class);
					HaveFunListActivity.this.startActivity(intent);
				}
			});

			return convertView;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		String collect;
		String praise;
		int currentPosition1;
		switch (resultCode) {
		case 1:
			Bundle bundle = data.getExtras();
			currentPosition1 = bundle.getInt("currentPosition");
			collect = bundle.getString("collection");
			praise = bundle.getString("praise");
			// currentPage = bundle.getInt("currentPageReturn");
			// loadMoreTask();
			list.get(currentPosition1).remove("storeIsPoint");
			list.get(currentPosition1).put("storeIsPoint", praise);
			list.get(currentPosition1).remove("sotreIsCollect");
			list.get(currentPosition1).put("sotreIsCollect", collect);
			// String name = (String)
			// list.get(currentPosition1).get("storeName");
			myListViewAdapter.notifyDataSetChanged();

			break;

		default:
			break;
		}
	}

	private class PraiseStoreAsyncTask extends
			AsyncTask<JSONObject, Integer, String> {
		private int position;

		public PraiseStoreAsyncTask(int mposition) {

			position = mposition;
		}

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

					Praise[position] = "1";

					// j = 1;
				} else {
					Toast.makeText(HaveFunListActivity.this, "点赞失败", 0).show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private class CollectStoreAsyncTask extends
			AsyncTask<JSONObject, Integer, String> {
		private int position;

		public CollectStoreAsyncTask(int mposition) {

			position = mposition;
		}

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
					Collection[position] = "1";

				} else {
					Toast.makeText(HaveFunListActivity.this, "收藏失败", 0).show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private class CancelPraise extends AsyncTask<JSONObject, Integer, String> {
		private int position;

		public CancelPraise(int myPosition) {
			position = myPosition;

		}

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

					Praise[position] = "";

				} else {
					Toast.makeText(HaveFunListActivity.this, "取消点赞失败", 0)
							.show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private class CancelCollect extends AsyncTask<JSONObject, Integer, String> {
		private int position;

		public CancelCollect(int myPosition) {

			position = myPosition;

		}

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
					Collection[position] = "";

				} else {
					Toast.makeText(HaveFunListActivity.this, "取消收藏失败", 0)
							.show();
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
		if (mTool.haveShowDialog()) {
			mTool.removeDialog();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
		System.gc();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			System.gc();
		}
		return super.onKeyDown(keyCode, event);
	}

	// @Override
	// protected void onActivityResult(int arg0, int arg1, Intent arg2) {
	// // TODO Auto-generated method stub
	// if (arg1 == 1) {
	// // 当 发送完我的评论后 返回 userComment 界面 需要刷新数据 从新加载 所以 需要 refresh（）一次；
	// refreshTask();
	//
	// }
	// }

	// private boolean user = false;
	//
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if (msharepreference.getStringProperty("user_id").equals("")) {
			UUID uuid = UUID.randomUUID();
			String user_id = uuid.toString();
			msharepreference.setStringProperty("user_id", user_id);
			Constant.user = true;
		}
		refresh();
	}

}
