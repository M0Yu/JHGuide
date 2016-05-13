package com.fanjie.activity.rightSlider;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jhunplay.com.fanjie.Constant.Constant;
import jhunplay.com.fanjie.tool.Msharepreference;
import jhunplay.com.fanjie.tool.Tool;

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

import com.ab.http.AbHttpResponseListener;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.block.activity.main.WelcomeActivity;
import com.block.jhguide.R;
import com.cx.second_deal.Sundries_deal_history;
import com.fanjie.activity.thoseyear.ImageDetailsActivity;
import com.fanjie.activity.thoseyear.commentary;
import com.fanjie.activity.thoseyear.commentary_detials;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class lw_submit_myGoods extends Activity {
	private ImageButton buttonBack;
	private ImageView imageView;
	private Button submit_button, button_P, button_C, button_no;
	private EditText goodsName, goodsInfo, goodsPhoto, call_type;
	private Spinner mySpinner;
	private String text_name, text_info, text_photo, theType, pic_uri = null,
			text_call_type, time_date;
	private int myposition;
	private View view;
	private String actionUrl = "http://61.142.71.63:9090/UpLoad/up.jsp";// jsp的網址

	private String ImagePath2;

	private OnClickListener clickListener;
	private static final int IMAGE_P = 0;
	private static final int CAMERA_C = 1;
	private static final int PIC_O = 2;
	private LinearLayout myLayout;
	private String mSavePath;
	private AlertDialog log;
	private String photoTimeName;
	private AbHttpUtil abHttpUtil;
	private Msharepreference myMs;
	private Tool mTool;
	private ProgressDialog pro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lw_submit_goods);

		myMs = new Msharepreference(this, "information");
		mTool = new Tool(this);

		myLayout = (LinearLayout) this.findViewById(R.id.myLayout);
		myLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				// goodsName.setCursorVisible(false);//失去光标
				// goodsInfo.setCursorVisible(false);
				// goodsPhoto.setCursorVisible(false);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

			}
		});

		buttonBack = (ImageButton) this.findViewById(R.id.buttonBack);
		buttonBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		imageView = (ImageView) this.findViewById(R.id.imageView);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				showMyDialog();
			}

		});

		goodsName = (EditText) this.findViewById(R.id.text_name);
		goodsInfo = (EditText) this.findViewById(R.id.text_describe);
		goodsPhoto = (EditText) this.findViewById(R.id.text_photo);
		call_type = (EditText) this.findViewById(R.id.call_type);

		mySpinner = (Spinner) this.findViewById(R.id.myspinner);
		mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView tv = (TextView) view;
				tv.setTextColor(getResources().getColor(R.color.white));
				myposition = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		submit_button = (Button) this.findViewById(R.id.buttonSend);
		submit_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 向服务器 提交数据
				text_name = goodsName.getText().toString();
				text_info = goodsInfo.getText().toString();
				text_photo = goodsPhoto.getText().toString();
				text_call_type = call_type.getText().toString();
				theType = lw_submit_myGoods.this.getResources().getStringArray(
						R.array.typeNumArrays)[myposition];

				long time = System.currentTimeMillis();
				SimpleDateFormat date_time = new SimpleDateFormat("yyyy-MM-dd");
				time_date = date_time.format(time);

				if (pic_uri == null) {
					Toast.makeText(lw_submit_myGoods.this, "上传数据不能有空的~哟",
							Toast.LENGTH_SHORT).show();
				} else if (!text_name.equals("") && !text_info.equals("")
						&& !text_call_type.equals("") && !text_photo.equals("")
						&& pic_uri != null) {
					mytools();
				} else {
					goodsName.setHint("物品名不能为空");
					Toast.makeText(lw_submit_myGoods.this, "上传数据不能有空的~哟",
							Toast.LENGTH_SHORT).show();
				}

				if (!text_name.equals("") && !text_info.equals("")
						&& !text_photo.equals("") && !text_call_type.equals("")
						&& pic_uri != null && ImagePath2 != null) {
					proDia();
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Looper.prepare();
							bitmapToString(ImagePath2);
							upLoadFile();
							Looper.loop();
						}
					}).start();
				}

			}
		});
	}

	// http://blog.csdn.net/huangbiao86/article/details/7100702

	// 自定义 底部弹出的 Dialog

	public void showMyDialog() {
		log = new AlertDialog.Builder(this).create();
		log.show();
		log.setCanceledOnTouchOutside(false);
		Window window = log.getWindow();

		window.setGravity(Gravity.BOTTOM);
		window.setContentView(R.layout.lw_submit_show_my_dialog);
		window.setWindowAnimations(R.style.Dialog_anim);

		button_P = (Button) window.findViewById(R.id.button_P);
		button_C = (Button) window.findViewById(R.id.button_C);
		button_no = (Button) window.findViewById(R.id.button_no);
		setOnclickListener();

	}

	// 等待Dialog
	public void proDia() {
		pro = ProgressDialog.show(this, null, "正在提交数据......");
		pro.setCanceledOnTouchOutside(false);
		pro.setCancelable(true);
	}

	private void setOnclickListener() {
		clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (v == button_P) {

					Intent intent_P = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent_P, IMAGE_P);

				} else if (v == button_C) {
					Intent intent_C = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "myBusiness/";
					File file = new File(mSavePath);
					System.out.println("file  " + file);
					if (!file.exists()) {
						file.mkdir();
					}
					photoTimeName = System.currentTimeMillis() + ".jpg";
					Uri imageUri = Uri.fromFile(new File(mSavePath,
							photoTimeName));
					System.out.println("imageUri-----> " + imageUri);
					intent_C.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);		
					startActivityForResult(intent_C, CAMERA_C);

				} else {
					log.dismiss();
				}
				if (log.isShowing()) {
					log.dismiss();
				}
			}
		};
		button_P.setOnClickListener(clickListener);
		button_C.setOnClickListener(clickListener);
		button_no.setOnClickListener(clickListener);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// if(requestCode !=RESULT_CANCELED){

		switch (requestCode) {
		// 相册中选取的图片
		case IMAGE_P:
			// Toast.makeText(Le.this, "hao", 1).show();
			if (data != null) {
				Uri uri = data.getData();
				// System.out.println("uri--->" + uri);
				// System.out.println("uri--->" + uri.toString());
				if (!uri.toString().contains("file:///")) {
					ImagePath2 = getPath(uri);
					//System.out.println("相册选取ImagePath2--->" + ImagePath2);
					pic_uri = ImagePath2.substring(ImagePath2
							.lastIndexOf("/") + 1);

					Bitmap photo=compressImageFromFile(ImagePath2);
			//		Bitmap photo = BitmapFactory.decodeFile(getPath(uri));
					imageView.setImageBitmap(photo);
				} else {
					ImagePath2 = uri.toString().substring(7,
							uri.toString().length());
					// System.out.println("小米云相册选取ImagePath2--->"+ImagePath2);
					imageView.setImageURI(uri);
					pic_uri = uri.toString().substring(
							uri.toString().lastIndexOf("/") + 1);
				}
			}
			break;
		// 拍照的图片
		case CAMERA_C:
			if (resultCode == -1) { // 等于 -1 表示相机拍照了
				pic_uri = photoTimeName;
				ImagePath2 = mSavePath + photoTimeName;
				int numberDegree = readPictureDegree(ImagePath2);
				if(numberDegree !=0){
                Bitmap bt = rotaingImageView(numberDegree,ImagePath2);
                ImagePath2 = compressBmpToFile(bt,ImagePath2);
				}
				Bitmap myphoto =compressImageFromFile(ImagePath2);
//				Bitmap myphoto = BitmapFactory.decodeFile(ImagePath2);
				imageView.setImageBitmap(myphoto);
			}
			break;
		default:
			break;
		}
		// }
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void mytools() {
		MyGood mygood = new MyGood();
		JSONObject jsons = new JSONObject();
		try {
			jsons.put("ids", "second_hand_exchange_new");
			jsons.put("rid", myMs.getStringProperty("user_id"));
			jsons.put("object_name", text_name);
			jsons.put("object_description", text_info);
			jsons.put("img_url", pic_uri);
			jsons.put("type", theType);
			jsons.put("tel", text_photo);
			jsons.put("style", text_call_type);
			jsons.put("sub_time", time_date);
			mygood.execute(jsons);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public class MyGood extends AsyncTask<JSONObject, Void, String> {

		@Override
		protected String doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub

			String result = Tool.registPost(params[0]);
			Log.i("结果", result);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {
				JSONObject jsobj = new JSONObject(result);
				System.out.println("结果-->" + jsobj.get("msg"));

				// String m = jsobj.get("msg").toString();
				// if(m.equals("执行成功")){
				// Toast.makeText(lw_submit_myGoods.this, "数据上传成功~",
				// Toast.LENGTH_SHORT)
				// .show();
				// }
				// else{
				// Toast.makeText(lw_submit_myGoods.this,"数据上传失败~",
				// Toast.LENGTH_SHORT)
				// .show();
				// }
				//

			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	// 通过URi 获取图片的绝对路径
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	


	private void upLoadFile() {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		try {
			URL url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设置传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file1\";filename=\"" + pic_uri + "\"" + end);
			ds.writeBytes(end);
			/* 取得文件的FileInputStream */
			FileInputStream fStream = new FileInputStream(ImagePath2);
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			/* 取得Response内容 */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}

			/* 将Response显示于Dialog */
			// showDialog("图片上传成功"+b.toString().trim());
			if (pro.isShowing()) {
				pro.dismiss();
			}
			showDialog("数据上传成功");
			/* 关闭DataOutputStream */
			ds.close();
		} catch (Exception e) {
			// e.printStackTrace();
			if (pro.isShowing()) {
				pro.dismiss();
			}
			showDialog("数据上传失败");
		}
	}

	/* 显示Dialog的method */
	private void showDialog(String mess) {
		new AlertDialog.Builder(lw_submit_myGoods.this).setTitle("Message")
				.setMessage(mess)
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// finish();
						Intent intent = new Intent(lw_submit_myGoods.this,
								Sundries_deal_history.class);
						lw_submit_myGoods.this.setResult(2, intent);
						lw_submit_myGoods.this.finish();
					}
				}).show();

	}

	//对图片进行压缩处理
	
	// 计算图片的缩放值
	public int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	// 把bitmap转换成String
	public void bitmapToString(String filePath) {
		Bitmap bm = getSmallBitmap(filePath);
		File dirFile = new File("/sdcard/myBusiness");
		if(!dirFile.exists()){
			dirFile.mkdir();
		}
		File file = new File("/sdcard/myBusiness/"+pic_uri);
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.JPEG, 50, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		ImagePath2 = file.toString();
	}
	
	 private Bitmap compressImageFromFile(String srcPath) {  
	        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
	        newOpts.inJustDecodeBounds = true;//只读边,不读内容  
	        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
	  
	        newOpts.inJustDecodeBounds = false;  
	        int w = newOpts.outWidth;  
	        int h = newOpts.outHeight;  
	        float hh = 800f;//  
	        float ww = 480f;//  
	        int be = 1;  
	        if (w > h && w > ww) {  
	            be = (int) (newOpts.outWidth / ww);  
	        } else if (w < h && h > hh) {  
	            be = (int) (newOpts.outHeight / hh);  
	        }  
	        if (be <= 0)  
	            be = 1;  
	        newOpts.inSampleSize = be;//设置采样率  
	          
	        newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设  
	        newOpts.inPurgeable = true;// 同时设置才会有效  
	        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收  
	          
	        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
//	      return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩  
	                                    //其实是无效的,大家尽管尝试  
	        return bitmap;  
	    }  
	 
	 
	    /** 
	     * 读取图片属性：旋转的角度 
	     * @param path 图片绝对路径 
	     * @return degree旋转的角度 
	     */  
	       public static int readPictureDegree(String path) {  
	           int degree  = 0;  
	           try {  
	                   ExifInterface exifInterface = new ExifInterface(path);  
	                   int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);  
	                   switch (orientation) {  
	                   case ExifInterface.ORIENTATION_ROTATE_90:  
	                           degree = 90;  
	                           break;  
	                   case ExifInterface.ORIENTATION_ROTATE_180:  
	                           degree = 180;  
	                           break;  
	                   case ExifInterface.ORIENTATION_ROTATE_270:  
	                           degree = 270;  
	                           break;  
	                   }  
	           } catch (IOException e) {  
	                   e.printStackTrace();  
	           }  
	           return degree;  
	       }  
	       
	       /* 旋转图片 
	       * @param angle 
	       * @param bitmap 
	       * @return Bitmap 
	       */  
	      public static Bitmap rotaingImageView(int angle ,String path) {
	    	  
	    	  Bitmap bitmap = BitmapFactory.decodeFile(path);
	          //旋转图片 动作  
	          Matrix matrix = new Matrix();;  
	          matrix.postRotate(angle);  
	          System.out.println("angle2=" + angle);  
	          // 创建新的图片  
	          Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
	                  bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
	          return resizedBitmap;  
	      }
	      
	      
	      
	      public static String compressBmpToFile(Bitmap bmp,String file){
              ByteArrayOutputStream baos = new ByteArrayOutputStream();
             
              bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
           
              try {
                      FileOutputStream fos = new FileOutputStream(file);
                      fos.write(baos.toByteArray());
                      fos.flush();
                      fos.close();
              } catch (Exception e) {
                      e.printStackTrace();
              }
              
              return file;
      }
}
