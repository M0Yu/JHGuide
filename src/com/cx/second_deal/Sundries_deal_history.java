package com.cx.second_deal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogRecord;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ab.activity.AbActivity;
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
import com.ab.view.titlebar.AbBottomBar;
import com.ab.view.titlebar.AbTitleBar;
import com.block.jhguide.R;
import com.fanjie.activity.rightSlider.lw_submit_myGoods;
import com.fanjie.activity.thoseyear.commentary;
import com.fanjie.activity.thoseyear.commentary_detials;
import com.tencent.android.tpush.horse.p;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract.Constants;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class Sundries_deal_history extends AbActivity implements
		OnHeaderRefreshListener, OnFooterLoadListener {
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private ImageListAdapter myListViewAdapter = null;
	private ListView mListView = null;
	private ImageButton returnButton;
	private Button addButton;
	private AbImageDownloader abImageDownloader;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private List<Map<String, Object>> list = null;
	private int currentPage = 1;
	private int pageSize = 4;
	private JSONArray jsonObjs;
	private ArrayList<Map<String, Object>> jsonList = new ArrayList<Map<String, Object>>();
	private Msharepreference msharepreference;

	// ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		msharepreference = new Msharepreference(this, "information");
		initBasecView();
		// TODO Auto-generated method stub
		myBottomSet();
		titleBar();
		myListViewAdapter.notifyDataSetChanged();
	}

	public void initBasecView() {
		setAbContentView(R.layout.mpullreshview);

		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView = (ListView) findViewById(R.id.list_paly_detail);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Sundries_deal_history.this,
						SundriesDetail.class);
				Bundle bundle = new Bundle();
				ArrayList list1 = new ArrayList();
				ArrayList<HashMap<String, Object>> sundriesList = new ArrayList<HashMap<String, Object>>();
				HashMap<String, Object> sundriesMap = new HashMap<String, Object>();

				sundriesMap.put("sundriesImageUrl",
						list.get(position).get("sundriesImageUrl"));
				sundriesMap.put("img_url", list.get(position).get("img_url"));
				sundriesMap.put("object_name",
						list.get(position).get("object_name"));
				sundriesMap.put("object_description",
						list.get(position).get("object_description"));
				sundriesMap.put("type", list.get(position).get("type"));
				sundriesMap.put("tel_type", list.get(position).get("tel_type"));
				sundriesMap.put("tel", list.get(position).get("tel"));

				sundriesList.add(sundriesMap);
				list1.add(sundriesList);
				bundle.putParcelableArrayList("listinfo", list1);
				intent.putExtra("bundlelist", bundle);
				Sundries_deal_history.this.startActivityForResult(intent, 1);
              
				// Sundries_deal_history.this.overridePendingTransition(
				// R.anim.appear_top_left_in,
				// R.anim.disappear_bottom_right_out);

			}
		});

		refreshTask();

		list = new ArrayList<Map<String, Object>>();
		myListViewAdapter = new ImageListAdapter(this, list,
				R.layout.item_list, new String[] { "sundriesImageUrl",
						"object_name", "object_description", "type","sub_time"}, new int[] {
						R.id.sundriesImage, R.id.sundriesName,
						R.id.sundriesDetail,R.id.type,R.id.sundriesTime });
		mListView.setAdapter(myListViewAdapter);

		// ���ü�����
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);

		// ���ý������ʽ
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));

	}


	
	public void refreshTask() {
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
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
					// TODO: handle exception

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
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public void update(List<?> paramList) {
				List<Map<String, Object>> newList = (List<Map<String, Object>>) paramList;
				// TODO Auto-generated method stub
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

					Map<String, Object> map = null;

				} catch (Exception e) {
					// TODO: handle exception
					currentPage--;
					newList.clear();
				}
				return newList;
			}
		});
		mAbTask.execute(item);
	}

	public ArrayList<Map<String, Object>> ToolManager(int min, int max) {
		// CXAsyncTask task = new CXAsyncTask();
		ArrayList<Map<String, Object>> jsonList1 = null ;
		JSONObject js = new JSONObject();
		String result = null;
		String id = msharepreference.getStringProperty("user_id");
		// String id = user_id;
		try {
			js.put("id", id);
			js.put("min", min);
			js.put("max", max);
			result = postGround("get_personal_secondhand_info", js.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// Ҫ��ȡ
		jsonList.clear();
		jsonList = JsonJx(result);
      
		
	    jsonList1 = new ArrayList<Map<String, Object>>();
		jsonList1.addAll(jsonList);
       
		// jsonList1 = setSundriesList(jsonList);
		return jsonList1;
       
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

//			System.out.println("urlString---"+urlString);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return urlString;
	}

	public ArrayList<Map<String, Object>> JsonJx(String str) {
		ArrayList<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		try {
			String SqlString = new JSONObject(str).getJSONArray("obj").toString();
			System.out.println("SqlString---"+SqlString.length());
		
			if(SqlString.length()==2){

            myHandler.sendEmptyMessage(0x123);
			}else{
			
			jsonObjs = new JSONObject(str).getJSONArray("obj");
			for (int i = 0; i < jsonObjs.length(); i++) {
				JSONObject jsonObject = (JSONObject) jsonObjs.opt(i);
				HashMap<String, Object> jsonmap = new HashMap<String, Object>();
				// HashMap<String, String> map = new HashMap<String, String>();
				// jsonmap.put("name", jsonObject.getString("name"));
				jsonmap.put("object_name", jsonObject.optString("object_name"));
				jsonmap.put("object_description",
						jsonObject.optString("object_description"));
				jsonmap.put("sundriesImageUrl",
						Contstant.URL2 + jsonObject.getString("img_url"));
				jsonmap.put("img_url", jsonObject.get("img_url"));
				jsonmap.put("type", jsonObject.optString("type"));
				jsonmap.put("tel_type", jsonObject.optString("tel_type"));
				jsonmap.put("sub_time", jsonObject.getString("sub_time"));
				jsonmap.put("tel", jsonObject.optString("tel"));
              
				list1.add(jsonmap);

			}
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
	
  
	Handler myHandler =new Handler(){

		public void handleMessage(Message msg) {
			Content_is_empty();
		}
		
	};
	
	public void Content_is_empty(){	
		TextView emptyView = new TextView(Sundries_deal_history.this);
		emptyView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setText("你还没有交易哦~");
		emptyView.setTextColor(getResources().getColor(R.color.orange));
		emptyView.setVisibility(View.GONE);
		((ViewGroup) mListView.getParent()).addView(emptyView);
		mListView.setEmptyView(emptyView);
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

	public class ImageListAdapter extends BaseAdapter {

		private Context mContext;
		// xmlתView����
		private LayoutInflater mInflater;
		// ���еĲ���
		private int mResource;
		// �б�չ�ֵ����
		private List mData;
		// Map�е�key
		private String[] mFrom;
		// view��id
		private int[] mTo;
		// ͼƬ������
		private AbImageDownloader mAbImageDownloader = null;

	
		public ImageListAdapter(Context context, List data, int resource,
				String[] from, int[] to) {
			this.mContext = context;
			this.mData = data;
			this.mResource = resource;
			this.mFrom = from;
			this.mTo = to;
			// ���ڽ�xmlתΪView
			this.mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// ͼƬ������
			mAbImageDownloader = new AbImageDownloader(mContext);
			mAbImageDownloader.setWidth(100);
			mAbImageDownloader.setHeight(100);
			mAbImageDownloader.setType(AbImageUtil.SCALEIMG);
			mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
			mAbImageDownloader.setErrorImage(R.drawable.image_error);
			mAbImageDownloader.setNoImage(R.drawable.image_no);
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
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				// ʹ���Զ����list_items��ΪLayout
				convertView = mInflater.inflate(mResource, parent, false);
			}

			ImageView sundriesImage = AbViewHolder.get(convertView, mTo[0]);
			TextView sundriesName = AbViewHolder.get(convertView, mTo[1]);
			TextView sundriesDetail = AbViewHolder.get(convertView, mTo[2]);
			TextView type = AbViewHolder.get(convertView, mTo[3]);
			TextView sub_time = AbViewHolder.get(convertView, mTo[4]);

			// ��ȡ���е����
			final Map<String, Object> obj = (Map<String, Object>) mData
					.get(position);
			String imageUrl = (String) obj.get("sundriesImageUrl");
			sundriesName.setText((String) obj.get("object_name"));
			sundriesDetail.setText((String) obj.get("object_description"));
			type.setText((String)obj.get("type"));
			sub_time.setText((String)obj.get("sub_time"));
			// ���ü����е�View
			mAbImageDownloader.setLoadingView(convertView
					.findViewById(R.id.sundriesImageProgressBar));
			// ͼƬ������
			mAbImageDownloader.display(sundriesImage, imageUrl);

			return convertView;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 1||resultCode == 2) {
			//当 提交信息或删除信息后  返回  Sundries_detail_history 界面 需要刷新数据重新加载  所以 需要 refresh（）一次；
			refreshTask();			
		}
		 
	}
	
	
	private void myBottomSet(){
		AbBottomBar mAbBottomBar = this.getBottomBar();
		View view = mInflater.inflate(R.layout.lw_add_button_xia, null);
		LinearLayout clickButton = (LinearLayout) view.findViewById(R.id.add_LinearLayout);
		clickButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Sundries_deal_history.this,lw_submit_myGoods.class);
				Sundries_deal_history.this.startActivityForResult(intent, 2);
	
			}
		});
		mAbBottomBar.setBottomView(view);
	}
	private void titleBar() {		
		AbTitleBar mAbTitleBar = this.getTitleBar();
		View view = mInflater.inflate(R.layout.titlebar_lw, null);
		ImageButton buttonBack = (ImageButton) view.findViewById(R.id.buttonBack);
		buttonBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				System.gc();
			}
		});
		TextView mytext = (TextView) view.findViewById(R.id.textTitleContent);
		mytext.setText("我的交易");
		mAbTitleBar.addView(view);
	}

}
