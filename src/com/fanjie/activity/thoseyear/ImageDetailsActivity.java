package com.fanjie.activity.thoseyear;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import jhunplay.com.block.tool.ActivitySplitAnimationUtil;
import jhunplay.com.block.tool.ImageLoader;
import jhunplay.com.fanjie.adapter.ImageShowAdapter;
import jhunplay.com.fanjie.ovrideLayout.ZoomImageView;
import jhunplay.com.fanjie.tool.Msharepreference;
import jhunplay.com.fanjie.tool.Tool;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ab.bitmap.AbImageDownloader;
import com.ab.util.AbImageUtil;
import com.block.jhguide.R;
import com.fanjie.activity.rightSlider.Login;

public class ImageDetailsActivity extends Activity {

	private ZoomImageView zoomImageView;
	private LayoutInflater inflater;
	private View view;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private GridView mGridView = null;
	private ImageShowAdapter mImagePathAdapter = null;
	private File PHOTO_DIR = null;
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 用来标识请求裁剪图片后的activity */
	private static final int CAMERA_CROP_DATA = 3022;
	private File mCurrentPhotoFile;
	private String mFileName;
	private int selectIndex = 0;
	// private int camIndex = 0;
	private int disIdex = 0;
	private int godIndex = 1;
	private int scIndex = 2;
	private boolean flag = false;
	private PopupWindow mPopupWindow;
	private Msharepreference msharepreference;
	private String pic_id;
	private String store_id;
	private String pic_praise;
	private String pic_collection;
	private String pic_position;
	private Tool tool;
	private AbImageDownloader abImageDownloader;
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_details);
		Display mDisplay = getWindowManager().getDefaultDisplay();
		int W = mDisplay.getWidth();
		int H = mDisplay.getHeight();

		mGridView = (GridView) findViewById(R.id.gridView1);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		abImageDownloader = new AbImageDownloader(this);
		zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);
		String imagePath = getIntent().getStringExtra("image_path");
		File file = new File(imagePath);
		tool = new Tool(this);
		bitmap = AbImageUtil.scaleImg(file, W, H);/*
												 * ImageLoader.
												 * decodeSampledBitmapFromResource
												 * (imagePath, W)
												 */
		;
		zoomImageView.setImageBitmap(bitmap);

		msharepreference = new Msharepreference(this, "information");

		Intent getIntent = getIntent();
		pic_id = getIntent.getStringExtra("pic_id");
		pic_praise = getIntent.getStringExtra("pic_praise");

		pic_collection = getIntent.getStringExtra("pic_collection");

		store_id = pic_id;

		if (pic_praise == null || pic_praise.equals("null")
				|| pic_praise.equals("")) {

			mPhotoList.add(String.valueOf(R.drawable.thoseyear_zan));

		} else {

			mPhotoList.add(String.valueOf(R.drawable.thoseyear_zan_after));

		}

		if (pic_collection == null || pic_collection.equals("null")
				|| pic_collection.equals("")) {

			mPhotoList.add(String.valueOf(R.drawable.thoseyear_shoucang));

		} else {

			mPhotoList.add(String.valueOf(R.drawable.thoseyear_shoucang_after));

		}

		mPhotoList.add(String.valueOf(R.drawable.thoseyear_pinglun));
		mImagePathAdapter = new ImageShowAdapter(this, mPhotoList,
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mGridView.setAdapter(mImagePathAdapter);

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view1,
					int position, long id) {
				selectIndex = position;
				if (selectIndex == disIdex) { // 如果点击的是“点赞”按钮

					if (pic_praise == null || pic_praise.equals("")
							|| pic_praise.equals("null")) { // 如果该图未被赞
						if (msharepreference.getStringProperty("sessionkey")
								.equals("")) {
							tool.showDialog(Login.class, "未登入", "请先登入");
							return;
						}
						String a = msharepreference
								.getStringProperty("user_id");
						JSONObject jsonObject = Tool.getJSONObject(2,
								"store_id", store_id, "type", "1", "user_id",
								msharepreference.getStringProperty("user_id"),
								"ids", "praise_store");
						Praise praise = new Praise();
						praise.execute(jsonObject);
					} else { // 如果该图已被赞

						JSONObject jsonObject = Tool.getJSONObject(2, "type",
								"1", "store_id", store_id, "user_id",
								msharepreference.getStringProperty("user_id"),
								"ids", "delete_parise");
						Parise_Cancel praise_cancel = new Parise_Cancel();
						praise_cancel.execute(jsonObject);
					}

				} else if (selectIndex == godIndex) { // 如果点击的是“收藏”按钮

					// 如果点击的是“收藏”按钮

					if (msharepreference.getStringProperty("sessionkey")
							.equals("")) {
						tool.showDialog(Login.class, "未登入", "请先登入");
						return;
					}
					if (pic_collection == null || pic_collection.equals("null")
							|| pic_collection.equals("")) { // 如果该图未被收藏

						JSONObject jsonObject = Tool.getJSONObject(2,
								"store_id", store_id, "type", "1", "user_id",
								msharepreference.getStringProperty("user_id"),
								"ids", "collection_store");
						Collection collection = new Collection();
						collection.execute(jsonObject);
					} else { // 如果该图已被收藏

						JSONObject jsonObject = Tool.getJSONObject(2,
								"store_id", store_id, "type", "1", "user_id",
								msharepreference.getStringProperty("user_id"),
								"ids", "delete_collection");
						Collection_Cancel collection_cancel = new Collection_Cancel();
						collection_cancel.execute(jsonObject);
					}

				} else if (selectIndex == scIndex) { // 如果点击的是“评论”按钮

					Intent it = new Intent(ImageDetailsActivity.this,
							commentary.class);
					it.putExtra("pic_id", pic_id);
					ActivitySplitAnimationUtil.startActivity(
							ImageDetailsActivity.this, it);

				} else {
					for (int i = 0; i < mImagePathAdapter.getCount(); i++) {
						ImageShowAdapter.ViewHolder mViewHolder = (ImageShowAdapter.ViewHolder) mGridView
								.getChildAt(i).getTag();
						if (mViewHolder != null) {
							mViewHolder.mImageView2.setBackgroundDrawable(null);
						}
					}

					ImageShowAdapter.ViewHolder mViewHolder = (ImageShowAdapter.ViewHolder) view1
							.getTag();
					mViewHolder.mImageView2
							.setBackgroundResource(R.drawable.photo_select);
				}
			}

		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			// if (!(bitmap.isRecycled())) {
			// bitmap.recycle();
			// bitmap = null;
			// }
			finish();
