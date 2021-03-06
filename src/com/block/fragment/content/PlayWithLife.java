package com.block.fragment.content;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ab.bitmap.AbImageDownloader;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener;
import com.ab.task.AbTaskListener;
import com.ab.util.AbImageUtil;
import com.ab.util.AbViewHolder;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ab.view.sliding.AbSlidingPlayView;
import com.block.jhguide.R;
import com.fanjie.activity.rightSlider.Login;
import com.lidroid.xutils.BitmapUtils;
import com.wudi.comment.activity.MyStoreComment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ListView;

public class PlayWithLife extends Fragment implements OnHeaderRefreshListener {
	// type为2时，表示选择 的即将跳转到详情 ，其中 top几张可滑动的图片的点击和bottom list
	// 每一个item的前2个的点击跳转效果的一样 都是 跳转到各自的详情页面

	public interface OnPlayWithLifeItemClickListener {
		public void onclick(int type, int position,
				ArrayList<HashMap<String, Object>> info);
	}

	public void setOnPlayWithLifeClickListener(
			OnPlayWithLifeItemClickListener listener) {
		delegate = listener;
	}

	// 向控制层activity传递点击消息
	OnPlayWithLifeItemClickListener delegate;

	private Activity mActivity = null;

	private AbImageDownloader abImageDownloader;
	// refreshTask中需用到 与数据源密切相关
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	private AbPullToRefreshView mAbPullToRefreshView = null;
	private ImageListAdapter myListViewAdapter = null;
	private ListView mListView = null;
	private AbSlidingPlayView mSlidingPlayView = null;
	private LayoutInflater mInflater;
	private int count = 0;

	private String user_id;
	private Tool tool;

	// 传递 List数据所设 的变量
	private List<Map<String, Object>> transmitTopList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> transmitBottomList = new ArrayList<Map<String, Object>>();

	private Msharepreference sharedPreferences;

	private NetWorkJudgeTool netWorkJudge;

	// private BroadcastReceiver connectionReceiver;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		mActivity = this.getActivity();
		View view = inflater
				.inflate(R.layout.view_tabcontent_playwithfun, null);
		// 获取ListView对象
		mAbPullToRefreshView = (AbPullToRefreshView) view
				.findViewById(R.id.mPullRefreshView);
		mListView = (ListView) view.findViewById(R.id.mListView);
		tool = new Tool(mActivity);
		netWorkJudge = new NetWorkJudgeTool();

		// if( netWorkJudge.isNetWorkConnected(getActivity())){
		//
		// if(!netWorkJudge.isMobileConnected(getActivity()) &&
		// !netWorkJudge.isWifiConnected(getActivity())){
		//
		// Toast.makeText(getActivity(), "当前网络连接不可用", Toast.LENGTH_SHORT);
		// //为 玩转生活加载本地图片 避免 没网络连接 而闪退
		//
		// }
		// else {
		// //当前网络已连接 可用 开启异步
		// ZmjAsyncTask zmjAsyncTask = new ZmjAsyncTask();
		// JSONObject js = new JSONObject();
		// // 获取 user_id 和 user_name
		// sharedPreferences = new Msharepreference(getActivity(),
		// "information");
		// user_id = sharedPreferences.getStringProperty("user_id");
		// try {
		// js.put("user_id", user_id + "");
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// zmjAsyncTask.execute("home_three_recommen", js.toString());
		//
		//
		// }
		//
		// }
		// else {
		//
		//
		// Toast.makeText(getActivity(), "当前网络未连接", Toast.LENGTH_SHORT);
		// startActivity(new Intent(
		// android.provider.Settings.ACTION_WIFI_SETTINGS));
		//
		//
		// }

		// 注册 connectionReceiver
		// IntentFilter intentFilter = new IntentFilter();
		// intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		// getActivity().registerReceiver(connectionReceiver, intentFilter);

