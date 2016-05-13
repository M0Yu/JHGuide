package com.fanjie.activity.rightSlider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.ab.activity.AbActivity;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ab.view.sliding.AbSlidingPlayView;
import com.ab.view.titlebar.AbTitleBar;
import com.block.jhguide.R;

import jhunplay.com.fanjie.Constant.Constant;
import jhunplay.com.fanjie.adapter.Msc_ImageListAdapter;
import jhunplay.com.fanjie.adapter.Msc_ImageListAdapter.getStringHttp;
import jhunplay.com.fanjie.ovrideLayout.SwipeDismissListView;
import jhunplay.com.fanjie.ovrideLayout.SwipeDismissListView.OnDismissCallback;
import jhunplay.com.fanjie.tool.Msharepreference;
import jhunplay.com.fanjie.tool.Tool;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Msc_MyStore_MainActivity extends AbActivity implements
		OnFooterLoadListener {

	private List<Map<String, Object>> list = null;
	private List<Map<String, Object>> list2 = null;
	private List<Map<String, Object>> newList = null;
	private List<Map<String, Object>> SumList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> newSumList = new ArrayList<Map<String, Object>>();
	private SwipeDismissListView mListView = null;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private int currentPage = 1;
	private AbTitleBar mAbTitleBar;
	private Msc_ImageListAdapter myListViewAdapter = null;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private int total = 50;
	private int pageSize = 6;
	private int dismissPosition=0;
	private AbSlidingPlayView mSlidingPlayView = null;
	private Msharepreference msharepreference;
	private String sessionkey;
	private String user_id;
	private int currentPageStart = 0;
	private final int count = 2;
	private final int storeType = 0;
	private int position;

	// private RatingBar ratingBar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setAbContentView(R.layout.mpullrefreshview);

		mAbTitleBar = this.getTitleBar();
		//mAbTitleBar.setLogo(R.drawable.return_btn);
		mAbTitleBar.setTitleBarBackground(R.drawable.navegation_bar);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);

		View rightView = mInflater.inflate(R.layout.msc_title_collection, null);
		ImageButton returnImagbutton  = (ImageButton) rightView.findViewById(R.id.returnbtn);
		returnImagbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Msc_MyStore_MainActivity.this.finish();
			}
		});
		mAbTitleBar.addRightView(rightView);
		msharepreference = new Msharepreference(this, "information");
		sessionkey = msharepreference.getStringProperty("sessionkey");
		user_id = msharepreference.getStringProperty("user_id");
		/*
		 * for (int i = 1; i < 7; i++) {
		 * mPhotoList.add("assets/picture/picture_" + i + ".png"); }
		 */

		mAbPullToRefreshView = (AbPullToRefreshView) this
				.findViewById(R.id.mPullRefreshView);

		mListView = (SwipeDismissListView) this
				.findViewById(R.id.swipeDismissListView);

		// java.lang.ClassCastException: android.widget.ListView cannot be cast
		// to com.ab.view.pullview.AbPullToRefreshView

		mAbPullToRefreshView.setOnFooterLoadListener(this);
		mAbPullToRefreshView.setPullRefreshEnable(false);
//		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
//				this.getResources().getDrawable(R.drawable.progress_circular2));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular2));
//		mAbPullToRefreshView.setLoadMoreEnable(false);
//		mAbPullToRefreshView.set

		list = new ArrayList<Map<String, Object>>();

		myListViewAdapter = new Msc_ImageListAdapter(this, list,
				R.layout.msc_mycollection, new String[] { "image", "name",
						"info", "shoucang", 
						"id" }, new int[] { R.id.image, R.id.name, R.id.info,
						R.id.ratingBar1,  R.id.sc });
		mListView.setAdapter(myListViewAdapter);

		/*
		 * for (int i = 0; i < 50; i++) { Map<String, Object> map = new
		 * HashMap<String, Object>(); list.add(map);
		 * 
		 * }
		 */

		/*
		 * for (int i = 0, j = 1; i < list.size(); i++, j++) { if (j > 5) { j =
		 * 1; } // list.get(i).put("storeName", "storeName_"+(i+1)); //
		 * list.get(i).put("storeIntroduce", "storeIntroduce_"+(i+1));
		 * list.get(i).put("ratingbarNum", j);
		 * 
		 * }
		 */
		/*
		 * jsonObject.put("ids","collection_store");
		 * jsonObject.put("user_id",user_id);
		 * jsonObject.put("store_id",obj.get("id").toString());
		 * jsonObject.put("type", storeType); // jsonObject.put("sessionkey",
		 * sessionkey); getStringHttp gsh=new getStringHttp();
		 * gsh.execute(jsonObject);
		 */
		mListView.setOnDismissCallback(new OnDismissCallback() {

			@Override
			public void onDismiss(int dismissPosition) {
				// TODO Auto-generated method stub
				Msc_MyStore_MainActivity.this.dismissPosition = dismissPosition;
				JSONObject jsonObject = Tool.getJSONObject(2, "store_id", (String)list
						.get(dismissPosition).get("id"), "type", "0", "user_id",
						msharepreference.getStringProperty("user_id"), "ids",
						"delete_collection");
				Remove remove = new Remove();
				remove.execute(jsonObject);
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}
		});

		// showProgressDialog();
		loadMoreTask();
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView view) {
		loadMoreTask();

	}



