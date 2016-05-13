package com.wudi.comment.activity;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ab.bitmap.AbImageDownloader;
import com.ab.util.AbImageUtil;
import com.ab.util.AbViewHolder;

public class wudi_ImageListAdapter extends BaseAdapter {

	private static String TAG = "ImageListAdapter";

	private Context mContext;

	private LayoutInflater mInflater;

	private String[] mFrom;

	private int[] mTo;

	private int mResource;

	private List mData;

	public wudi_ImageListAdapter(Context context, List data, int resource,
			String[] from, int[] to) {
		this.mContext = context;
		this.mData = data;
		this.mResource = resource;
		this.mFrom = from;
		this.mTo = to;
		// xml转化成 listView
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

			convertView = mInflater.inflate(mResource, parent, false);
		}

		TextView username = AbViewHolder.get(convertView, mTo[0]);
		RatingBar userRatingbar = AbViewHolder.get(convertView, mTo[1]);
		TextView userComment = AbViewHolder.get(convertView, mTo[2]);
		// 给每个item中的3个控件初始化 每次回调 getView时设置一个listview的item
		final Map<String, Object> obj = (Map<String, Object>) mData
				.get(position);
		username.setText(obj.get("username").toString());
		userRatingbar.setRating((Float) obj.get("ratingbarSocore"));
		userComment.setText(obj.get("userComment").toString());
		return convertView;
	}

}