		// connectionReceiver = new BroadcastReceiver() {
		//
		// @Override
		// public void onReceive(Context context, Intent intent) {
		// // TODO Auto-generated method stub
		//
		// if( netWorkJudge.isNetWorkConnected(context)){
		//
		// if(!netWorkJudge.isMobileConnected(context) &&
		// !netWorkJudge.isWifiConnected(context)){
		//
		// Toast.makeText(getActivity(), "当前网络连接不可用", Toast.LENGTH_SHORT);
		// //为 玩转生活加载本地图片 避免 没网络连接 而闪退
		//
		// }
		// else {
		// //当前网络已连接 可用 开启异步
		// ZmjAsyncTask zmjAsyncTask = new ZmjAsyncTask();
		// JSONObject js = new JSONObject();
		// // 获取 user_id 和 user_name
		// sharedPreferences = new Msharepreference(getActivity(),
		// "information");
		// user_id = sharedPreferences.getStringProperty("user_id");
		// try {
		// js.put("user_id", user_id + "");
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// zmjAsyncTask.execute("home_three_recommen", js.toString());
		//
		//
		// }
		//
		// }
		// else {
		//
		//
		// Toast.makeText(getActivity(), "当前网络未连接", Toast.LENGTH_SHORT);
		// startActivityForResult(new Intent(
		// android.provider.Settings.ACTION_WIRELESS_SETTINGS), 1);
		//
		//
		// }
		//
		// }
		//
		// };
		//
		// connectionReceiver.onReceive(getActivity(), null);

