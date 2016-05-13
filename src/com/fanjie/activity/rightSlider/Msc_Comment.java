package com.fanjie.activity.rightSlider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jhunplay.com.fanjie.tool.Msharepreference;
import jhunplay.com.fanjie.tool.Tool;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ab.view.titlebar.AbTitleBar;
import com.block.jhguide.R;

public class Msc_Comment extends AbActivity implements OnFooterLoadListener,
		OnHeaderRefreshListener {

	private ListView comListView;
	private RatingBar stars;
	private List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	private AbPullToRefreshView mPullToRefreshView;
	private AbTitleBar mAbTitleBar;
	private MyAdapter mAdapter;
	private int currentPage;
	private final int pageSize = 10;
	private Tool mTool;
	private Msharepreference msharepreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.msc_comment_activity_main);		
		TitleSet();
		initview();
		initdate();
		showProgressDialog();
		refreshTask();
	}

	private void TitleSet() {
		mAbTitleBar = this.getTitleBar();
		View view = mInflater.inflate(R.layout.msc_title_comment, null);
		ImageButton returnBtn = (ImageButton) view.findViewById(R.id.returnbutton);
		returnBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mAbTitleBar.addView(view);
	}

	private void initview() {
		mTool = new Tool(this);
		msharepreference = new Msharepreference(this, "information");
		mPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		comListView = (ListView) findViewById(R.id.commentlist);
		//stars = (RatingBar) findViewById(R.id.stars);
	}

	private void initdate() {
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterLoadListener(this);
		mPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(
						R.drawable.msc_mycomment_progress_circular));
		mPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(
						R.drawable.msc_mycomment_progress_circular));

		mAdapter = new MyAdapter(this, list, R.layout.msc_mycomment,
				new String[] { "user_name", "content", "name" ,"level"}, new int[] { R.id.id,
						R.id.cm, R.id.pl ,R.id.stars});
//        mSimpleAdapter.setViewBinder(new ViewBinder() {
//            @Override
//            public boolean setViewValue(View view, Object data,
//                    String textRepresentation) {
//                if ((view instanceof RatingBar) && (data instanceof Float)) {
//                    stars = (RatingBar) view;                   
//                    stars.setRating((Float) data);
//                    return true;
//                }
//                return false;
//            }
//        });
		comListView.setAdapter(mAdapter);
	}

	private void refreshTask() {
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public void update(List<?> arg0) {
				// TODO Auto-generated method stub
				removeProgressDialog();
				List<Map<String, Object>> newList 
				= (List<Map<String, Object>>) arg0;
				list.clear();
				if (newList != null && newList.size() > 0) {
					list.addAll(newList);
					mAdapter.notifyDataSetChanged();
					newList.clear();
				}
				mPullToRefreshView.onHeaderRefreshFinish();
			}

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				List<Map<String, Object>> newList = null;
				try {
					currentPage = 1;
					Thread.sleep(1000);
					newList = new ArrayList<Map<String, Object>>();
					newList = toolManager((currentPage - 1)*pageSize, pageSize);
				} catch (Exception e) {
				}
				return newList;
			}
		});
		mAbTask.execute(item);
	}

	private void loadMoreTask() {
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public void update(List<?> arg0) {
				// TODO Auto-generated method stub
				List<Map<String, Object>> newList 
				= (List<Map<String, Object>>) arg0;
				if (newList != null && newList.size() > 0) {
					list.addAll(newList);
					mAdapter.notifyDataSetChanged();
					newList.clear();
				}else {
					Toast.makeText(Msc_Comment.this, "No More Commentaries!", 0).show();
				}
				mPullToRefreshView.onFooterLoadFinish();
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
						newList = toolManager((currentPage - 1)*pageSize , pageSize );
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
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		refreshTask();
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		loadMoreTask();
	}
	
	private ArrayList<Map<String, Object>> toolManager(int min,int max){
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String[] key = new String[]{"user_name","content","level","store_id","name"};
		String result = null;  
		String success = null;
        JSONObject params = new JSONObject();
//        String sessionkey =msharepreference.getStringProperty("sessionkey");
        try {
	        params.put("ids", "my_comment_store");
	        params.put("user_id", msharepreference.getStringProperty("user_id"));
	        params.put("min", min);
	        params.put("max", max);
//	        params.put("sessionkey",sessionkey);
	        result = Tool.registPost(params);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        list = Tool.Json(result, key);
        //list = mTool.Json(result, key);
        return list;
	}
	
	private class MyAdapter extends SimpleAdapter {

		private Context mContext;
	    //单行的布局
	    private int mResource;
	    //列表展现的数据
	    private List<? extends Map<String, ?>> mData;
	    //Map中的key
	    private String[] mFrom;
	    //view的id
	    private int[] mTo;
		
		public MyAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			// TODO Auto-generated constructor stub
	    	 mContext = context;
	    	 mData = data;
	         mResource = resource;
	         mFrom = from;
	         mTo = to;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mData.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext)
						.inflate(mResource,parent,false);
				
				holder = new ViewHolder();
				holder.id= (TextView) convertView
						.findViewById(mTo[0]);
				holder.cm= (TextView) convertView
						.findViewById(mTo[1]);
				holder.pl = (TextView) convertView
						.findViewById(mTo[2]);
				holder.stars= (RatingBar) convertView
						.findViewById(mTo[3]);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
				
				holder.id.setText((String)list.get(position).get(mFrom[0]));
				holder.cm.setText((String)list.get(position).get(mFrom[1]));
				holder.pl.setText((String)list.get(position).get(mFrom[2]));
				holder.stars.setRating(Float.parseFloat((String) list.get(position).get(mFrom[3])));
				
					
			return convertView;

		}
	}

		public final class ViewHolder {

			public TextView id;
			public TextView cm;
			public TextView pl;
			public RatingBar stars;

		}


}
