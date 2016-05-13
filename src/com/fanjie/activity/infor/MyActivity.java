package com.fanjie.activity.infor;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.block.jhguide.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MyActivity extends Activity {
	private String URL = "http://61.142.71.63:9090/jhun_play/";
	private String[] picture = { };
	private String[] text = {
			 };
	private TextView textView;
	private ImageView imageView;
	private View view;
	private LayoutInflater inflater;
	private LinearLayout layout;
    private ArrayList list=new ArrayList();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picturetext);
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		list=bundle.getParcelableArrayList("Info");
		if(!list.get(0).toString().equals("null")&list.get(0).toString().contains(",")){
			picture=list.get(0).toString().split(",");
		}else if(!list.get(0).toString().equals("null")&!list.get(0).toString().contains(",")){
			picture=new String[]{list.get(0).toString()};
		}	
		if(!list.get(2).toString().equals("null")&list.get(2).toString().contains("|")){
			text=list.get(2).toString().split("\\|");
		}else if(!list.get(2).toString().equals("null")&!list.get(2).toString().contains("\\|")){
			text=new String[]{list.get(2).toString()};
		}
		
		inflater = getLayoutInflater();
		layout = (LinearLayout) findViewById(R.id.mylayout);
		textView = new TextView(this);
		textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		textView.setTextSize(30);
		textView.setBackgroundColor(Color.WHITE);
		layout.addView(textView);
		ScrollView myScroeView = (ScrollView) this
				.findViewById(R.id.scrollView1);
		textView.setText(list.get(1).toString());
		if(picture.length!=0){
			loadImageText();
			}
		else{
			for(String str :text){
				addText(str);
				
			}
		}
	}

	private void loadImageText() {
		for (int i = 0; i < picture.length; i++) {
			MyDown down = new MyDown();

			down.execute(URL + getString(picture, i), getString(text, i));
		}
	}
	
	public String getString(String[] a, int i){
		return (i<a.length)? a[i]:"";
	}

	public void addText(String text) {
		view = inflater.inflate(R.layout.image, null);
		textView = (TextView) view.findViewById(R.id.textView1);
		textView.setTextColor(Color.BLACK);
		textView.setText(text);
		layout.addView(view);
	}
	public class MyDown extends AsyncTask<String, Integer, Bitmap> {

		String text;

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			Bitmap bitmap = loadImage(params[0]);
			text = params[1];
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			// TODO Auto-generated method stub
			super.onPostExecute(bitmap);
			if (bitmap != null) {
				addImage(bitmap, text);
			}
		}

		private Bitmap loadImage(String imageUrl) {
			Bitmap bitmap = null;
			if (imageUrl != null) {
				bitmap = downloadImage(imageUrl);

			}
			return bitmap;
		}

		private void addImage(Bitmap bitmap, String text) {
			view = inflater.inflate(R.layout.image, null);
			imageView = (ImageView) view.findViewById(R.id.imageView1);
			textView = (TextView) view.findViewById(R.id.textView1);
			imageView.setImageBitmap(bitmap);
			textView.setTextColor(Color.BLACK);
			textView.setText(text);
			layout.addView(view);
		}
 
		private Bitmap downloadImage(String imageUrl) {
			Bitmap bitmap = null;
			try {

				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(imageUrl);
				HttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				if (response.getStatusLine().getStatusCode() == 200) {
					bitmap = BitmapFactory.decodeStream(entity.getContent());

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("----------", e.toString());
				return null;
			}
			return bitmap;
		}
	}
}
