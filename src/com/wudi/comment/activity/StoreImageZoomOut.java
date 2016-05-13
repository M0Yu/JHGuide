package com.wudi.comment.activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import jhunplay.com.fanjie.ovrideLayout.ZoomImageView;
import jhunplay.com.fanjie.tool.Msharepreference;

import com.ab.bitmap.AbImageDownloader;
import com.block.jhguide.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

public class StoreImageZoomOut extends Activity {
	private ZoomImageView zoomImageView;
	private String imagePath;
	private AbImageDownloader abImageDownloader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.xml.storeimagedetail);
		zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);
		imagePath = getIntent().getStringExtra("image_path");
//		Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
		abImageDownloader = new AbImageDownloader(this);
//		zoomImageView.setImageBitmap(bitmap);
		abImageDownloader.display(zoomImageView, imagePath);
		
//		myThread.start();

	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.appear_top_left_in,
					R.anim.disappear_bottom_right_out);

		}
		return true;
	}
	
//	Thread myThread = new Thread(new Runnable() {
//		
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			 URL myFileUrl = null;   
//			   Bitmap bitmap = null;   
//			   try {
//			    myFileUrl = new URL(imagePath);    
//			    HttpURLConnection conn;
//			    conn = (HttpURLConnection) myFileUrl.openConnection();
//			    conn.setDoInput(true);   
//			    conn.connect(); 
//			    InputStream is = conn.getInputStream();   
//			    bitmap = BitmapFactory.decodeStream(is);  
//			    
//			   } catch (MalformedURLException e) {
//			    // TODO Auto-generated catch block
//			    e.printStackTrace();
//			   }  catch (IOException e) {
//			    // TODO Auto-generated catch block
//			    e.printStackTrace();
//			   }
//			   zoomImageView.setImageBitmap(bitmap);
//
//
//		}
//	});



}