//			System.gc();
			overridePendingTransition(R.anim.appear_top_left_in,
					R.anim.disappear_bottom_right_out);

		}
		return true;
	}

	public class Praise extends AsyncTask<JSONObject, Integer, String> {

		@Override
		protected String doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub
			String result = Tool.registPost(params[0]);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (result.contains("执行成功")) {
				mPhotoList.remove(0);
				mPhotoList.add(0,
						String.valueOf(R.drawable.thoseyear_zan_after));
				mImagePathAdapter.notifyDataSetChanged();
				pic_praise = "已赞";
				Toast.makeText(ImageDetailsActivity.this, "点赞+1",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ImageDetailsActivity.this, "点赞失败", 0).show();
			}
			super.onPostExecute(result);
		}
	}

	public class Parise_Cancel extends AsyncTask<JSONObject, Integer, String> {

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

			if (result.contains("执行成功")) {
				mPhotoList.remove(0);
				mPhotoList.add(0, String.valueOf(R.drawable.thoseyear_zan));
				mImagePathAdapter.notifyDataSetChanged();

				pic_praise = null;

				Toast.makeText(ImageDetailsActivity.this, "取消点赞",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ImageDetailsActivity.this, "取消点赞失败",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	public class Collection extends AsyncTask<JSONObject, Integer, String> {

		@Override
		protected String doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub
			String result = Tool.registPost(params[0]);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			if (result.contains("执行成功")) {
				mPhotoList.remove(1);
				mPhotoList.add(1,
						String.valueOf(R.drawable.thoseyear_shoucang_after));
				mImagePathAdapter.notifyDataSetChanged();
				pic_collection = "已收藏";
				Toast.makeText(ImageDetailsActivity.this, "已收藏",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ImageDetailsActivity.this, "收藏失败",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	public class Collection_Cancel extends
			AsyncTask<JSONObject, Integer, String> {

		@Override
		protected String doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub
			String result = Tool.registPost(params[0]);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			if (result.contains("执行成功")) {
				mPhotoList.remove(1);
				mPhotoList
						.add(1, String.valueOf(R.drawable.thoseyear_shoucang));
				mImagePathAdapter.notifyDataSetChanged();
				pic_collection = null;
				Toast.makeText(ImageDetailsActivity.this, "取消收藏",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ImageDetailsActivity.this, "取消收藏失败",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

	private String getImagePath(String imageUrl) {
		int lastSlashIndex = imageUrl.lastIndexOf("/");
		String imageName = imageUrl.substring(lastSlashIndex + 1);
		String imageDir = Environment.getExternalStorageDirectory().getPath()
				+ "/PhotoWallFalls/";
		File file = new File(imageDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		String imagePath = imageDir + imageName;
		return imagePath;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (tool.haveShowDialog()) {
			tool.removeDialog();
		}
		finish();
	}
}