//	public void refreshTask() {
//		AbTask mAbTask = new AbTask();
//		final AbTaskItem item = new AbTaskItem();
//		item.setListener(new AbTaskListListener() {
//			@Override
//			public List<?> getList() {
//				// TODO Auto-generated method stub
//				List<Map<String, Object>> newList = null;
//				try {
//					Thread.sleep(1000);
//					currentPage = 1;
//					newList = new ArrayList<Map<String, Object>>();
//					Map<String, Object> map = null;
//					/*
//					 * for (int i = 0; i < pageSize; i++) { map = new
//					 * HashMap<String, Object>(); map.put("image",
//					 * mPhotoList.get(i)); map.put("name", "storeName_" + (i +
//					 * 1)); map.put("info", "storeIntroduce_" + (i + 1));
//					 * map.put("ratingBar",5); map.put("dianzan", null);
//					 * map.put("shoucang", 1); newList.add(map); }
//					 */
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//
//				return newList;
//			}
//
//			@Override
//			public void update(List<?> paramList) {
//				// TODO Auto-generated method stub
//				removeProgressDialog();
//				List<Map<String, Object>> newList = (List<Map<String, Object>>) paramList;
//
//				// list.clear();
//				if (newList != null && newList.size() > 0) {
//					list.addAll(newList);
//					myListViewAdapter.notifyDataSetChanged();
//					newList.clear();
//				}
//				mAbPullToRefreshView.onHeaderRefreshFinish();
//
//			}
//
//		});
//		mAbTask.execute(item);
//	}

	public void loadMoreTask() {
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public void update(List<?> paramList) {
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
				// List<Map<String, Object>> newList = null;
				try {
					currentPage++;
					Thread.sleep(1000);
					currentPage++;
					newList = new ArrayList<Map<String, Object>>();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("ids", "my_collection");
					jsonObject.put("user_id", user_id);
					jsonObject.put("min", currentPageStart);
					jsonObject.put("max", count);
					jsonObject.put("type", storeType);
					currentPageStart += count;
					String result = Tool.registPost(jsonObject);
					// newList=Tool.Json(result, "name","details","pic_url");
					SumList = Tool.Json(result, "name", "details", "pic_url",
							"praise", "collection", "_level", "id");
					// SumList.addAll(newSumList);
					// System.out.println("wsh-------------------->"+SumnewList.size());
					for (int i = 0; i < SumList.size(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("image", Constant.URL3+SumList.get(i).get("pic_url"));
						map.put("name", SumList.get(i).get("name"));
						map.put("info", SumList.get(i).get("details"));
						//map.put("ratingBar", SumList.get(i).get("_level"));
						//map.put("dianzan", SumList.get(i).get("praise"));
						map.put("shoucang", SumList.get(i).get("collection"));
						map.put("id", SumList.get(i).get("id"));
						newList.add(map); // ��add����set
					}

				} catch (Exception e) {
					currentPage--;
					newList.clear();
					showToastInThread(e.getMessage());
				}

				return newList;
			};
		});

		// System.out.println("------------->"+a);
		mAbTask.execute(item);
	}

	// ((currentPage - 1) * pageSize + (i + 1))
	@Override
	protected void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

	public class Remove extends AsyncTask<JSONObject, Integer, String> {

		@Override
		protected String doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub
			String result = Tool.registPost(params[0]);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			String success = null;
			try {
				success = (String) new JSONObject(result).get("msg");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (success.equals("执行成功")) {
				list.remove(dismissPosition);
				myListViewAdapter.notifyDataSetChanged();
			}else{
				Toast.makeText(Msc_MyStore_MainActivity.this, "取消收藏失败", 0).show();
			}
		}

	}

}
