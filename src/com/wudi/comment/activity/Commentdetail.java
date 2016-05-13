package com.wudi.comment.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jhunplay.com.fanjie.Constant.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ab.view.titlebar.AbBottomBar;
import com.ab.view.titlebar.AbTitleBar;
import com.block.jhguide.R;



public class Commentdetail extends Activity implements
		OnHeaderRefreshListener, OnFooterLoadListener {

	private ListView listView;
	private AbTitleBar abTitleBar;
	private AbBottomBar abBottomBar;
	private List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private ListView mListView = null;
	private wudi_ImageListAdapter myListViewAdapter = null;
	private ListView usercCommentListView = null;
	private LinearLayout myLinearLayout;
	private int store_id;
	private int refreshOrLoadMoreFlag;
	private int loadMoreCount = 0;
	private int currentPage;
	private int pageSize = 18;
	private ImageButton pinglunButton;
	private Button backToStoreList;
	
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wudi_commentdetail);

		//获取 StoreDetailActivity 传过来的 bundle
//		Intent intent = Commentdetail.this.getIntent();
//		Bundle bundle = new Bundle();
//		bundle = intent.getBundleExtra("storeCommentBundle");
//		store_id = bundle.getInt("store_id");
		

		pinglunButton = (ImageButton) findViewById(R.id.pinglunbutton);
		backToStoreList = (Button) findViewById(R.id.backToStoreList);
		
		
		//设置顶部栏
//		abTitleBar = this.getTitleBar();
//		abTitleBar.setTitleBarBackground(R.drawable.wudi_navegation_bar);
//		abTitleBar.setLogo(R.drawable.wudi_back);
//		abTitleBar.setLogo2(R.drawable.wudi_usercomment);

		// 设置底部的button和textview
//		abBottomBar = this.getBottomBar();
//		View view = mInflater.inflate(R.layout.wudi_bottombutton, null);
//		myLinearLayout = (LinearLayout) view.findViewById(R.id.skipToMyCommnt);
		
		
		// 设置底部linearlayout的监听器 跳转到 我的评论
//		myLinearLayout.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.setClass(Commentdetail.this, MyStoreComment.class);
//				Bundle bundle = new Bundle();
//				bundle.putInt("store_id", store_id);
//				intent.putExtra("storeIdBundle", bundle);
//				Commentdetail.this.startActivityForResult(intent, 1);
//			}
//		});
//		abBottomBar.setBottomBarBackground(R.drawable.wudi_tab);
//		abBottomBar.setPadding(0, 40, 0, 40);
//		abBottomBar.addView(view);
		
		
		pinglunButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Commentdetail.this, MyStoreComment.class);
				Bundle bundle = new Bundle();
				bundle.putInt("store_id", store_id);
				intent.putExtra("storeIdBundle", bundle);
				Commentdetail.this.startActivityForResult(intent, 1);
			}
		});
		

		// 获取ListView对象
		mAbPullToRefreshView = (AbPullToRefreshView) this
				.findViewById(R.id.wudi_mPullRefreshView);
		mListView = (ListView) this.findViewById(R.id.mcommentListView);

		// 设置监听器
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);

		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.wudi_progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.wudi_progress_circular));
		
		// // 使用自定义的Adapter
		myListViewAdapter = new wudi_ImageListAdapter(this, list,
				R.layout.wudi_detailsim, new String[] { "username",
						"ratingbarSocore", "userComment" }, new int[] {
						R.id.username,R.id.userRatingbar,R.id.userComment});
		mListView.setAdapter(myListViewAdapter);

		
		// 第一次下载数据
		refreshTask();

		backToStoreList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Commentdetail.this.finish();
			}
		});

	}
	
	
	
	private List<Map<String, Object>> toolManager(int min,int max){
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String result = null;
        
        //params的参数，含图片的ID，评论数量的最大值和最小值
        JSONObject params = new JSONObject();
        try {
	        params.put("ids", "get_commonts");
	        params.put("store_id", store_id);
	        params.put("min", min);
	        params.put("max", max);
	        params.put("type",0);
	        result = Register(params);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        list = JsonAnalysis(result);
        return list;
	}
	
	
	public static String Register(JSONObject jsonObject) {
		String result = null;
		try {
			List<NameValuePair> parms = new ArrayList<NameValuePair>();
			String id = jsonObject.optString("ids");
			jsonObject.remove("ids");
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constant.URL2);
			parms.add(new BasicNameValuePair("register", "true"));
			parms.add(new BasicNameValuePair("ids", id));
			parms.add(new BasicNameValuePair("params", jsonObject.toString()));
			post.setEntity(new UrlEncodedFormEntity(parms));
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private void refreshTask(){
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {
			
			@Override
			public void update(List<?> arg0) {
				// TODO Auto-generated method stub
				List<Map<String, Object>> newList 
				= (List<Map<String, Object>>) arg0;
				list.clear();
				if(newList!=null && newList.size()>0){
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

                    Thread.sleep(1000);
                    currentPage = 1;
                    List<Map<String, Object>> userCommentList = new ArrayList<Map<String,Object>>();
					newList = new ArrayList<Map<String, Object>>();
					userCommentList = toolManager((currentPage - 1)*pageSize, pageSize);
					Map<String, Object> map = null;
					for (int i = 0; i < userCommentList.size(); i++) {
						map = new HashMap<String, Object>();
						map.put("username", userCommentList.get(i).get("user_name"));
						map.put("ratingbarSocore", userCommentList.get(i).get("level"));
						map.put("userComment", userCommentList.get(i).get("content"));
						newList.add(map);
					}
					
                } catch (Exception e) {
                }
                return newList;
			}
		});
		mAbTask.execute(item);
	}
	
	
	private void loadMoreTask(){
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {
				
			@Override
			public void update(List<?> arg0) {
				// TODO Auto-generated method stub
				 List<Map<String, Object>> newList 
                 = (List<Map<String, Object>>) arg0;
				 
				 if(newList!=null && newList.size()>0){
					 list.addAll(newList);
					 myListViewAdapter.notifyDataSetChanged();
					 newList.clear();
				 } else {
						Toast.makeText(Commentdetail.this, "No More Commentaries!", 1000).show();
				 }
				 mAbPullToRefreshView.onFooterLoadFinish();
			}
				
			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				
				List<Map<String, Object>> newList = null;
				try {
					
						newList = new ArrayList<Map<String, Object>>();	
						if(list.size() >= currentPage*pageSize){
							currentPage++;
							Thread.sleep(1000);
							
							List<Map<String, Object>> userCommentList = new ArrayList<Map<String,Object>>();
							userCommentList = toolManager((currentPage - 1)*pageSize, pageSize);
							
							Map<String, Object> map = null;
							for (int i = 0; i < userCommentList.size(); i++) {
								map = new HashMap<String, Object>();
								map.put("username", userCommentList.get(i).get("user_name"));
								map.put("ratingbarSocore", userCommentList.get(i).get("level"));
								map.put("userComment", userCommentList.get(i).get("content"));
								newList.add(map);
							}
								
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
	

	
	public List<Map<String, Object>> JsonAnalysis(String str) {

		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		try {

			JSONArray jsonObjs = new JSONObject(str).getJSONArray("obj");

			for (int i = 0; i < jsonObjs.length(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				JSONObject jsonObj = (JSONObject) jsonObjs.opt(i);
				int id = jsonObj.getInt("id");//评论数 
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
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		 //TODO Auto-generated method stub
		if (arg1 == 1) {
			//当 发送完我的评论后  返回  userComment 界面 需要刷新数据 从新加载  所以 需要 refresh（）一次；
			refreshTask();
			
		}
	}

}
