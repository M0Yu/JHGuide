package com.huangbolun.sershou.waterex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.Toast;

import com.block.jhguide.R;
import com.huangbolun.ershou.android.bitmapfun.util.Helper;
import com.huangbolun.ershou.android.bitmapfun.util.ImageFetcher;
import com.huangbolun.ershou.library.views.StaggeredGridView;
import com.huangbolun.ershou.model.DuitangInfo;

/** This will not work so great since the heights of the imageViews are
 * calculated on the iamgeLoader callback ruining the offsets. To fix this try
 * to get the (intrinsic) image width and height and set the views height
 * manually. I will look into a fix once I find extra time.
 * 
 * @author Maurycy Wojtowicz */
public class ExchangeFragment extends Fragment {

	private ImageFetcher mImageFetcher;
	private StaggeredAdapter mAdapter;
	private ContentTask task = new ContentTask(getActivity(), 2);
	String str;
	View view ;

	private JSONArray jsonObjs;

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.huangbolun_ershoujiaoyi, null);
		mImageFetcher = new ImageFetcher(getActivity(), 240);
		mImageFetcher.setLoadingImage(R.drawable.huangbolun_empty_photo);
		StaggeredGridView gridView = (StaggeredGridView) view.findViewById(R.id.staggeredGridView2);
  
        
       
		int margin = getResources().getDimensionPixelSize(R.dimen.margin);

		gridView.setFastScrollEnabled(true);

		mAdapter = new StaggeredAdapter(getActivity(), mImageFetcher);
		gridView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		AddItemToContainer(1, 1);
//		AddItemToContainer(2, 1);
//		AddItemToContainer(3, 1);
return view;
	}
	
	

	private void AddItemToContainer(int pageindex, int type) {
		if (task.getStatus() != Status.RUNNING) {
			String url = "http://www.duitang.com/album/1733789/masn/p/" + pageindex + "/24/";
			Log.d("MainActivity", "current url:" + url);
			ContentTask task = new ContentTask(getActivity(), type);
			task.execute(url);

		}
	}

	
	public boolean onCreateOptionsMenu(Menu menu) {
		getActivity().getMenuInflater().inflate(R.layout.activity_main, menu);
		return true;
	}

	private class ContentTask extends AsyncTask<String, Integer, List<DuitangInfo>> {

		private Context mContext;
		private int mType = 1;

		public ContentTask(Context context, int type) {
			super();
			mContext = context;
			mType = type;
		}

		@Override
		protected List<DuitangInfo> doInBackground(String... params) {
			try {
				return parseNewsJSON(params[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<DuitangInfo> result) {
			if (mType == 1) {

				mAdapter.addItemTop(result);
				mAdapter.notifyDataSetChanged();

			} else if (mType == 2) {
				mAdapter.addItemLast(result);
				mAdapter.notifyDataSetChanged();

			}

		}
//============================
		 
		
		
		@Override
		protected void onPreExecute() {
		}

		public List<DuitangInfo> parseNewsJSON(String url) throws IOException {
			List<DuitangInfo> duitangs = new ArrayList<DuitangInfo>();

			try {
				str = Js.network();
				
				jsonObjs = new JSONObject(str).getJSONArray("obj");

					for (int i = 0; i < jsonObjs.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonObjs.opt(i);
						DuitangInfo newsInfo1 = new DuitangInfo();
						//newsInfo1.setAlbid(newsInfoLeftObject.isNull("albid") ? "" : newsInfoLeftObject.getString("albid"));
						newsInfo1.setIsrc( "http://61.142.71.63:9090/jhun_play/"+jsonObject.getString("img_url"));
						newsInfo1.setObject_name( jsonObject.getString("object_name"));
						newsInfo1.setObject_description( jsonObject.getString("object_description"));
						newsInfo1.setType(jsonObject.getString("type"));
						newsInfo1.setName(jsonObject.getString("name"));
						newsInfo1.setTel(jsonObject.getString("tel"));
						newsInfo1.setTel_type(jsonObject.getString("tel_type"));
						newsInfo1.setSub_time(jsonObject.getString("sub_time"));
					 	newsInfo1.setHeight(300+new Random().nextInt(300)+1);
						duitangs.add(newsInfo1);
					}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return duitangs;
		}
	}

}
