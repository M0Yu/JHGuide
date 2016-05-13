package com.block.activity.government;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jhunplay.com.db.NotificationService;
import jhunplay.com.db.XGNotification;
import jhunplay.com.fanjie.Constant.Constant;
import jhunplay.com.fanjie.adapter.MyAdapter;
import jhunplay.com.fanjie.ovrideLayout.MarqueeText;
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

import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener;
import com.ab.task.AbTaskListener;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ab.view.sliding.AbTabItemView;
import com.block.fragment.content.Government;
import com.block.jhguide.R;
import com.fanjie.activity.infor.MyActivity;
//import com.qq.xgdemo.MainActivity1.MsgReceiver;
//import com.qq.xgdemo.MainActivity1.MsgReceiver;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGLocalMessage;
import com.tencent.android.tpush.XGPushManager;
//import com.qq.xgdemo.common.NotificationService;
//import com.qq.xgdemo.po.XGNotification;
import com.tencent.android.tpush.XGPushShowedResult;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class XueGongBu extends Fragment implements OnHeaderRefreshListener,
		OnFooterLoadListener {
	private TextView textview;
	private MarqueeText autotextview;
	private ArrayList<Map<String, Object>> lists;
	private MyAdapter adapter = null;
	private ListView listview;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private int currentPage = 1;
	private MarqueeText Auto;
	private Context mContext;
	private Msharepreference msharepreference;

	private WindowManager.LayoutParams wmParams = null;
	private boolean isFirstPosition = true;

	// ImageView的alpha值
	private int mAlpha = 0;
	private boolean isHide;
	private ImageView btn = null;

	HttpClient client = new DefaultHttpClient();
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> addlist = new ArrayList<Map<String, Object>>();
	private int currentPageStart = 0;
	private int currentPageEnd = 2;
	private String sessionkey;
	private int WebPage;
	private List<Map<String, Object>> SumList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> newSumList = new ArrayList<Map<String, Object>>();
	private Intent intent = new Intent("com.qq.xgdemo.activity.UPDATE_LISTVIEW");
	private List<XGNotification> notification = new ArrayList<XGNotification>();
	private NotificationService notificationService;
	private int allRecorders = 0;// 全部记录数
	private MsgReceiver updateListViewReceiver;
	private AbTabItemView tabView;

	public XueGongBu(int page) {

		this.WebPage = page;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		loadMoreTask();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.wshview_topcontent_xuegongbu,
				null);
		mAbPullToRefreshView = (AbPullToRefreshView) view
				.findViewById(R.id.mPullRefreshView1);

		listview = (ListView) view.findViewById(R.id.LifeIfoLv);
		textview = (TextView) view.findViewById(R.id.LifeInfoTxt1);
		autotextview = (MarqueeText) view.findViewById(R.id.auto);
		// Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),
		// "fonts/lanting.TTF");
		// textview.setTypeface(typeface);
		// autotextview.setTypeface(typeface);

		// autotextview.setText("00000000000000000000000000");
		msharepreference = new Msharepreference(getActivity(), "information");
		sessionkey = msharepreference.getStringProperty("sessionkey");
		// loadMoreTask();
		adapter = new MyAdapter(getActivity(), list, R.layout.listadapter,
				new String[] { "image", "title", "info_time" },
				new int[] { R.id.schinfoimage, R.id.LifeInfoTxt2,
						R.id.LifeInfoTxt3 });
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), MyActivity.class);
				getActivity().startActivity(intent);
			}

		});

		// setAutoTxt(null);
		// Auto = (MarqueeText) view.findViewById(R.id.auto);
		// 0.注册数据更新监听器
		updateListViewReceiver = new MsgReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.block.jhguide.activity.UPDATE_LISTVIEW");
		getActivity().registerReceiver(updateListViewReceiver, intentFilter);
		autotextview.startFor0();
		SetTextAuto();
		initBasicView();
		setListener();
		// RegisterPush();
		// initLocalMessage();
		// initFloatView();
		return view;

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void initBasicView() {
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
	}

	private void setListener() {
		mAbPullToRefreshView
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

					@Override
					public void onHeaderRefresh(AbPullToRefreshView view) {
						// TODO Auto-generated method stub
						refreshTask();
					}
				});

		mAbPullToRefreshView
				.setOnFooterLoadListener(new OnFooterLoadListener() {

					@Override
					public void onFooterLoad(AbPullToRefreshView arg0) {
						// TODO Auto-generated method stub
						loadMoreTask();
					}
				});

		autotextview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ArrayList<String> Info1 = new ArrayList<String>();
				ArrayList Info = new ArrayList();
				String title = null;
				switch (WebPage) {
				case 1:
					title = "生活信息";
					break;
				case 2:
					title = "教学信息";
					break;
				case 3:
					title = "学工活动";
					break;
				case 4:
					title = "社团活动";
					break;
				case 5:
					title = "失物招领";
					break;

				default:
					break;
				}
				Info1.add("");
				Info1.add(title);
				Info1.add(autotextview.getText().toString());
				Info1.add("");
				Info1.add(textview.getText().toString());
				Info.addAll(Info1);
				Bundle mBundle = new Bundle();
				mBundle.putParcelableArrayList("Info", Info);
				Intent intent = new Intent(getActivity(), MyActivity.class);
				intent.putExtras(mBundle);
				getActivity().startActivity(intent);
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ArrayList<String> Info1 = new ArrayList<String>();
				ArrayList Info = new ArrayList();
				Info1.add(SumList.get(arg2).get("pic_url").toString());
				Info1.add(SumList.get(arg2).get("info_style").toString());
				Info1.add(SumList.get(arg2).get("details").toString());
				Info1.add(SumList.get(arg2).get("info_part").toString());
				Info1.add(SumList.get(arg2).get("info_time").toString());
				Info.addAll(Info1);
				Bundle mBundle = new Bundle();
				mBundle.putParcelableArrayList("Info", Info);
				Intent intent = new Intent(getActivity(), MyActivity.class);
				intent.putExtras(mBundle);
				getActivity().startActivity(intent);
			}

		});

		listview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				getActivity().onTouchEvent(arg1);
				return false;
			}
		});
		listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void refreshTask() {
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public void update(List<?> paramList) {
				List<Map<String, Object>> newList = (List<Map<String, Object>>) paramList;
				if (newList != null && newList.size() > 0) {
					list.addAll(newList);
					adapter.notifyDataSetChanged();
					newList.clear();
				}
				if (newList.size() < Constant.COUNT) {
					Toast.makeText(getActivity(), Constant.NOMOREINFR, 0)
							.show();
				}
				mAbPullToRefreshView.onHeaderRefreshFinish();

			}

			@Override
			public List<?> getList() {
				List<Map<String, Object>> newList = new ArrayList<Map<String,Object>>();
				try {
					currentPage++;
					Thread.sleep(1000);
					newList = new ArrayList<Map<String, Object>>();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("ids", "school_info");
					jsonObject.put("style", WebPage);
					jsonObject.put("min", currentPageStart);
					jsonObject.put("max", Constant.COUNT);
					// currentPageStart+=2;
					// jsonObject.put("sessionkey", sessionkey);
					// String result = Tool.httpPost(jsonObject);
					// newList = Tool.Json(result, "details", "info_time");

				} catch (Exception e) {
					currentPage--;
					newList.clear();

				}
				return newList;
			};
		});

		mAbTask.execute(item);

	}

	public void loadMoreTask() {
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {
			@Override
			public void update(List<?> paramList) {
				List<Map<String, Object>> newList = (List<Map<String, Object>>) paramList;
				if (newList != null && newList.size() > 0) {
					list.addAll(newList);
					adapter.notifyDataSetChanged();
					newList.clear();
				}
				if (newList.size() == 0) {
					// Toast.makeText(getActivity(), Constant.NOMOREINFR,
					// 0).show();
				}
				mAbPullToRefreshView.onFooterLoadFinish();
			}

			@Override
			public List<?> getList() {
				List<Map<String, Object>> newList = new ArrayList<Map<String,Object>>();
				try {
					currentPage++;
					Thread.sleep(1000);
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("ids", "school_info");
					jsonObject.put("register", "true");
					jsonObject.put("style", WebPage);
					jsonObject.put("min", currentPageStart);
					jsonObject.put("max", Constant.COUNT);
					// jsonObject.put("sessionkey", sessionkey);
					currentPageStart += Constant.COUNT;
					String result = Tool.registPost(jsonObject);
					if (result == null) {
//						throw new Exception("internet wrong");
						return newList;
					}
					newList = Tool.Json(result, "title", "info_time",
							"pic_url");
					newSumList = Tool.Json(result, "details", "info_time",
							"info_part", "info_style", "pic_url");
					SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");

					for (int i = 0; i < newList.size(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						Map<String, Object> newmap = new HashMap<String, Object>();
						long time = Long.parseLong(newList.get(i)
								.get("info_time").toString());
						Date d = new Date(time);
						if (newList.get(i).get("pic_url") == "null") {
							map.put("image", "null");
						} else {
							if(newList.get(i).get("pic_url").toString().contains(",")){
							map.put("image", Constant.URL3
									+ newList.get(i).get("pic_url").toString()
											.split(",")[0]);
							}else{
								map.put("image", Constant.URL3
										+ newList.get(i).get("pic_url").toString());
							}
						}
						map.put("title", newList.get(i).get("title"));
						map.put("info_time", sdf.format(d));
						newList.set(i, map);
						newmap.put("details", newSumList.get(i).get("details"));
						newmap.put("pic_url", newSumList.get(i).get("pic_url"));
						newmap.put("info_time", sdf.format(d));
						newmap.put("info_part",
								newSumList.get(i).get("info_part"));
						newmap.put("info_style",
								newSumList.get(i).get("info_style"));
						newSumList.set(i, newmap);
					}
					SumList.addAll(newSumList);

				} catch (Exception e) {
					currentPage--;
					if (newList != null) {
						newList.clear();
					}

				}
				return newList;
			};
		});

		mAbTask.execute(item);
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		loadMoreTask();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		refreshTask();

	}

	protected String getStringHttp(JSONObject... params) {
		// TODO Auto-generated method stub
		String result = null;
		try {
			List<NameValuePair> parms = new ArrayList<NameValuePair>();
			String id = params[0].optString("ids");
			String register = params[0].optString("register");
			params[0].remove("ids");
			params[0].remove("regiter");
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constant.URL2);

			parms.add(new BasicNameValuePair("ids", id));
			parms.add(new BasicNameValuePair("register", register));
			parms.add(new BasicNameValuePair("params", params[0].toString()));
			post.setEntity(new UrlEncodedFormEntity(parms, HTTP.UTF_8));
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	protected void SetTextAuto() {
		notificationService = NotificationService.getInstance(getActivity());
		int count = notificationService.getCount();
		System.out.println("wwsshh------------->" + count);
		notification = NotificationService.getInstance(getActivity())
				.getScrollData(1, count, "");
		String title = null, content = null;
		switch (WebPage) {
		case 1:
			title = "生活信息";
			content = "这是一条关于生活信息的消息";
			Search(count, title, content);
			break;
		case 2:
			title = "教学信息";
			content = "这是一条关于教学信息的消息";
			Search(count, title, content);
			break;
		case 3:
			title = "学工活动";
			content = "这是一条关于学工活动的消息";
			Search(count, title, content);
			break;
		case 4:
			title = "社团活动";
			content = "这是一条关于社团活动的信息";
			Search(count, title, content);
			break;
		case 5:
			title = "失物招领";
			content = "这是一条关于失物招领的消息";
			Search(count, title, content);
			break;
		default:
			break;
		}
	}

	private void Search(int count, String title, String content) {
		int i = 0;
		if (count == 0) {
			autotextview.setText(content);
		} else {
			while (i < count) {
				if (title.equals(notification.get(i).getTitle())) {
					autotextview.setText(notification.get(i).getContent());
					textview.setText(notification.get(i).getUpdate_time());
					break;
				} else {
					i++;
				}
			}
			if (i >= count) {
				autotextview.setText(content);
			}
		}
	}

	public class MsgReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			allRecorders = notificationService.getCount();
			// System.out.println(allRecorders);
			// getNotificationswithouthint(id);
			SetTextAuto();
		}
	}

	public List<Map<String, Object>> getSumList() {
		return SumList;
	}

	public MarqueeText getautotext() {
		return autotextview;
	}

	public TextView gettextview() {
		return textview;
	}
}
