package com.fanjie.activity.rightSlider;

import java.io.File;

import jhunplay.com.fanjie.tool.Msharepreference;
import jhunplay.com.fanjie.tool.Tool;

import org.json.JSONException;
import org.json.JSONObject;

import com.block.jhguide.R;
import com.tencent.android.tpush.horse.Tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Change extends Activity implements OnClickListener {

	private Button my4button,my41button,my411button;
	private ImageView my4image;
	private TextView my4text,my41text1,my41text2,l4_textView7;
	private LinearLayout my4linear1,my4linear2;
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private File tempFile;
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	private ImageView mymaimage;
	private String[] itens = new String[]{"本地图片", "手机拍照"};
	private Msharepreference msharepreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_info);
		msharepreference = new Msharepreference(this, "information");

		l4_textView7 = (TextView) findViewById(R.id.l4_textView7);
		my4button = (Button)findViewById(R.id.l4_button1);
		my4image = (ImageView)findViewById(R.id.l4_imageView1);
		my4linear1 = (LinearLayout)findViewById(R.id.l4_linearLayout1);
		my4linear2 = (LinearLayout)findViewById(R.id.l4_linearLayout2);
		my4image.setOnClickListener(this);
		my4button.setOnClickListener(this);
		my4linear1.setOnClickListener(this);
		my4linear2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.l4_button1:
			// Intent intent = new Intent();
			// intent.setClass(Change.this, MyInformation.class);
			// startActivity(intent);
			finish();
			break;
		case R.id.l4_linearLayout1:
			Intent j = new Intent().setClass(Change.this, change_name.class);
			Change.this.startActivity(j);
			break;
		case R.id.l4_linearLayout2:
			Intent k = new Intent().setClass(Change.this, change_password.class);
			Change.this.startActivity(k);
			break;

		default:
			break;
		}
	}
//	}

//	public class ChangeUser extends AsyncTask<JSONObject, Integer, String> {
//
//		@Override
//		protected String doInBackground(JSONObject... params) {
//			// TODO Auto-generated method stub
//			String result = Tool.changePassword(params[0]);
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			try {
//				String success = (String) new JSONObject(result)
//				.get("msg");
//				if(success.equals("执行成功")){
//					Toast.makeText(Change.this, success, 0).show();
//					finish();
//				}else if (success.equals("执行失败")){
//					Toast.makeText(Change.this, success, 0).show();
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		
//
//	}
	
	class myDialog extends Dialog implements android.view.View.OnClickListener{

		public myDialog(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		
		public void setdialogs(View v){
//			LayoutInflater inflater = getLayoutInflater();  
//			View layout = inflater.inflate(R.layout.l41, null);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(v);
			my41text1 = (TextView)findViewById(R.id.l41_textView1);
			my41text2 = (TextView)findViewById(R.id.l41_textView2);
			my41button = (Button)findViewById(R.id.l41_button3);
			my41text1.setOnClickListener(this);
			my41text2.setOnClickListener(this);
			my41button.setOnClickListener(this);
			setProperty();
			setTitle("有标题");
			show();
		}
		
		public void setProperty(){
			 Window window=getWindow();
			 WindowManager.LayoutParams wl = window.getAttributes();
			 wl.x =0;//这两句设置了对话框的位置．0为中间
			          wl.y =0;
			          //wl.alpha=0.6f;//这句设置了对话框的透明度
			          wl.gravity=Gravity.BOTTOM;       
			          window.setAttributes(wl);

		 }

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.l41_textView1:
				dismiss();
				showDialog();
				break;
			case R.id.l41_textView2:
				Intent i = new Intent().setClass(Change.this, fangda.class);
				Change.this.startActivity(i);
				dismiss();
				break;
			case R.id.l41_button3:
				dismiss();
				break;
			default:
				break;
			}
			
		}
		
	}
	
	 
	
	
	private void showDialog(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
	    alertDialog.setTitle("设置头像");
	    alertDialog.setItems(itens, new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
		switch (which) {
		case 0:
			Intent intentFromGallery = new Intent();
			intentFromGallery.setType("image/*");
			intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);			
			break;
		case 1:
			Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if(Tool.hasSdcard()){
				tempFile = new File(Environment.getExternalStorageDirectory(),  
						IMAGE_FILE_NAME);  
	            // 从文件中创建uri  
	            Uri uri = Uri.fromFile(tempFile);  
	            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			}
			startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
			break;
		default:
			break;
		}	
		}
	}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	}).show();}
	
	
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		   if(resultCode != RESULT_CANCELED){
			   switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (Tool.hasSdcard()) {
					//File tempFile = new File(Environment.getExternalStorageDirectory()+ IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(Change.this, "未找到存储卡，无法存储照片！",
							Toast.LENGTH_LONG).show();}
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;

			default:
				break;
			}
		   }super.onActivityResult(requestCode, resultCode, data);

	   }
	
	
	
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);
	}
	


	private void getImageToView(Intent data){
		   Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				Drawable drawable = new BitmapDrawable(photo);
				my4image.setImageDrawable(drawable);
			}
	   }
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		String name = msharepreference.getStringProperty("name");
		l4_textView7.setText(name);
	}

}