		ZmjAsyncTask zmjAsyncTask = new ZmjAsyncTask();
		JSONObject js = new JSONObject();
		// 获取 user_id 和 user_name
		sharedPreferences = new Msharepreference(getActivity(), "information");
		user_id = sharedPreferences.getStringProperty("user_id");
		try {
			js.put("user_id", user_id + "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		zmjAsyncTask.execute("home_three_recommen", js.toString());

		initBasicView();
		setListener();
		return view;

	}

	private void initBasicView() {
		// TODO Auto-generated method stub

		// 设置上拉刷新 下拉加载的 进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));

		// 使用自定义的Adapter
		myListViewAdapter = new ImageListAdapter(mActivity, list,
				R.layout.item_list_playwithfun, new String[] { "itemsIcon_1",
						"itemsIcon_2", "itemsIcon_3" }, new int[] {
						R.id.itemsIcon_1, R.id.itemsIcon_2, R.id.itemsIcon_3 });

		// 第一次更新数据
		refreshTask();

		// 组和个AbSlidingPlayView
		mSlidingPlayView = new AbSlidingPlayView(getActivity());

		// mSlidingPlayView.setNavHorizontalGravity(Gravity.RIGHT);
		mSlidingPlayView.startPlay();
		// 设置高度
		mSlidingPlayView.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.FILL_PARENT, 490));
		mListView.addHeaderView(mSlidingPlayView);
		// 解决冲突问题
		mSlidingPlayView.setParentListView(mListView);
		// mListView的适配
		mListView.setAdapter(myListViewAdapter);
	}

	private void setListener() {

		mSlidingPlayView
				.setOnItemClickListener(new AbSlidingPlayView.AbOnItemClickListener() {

					@Override
					public void onClick(int position) {
						// 调用MainActivity中实现接口重写的方法 对即将跳转的页面的参数进行设置
						ArrayList<HashMap<String, Object>> topPointedList = new ArrayList<HashMap<String, Object>>();
						HashMap<String, Object> topPointedMap = new HashMap<String, Object>();
						// 将 顶部的 n个店铺的消息 抽出来 抽取 你点击的 店铺的信息

						topPointedMap.put("id", transmitTopList.get(position)
								.get("id"));
						topPointedMap.put("name", transmitTopList.get(position)
								.get("name"));
						topPointedMap.put("details",
								transmitTopList.get(position).get("details"));
						topPointedMap.put("address",
								transmitTopList.get(position).get("address"));
						topPointedMap.put("xlong", transmitTopList
								.get(position).get("xlong"));
						topPointedMap.put("xlat", transmitTopList.get(position)
								.get("xlat"));
						topPointedMap.put("oknums",
								transmitTopList.get(position).get("oknums"));
						topPointedMap.put("pic_url",
								transmitTopList.get(position).get("pic_url"));
						topPointedMap.put("_level",
								transmitTopList.get(position).get("_level"));
						topPointedMap.put("praise",
								transmitTopList.get(position).get("praise"));
						topPointedMap.put("style", transmitTopList
								.get(position).get("style"));
						topPointedMap
								.put("collection", transmitTopList
										.get(position).get("collection"));
						topPointedMap.put(
								"activity_info",
								transmitTopList.get(position).get(
										"activity_info"));
						topPointedMap.put("phone", transmitTopList
								.get(position).get("phone"));
						// topPointedMap.put("stylename",
						// transmitTopList.get(position).get("stylename"));
						topPointedMap.put("currentPosition", position);
						topPointedList.add(topPointedMap);

						delegate.onclick(2, position, topPointedList);

					}
				});

		// 设置下拉头部的viewpage的监听器
		mAbPullToRefreshView
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

					@Override
					public void onHeaderRefresh(AbPullToRefreshView view) {
						refreshTask();
					}
				});

		mAbPullToRefreshView.setLoadMoreEnable(false);

	}

	public void addMSlidingPlayView(List<Map<String, Object>> topInforList) {
		// TODO Auto-generated method stub

		if (count >= 2) {
			mSlidingPlayView.removeAllViews();
		}
		for (int i = 0; i < topInforList.size(); i++) {

			// BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
			final View mPlayView_i = mInflater.inflate(
					R.layout.slider_showimg_item, null);
			ImageView mPlayImage_i = (ImageView) mPlayView_i
					.findViewById(R.id.mPlayImage);
			TextView mPlayText_i = (TextView) mPlayView_i
					.findViewById(R.id.mPlayText);
			mPlayText_i.setText((CharSequence) topInforList.get(i).get("name"));
			// abImageDownloader.setWidth(mPlayImage_i.getWidth());
			// abImageDownloader.setHeight(mPlayImage_i.getHeight());
			// abImageDownloader.setType(AbImageUtil.SCALEIMG);
			// abImageDownloader.setLoadingImage(R.drawable.image_loading);
			// abImageDownloader.setErrorImage(R.drawable.empty_photo);
			// abImageDownloader.setNoImage(R.drawable.image_no);
			abImageDownloader.display(mPlayImage_i,
					"http://61.142.71.63:9090/jhun_play/"
							+ topInforList.get(i).get("pic_url"));
			mSlidingPlayView.addView(mPlayView_i);
		}

		mSlidingPlayView.setNavHorizontalGravity(Gravity.RIGHT);

	}

	private void refreshTask() {
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public void update(List<?> arg0) {
				// TODO Auto-generated method stub
				List<Map<String, Object>> newList = (List<Map<String, Object>>) arg0;
				list.clear();
				if (newList != null && newList.size() > 0) {
					list.addAll(newList);
					myListViewAdapter.notifyDataSetChanged();
					newList.clear();
				}
				mAbPullToRefreshView.onHeaderRefreshFinish();
			}

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				List<Map<String, Object>> newList = null;
				try {

					// ArrayList<Map<String, Object>> topInforList = new
					// ArrayList<Map<String,Object>>();
					// topInforList=toolManager("home_three_recommen");
					// 回调 mainActivity 传递的 顶部的 view的参数
					// transmitTopList = topInforList;

					count++;
					// 动态添加 顶部的 view
					// addMSlidingPlayView(topInforList);
					// 获取底部 推荐的 信息
					ArrayList<Map<String, Object>> bottomInforList = new ArrayList<Map<String, Object>>();
					Map<String, Object> map = null;
					newList = new ArrayList<Map<String, Object>>();
					bottomInforList = toolManager("home_commen");

					// 设置回调 mainActivity 传递顶部的view的参数
					transmitBottomList = bottomInforList;

					// 从 10个 obj中 分别获取 两列的 pic_url
					for (int i = 0; i < 9; i += 2) {
						map = new HashMap<String, Object>();
						map.put("itemsIcon_1",
								bottomInforList.get(i).get("pic_url"));
						map.put("itemsIcon_2",
								bottomInforList.get(i + 1).get("pic_url"));
						newList.add(map);
					}

				} catch (Exception e) {
				}
				return newList;
			}
		});
		mAbTask.execute(item);
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		refreshTask();
	}

	public class ImageListAdapter extends BaseAdapter {

		// private static final boolean D = Constant.DEBUG;

		private Context mContext;
		// xml转View对象
		private LayoutInflater mInflater;
		// 单行的布局
		private int mResource;
		// 列表展现的数据
		private List mData;
		// Map中的key
		private String[] mFrom;
		// view的id
		private int[] mTo;

		// 程序窗口size
		// MyApplication.CGSize size;
		// 图片加载器
		// private BitmapUtils bitmapUtils;

		public ImageListAdapter(Context context, List data, int resource,
				String[] from, int[] to) {
			this.mContext = context;
			this.mData = data;
			this.mResource = resource;
			this.mFrom = from;
			this.mTo = to;
			// 用于将xml转为View
			this.mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			abImageDownloader = new AbImageDownloader(context);
			// this.bitmapUtils = new BitmapUtils(context);
			// this.size = MyApplication.getWindowsize(context);
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {

			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				// 使用自定义的list_items作为Layout
				convertView = mInflater.inflate(mResource, parent, false);
				holder = new ViewHolder();
				holder.itemsIcon_1 = (ImageView) /*
												 * AbViewHolder.get(convertView,
												 * mTo[0]);
												 */convertView
						.findViewById(R.id.itemsIcon_1);
				holder.itemsIcon_2 = /* AbViewHolder.get(convertView, mTo[1]); */(ImageView) convertView
						.findViewById(R.id.itemsIcon_2);
				holder.itemsIcon_3 = (ImageView) /*
												 * AbViewHolder.get(convertView,
												 * mTo[2]);
												 */convertView
						.findViewById(R.id.itemsIcon_3);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 此处获得positon处 前 imageview 的资源 List 准备进行传递
			final ArrayList<HashMap<String, Object>> bottomlist_1 = new ArrayList<HashMap<String, Object>>();
			final ArrayList<HashMap<String, Object>> bottomlist_2 = new ArrayList<HashMap<String, Object>>();
			bottomlist_1.add((HashMap<String, Object>) transmitBottomList
					.get(2 * position));
			bottomlist_2.add((HashMap<String, Object>) transmitBottomList
					.get(2 * position + 1));

			// 此处获取position 第三个 imageview 的类别信息 应有 style和 stylename
			final ArrayList<HashMap<String, Object>> bottomlist_3 = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("style", transmitBottomList.get(2 * position).get("style"));
			int a = (Integer) transmitBottomList.get(2 * position).get("style");
			map.put("stylename",
					transmitBottomList.get(2 * position).get("stylename"));

			bottomlist_3.add(map);

			holder.itemsIcon_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (delegate != null) {
						delegate.onclick(2, 1, bottomlist_1);
					}

				}
			});

			holder.itemsIcon_2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (delegate != null) {
						delegate.onclick(2, 2, bottomlist_2);
					}

				}
			});

			holder.itemsIcon_3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// bottomlist_3
					// 调用MainActivity中实现接口重写的方法 对即将跳转的页面的参数进行设置
					if (delegate != null) {
						delegate.onclick(1, position + 1, null);
					}

				}
			});

			// 把屏幕三等分 分给三个Button
			// LayoutParams params = itemsIcon_1.getLayoutParams();
			// params.width = (int) (size.width / 3);
			// itemsIcon_1.setLayoutParams(params);
			//
			// params = itemsIcon_2.getLayoutParams();
			// params.width = (int) (size.width / 3);
			// itemsIcon_2.setLayoutParams(params);
			//
			// params = itemsIcon_3.getLayoutParams();
			// params.width = (int) (size.width / 3);
			// itemsIcon_3.setLayoutParams(params);
			// itemsIcon_1.setLayoutParams(params);

			// 获取该行的数据
			final Map<String, Object> obj = (Map<String, Object>) mData
					.get(position);
			String imageUrl_1 = (String) obj.get("itemsIcon_1");
			String imageUrl_2 = (String) obj.get("itemsIcon_2");
			// abImageDownloader.setWidth(holder.itemsIcon_1.getWidth());
			// abImageDownloader.setHeight(holder.itemsIcon_1.getHeight());
			// abImageDownloader.setType(AbImageUtil.SCALEIMG);
			// abImageDownloader.setLoadingImage(R.drawable.image_loading);
			// abImageDownloader.setErrorImage(R.drawable.empty_photo);
			// abImageDownloader.setNoImage(R.drawable.image_no);
			abImageDownloader.display(holder.itemsIcon_1,
					"http://61.142.71.63:9090/jhun_play/" + imageUrl_1);
			// abImageDownloader.setWidth(holder.itemsIcon_2.getWidth());
			// abImageDownloader.setHeight(holder.itemsIcon_2.getHeight());
			// abImageDownloader.setType(AbImageUtil.SCALEIMG);
			// abImageDownloader.setLoadingImage(R.drawable.image_loading);
			// abImageDownloader.setErrorImage(R.drawable.empty_photo);
			// abImageDownloader.setNoImage(R.drawable.image_no);
			abImageDownloader.display(holder.itemsIcon_2,
					"http://61.142.71.63:9090/jhun_play/" + imageUrl_2);
			switch (position) {
			case 0:
				// bitmapUtils.display(holder.itemsIcon_3,
				// "assets/image/playwithlife_listitem3_0.png");
				holder.itemsIcon_3.setImageDrawable(getResources().getDrawable(
						R.drawable.playwithlife_listitem3_0));
				break;
			case 1:
				// bitmapUtils.display(holder.itemsIcon_3,
				// "assets/image/playwithlife_listitem3_1.png");
				holder.itemsIcon_3.setImageDrawable(getResources().getDrawable(
						R.drawable.playwithlife_listitem3_1));
				break;
			case 2:
				// bitmapUtils.display(holder.itemsIcon_3,
				// "assets/image/playwithlife_listitem3_2.png");
				holder.itemsIcon_3.setImageDrawable(getResources().getDrawable(
						R.drawable.playwithlife_listitem3_2));
				break;
			case 3:
				// bitmapUtils.display(holder.itemsIcon_3,
				// "assets/image/playwithlife_listitem3_3.png");
				holder.itemsIcon_3.setImageDrawable(getResources().getDrawable(
						R.drawable.playwithlife_listitem3_3));

				break;
			case 4:
				// bitmapUtils.display(holder.itemsIcon_3,
				// "assets/image/playwithlife_listitem3_4.png");
				holder.itemsIcon_3.setImageDrawable(getResources().getDrawable(
						R.drawable.playwithlife_listitem3_4));

				break;
			default:

				break;
			}

			return convertView;
		}

		public final class ViewHolder {
			public ImageView itemsIcon_1;
			public ImageView itemsIcon_2;
			public ImageView itemsIcon_3;
		}
	}

	private ArrayList<Map<String, Object>> toolManager(String ids) {

		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String result = null;

		// // 获取 user_id 和 user_name
		// sharedPreferences = new Msharepreference(getActivity(),
		// "information");
		// String user_id = sharedPreferences.getStringProperty("user_id");

		// params的参数，含图片的ID，评论数量的最大值和最小值
		JSONObject params = new JSONObject();
		try {
			params.put("user_id", user_id + "");
			params.put("ids", ids + "");
			result = Tool.registPost(params);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		list = JsonAnalysis(result);
		return list;
	}

	// public static String Register(JSONObject jsonObject) {
	// String result = null;
	// try {
	// List<NameValuePair> parms = new ArrayList<NameValuePair>();
	// String id = jsonObject.optString("ids");
	// jsonObject.remove("ids");
	// HttpClient client = new DefaultHttpClient();
	// HttpPost post = new HttpPost(Constant.URL2);
	// parms.add(new BasicNameValuePair("register", "true"));
	// parms.add(new BasicNameValuePair("ids", id));
	// parms.add(new BasicNameValuePair("params", jsonObject.toString()));
	// post.setEntity(new UrlEncodedFormEntity(parms));
	// HttpResponse response = client.execute(post);
	// // int code = response.getStatusLine().getStatusCode();
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

	public ArrayList<Map<String, Object>> JsonAnalysis(String str) {

		ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		if (str == null) {
			try {
				throw new Exception("null String");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return dataList;
		}
		try {

			JSONArray jsonObjs = new JSONObject(str).getJSONArray("obj");

			for (int i = 0; i < jsonObjs.length(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				JSONObject jsonObj = (JSONObject) jsonObjs.opt(i);
				int id = jsonObj.getInt("id");
				String name = jsonObj.getString("name");
				String details = jsonObj.getString("details");
				String address = jsonObj.getString("address");
				double xlong = jsonObj.getDouble("xlong");
				double xlat = jsonObj.getDouble("xlat");
				int oknums = jsonObj.getInt("oknums");
				int style = jsonObj.getInt("style");
				// String stylename = jsonObj.getString("stylename");
				String pic_url = jsonObj.getString("pic_url");
				float _level;
				if (jsonObj.optString("_level").contains("null")) {
					_level = 0;
				} else {
					_level = (float) jsonObj.optDouble("_level");
				}

				String praise;
				if (jsonObj.optString("praise").contains("null")) {
					praise = "";
				} else {

					praise = jsonObj.optString("praise");
				}

				String collection;
				if (jsonObj.optString("collection").contains("null")) {
					collection = "";

				} else {
					collection = jsonObj.optString("collection");
				}

				String activity_info = jsonObj.optString("activity_info");
				String phone = jsonObj.optString("phone");
				map.put("activity_info", activity_info);
				map.put("phone", phone);
				map.put("id", id);
				map.put("name", name);
				map.put("details", details);
				map.put("address", address);
				map.put("xlong", xlong);
				map.put("xlat", xlat);
				map.put("oknums", oknums);
				map.put("style", style);
				// map.put("stylename", stylename);
				map.put("pic_url", pic_url);
				map.put("_level", _level);
				map.put("praise", praise);
				map.put("collection", collection);
				map.put("currentPosition", 1);
				dataList.add(map);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataList;

	}

	public class ZmjAsyncTask extends AsyncTask<String, Integer, String> {

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
			// TODO Auto-generated method stub
			ArrayList<Map<String, Object>> topInforList = new ArrayList<Map<String, Object>>();
			topInforList = JsonAnalysis(result);
			// 回调 mainActivity 传递的 顶部的 view的参数
			transmitTopList = topInforList;
			// 动态添加 顶部的 view
			addMSlidingPlayView(topInforList);

		}

	}

	// /* (non-Javadoc)
	// * @see android.support.v4.app.Fragment#onDestroy()
	// */
	// @Override
	// public void onDestroy() {
	// // TODO Auto-generated method stub
	// super.onDestroy();
	//
	// //取消 注册 connectionReceiver
	// if (connectionReceiver != null) {
	// getActivity().unregisterReceiver(connectionReceiver);
	// }
	//
	//
	// }

}
