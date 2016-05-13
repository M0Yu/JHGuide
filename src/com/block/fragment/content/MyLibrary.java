package com.block.fragment.content;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.block.jhguide.R;
import com.fanjie.activity.rightSlider.MyUpdate.GetHttpString;

import jhunplay.com.fanjie.Constant.Constant;
import jhunplay.com.fanjie.tool.Msharepreference;
import jhunplay.com.fanjie.tool.Tool;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MyLibrary {

	/* 是否取消更新 */
	private boolean cancelUpdate = false;
	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	/* 记录进度条数量 */
	private int progress;
	/* 显示加载百分数 */
	private TextView proText;

	private Dialog mDownloadDialog;
	/* 下载保存路径 */
	private String mSavePath;

	// 下载状态
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 停止下载 */
	private static final int DOWNLOAD_FINISH = 2;

	// 实例例化
	private Msharepreference myshare;

	private Tool mTool;

	private String filepath = new String();
	// 存放APP的下载地址
	private String murl;

	// 加载弹框
	private ProgressDialog pro;
	private String APK; 
	private String ID;
	public MyLibrary(Context context, String APK) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.APK = APK;
		if(APK.equals("com.example.liberary")){
			filepath = "江大图书馆";
			ID = "0";
		} else {
			filepath = "我的课表";
			ID = "1";
		}
		System.out.println("APK = "+ APK);
		System.out.println("filepath = "+filepath);
		myshare = new Msharepreference(mContext, "information");
		mTool = new Tool(context);
		if (isNetworkConnected()) {
			if (!isAPPInstalled(APK)) {
				proDia();
				GerUrl();
			} else {
				startApp(APK);
			}
		} else {
			Toast.makeText(mContext, "当前网络无法连接", 0).show();
		}
	}

	// 判断网络是否连接
	public boolean isNetworkConnected() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			return true;
		}
		return false;
	}

	// 加载Dialog
	public void proDia() {
		pro = ProgressDialog.show(mContext, "加载中", "正在加载......");
		pro.setCanceledOnTouchOutside(false);
		pro.setCancelable(true);
	}

	// 自定义提示软件更新的弹窗
	public void myShowDialog() {
		if (pro.isShowing()) {
			pro.dismiss();
		}
		final AlertDialog log = new AlertDialog.Builder(mContext).create();
		log.show();
		log.setCanceledOnTouchOutside(false);
		Window window = log.getWindow();
		window.setContentView(R.layout.lyz_new_myupdate_dialog);

		Button ok = (Button) window.findViewById(R.id.ok);
		ok.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				log.dismiss();
				// 显示下载对话框
				showDownloadDialog();
			}
		});

		Button no = (Button) window.findViewById(R.id.no);
		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// log.dismiss();
				log.cancel();

			}
		});

	}

	// 定义软件下载对话框
	protected void showDownloadDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setCancelable(false);
		builder.setTitle("正在更新");
		// 设置进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.softupdate_progress_lw, null);
		mProgress = (ProgressBar) view.findViewById(R.id.progress_lw);
		proText = (TextView) view.findViewById(R.id.proText);
		builder.setView(view);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface log, int arg1) {
				// TODO Auto-generated method stub
				log.dismiss();
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 下载文件
		downloadApk();
	}

	// 判断是否安装了该应用
	public boolean isAPPInstalled(String APPName) {
		try {
			PackageManager pm = mContext.getPackageManager();
			pm.getPackageInfo(APPName, PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			return false;
		}
		return true;
	}

	// 超链接启动图书馆应用
	public void startApp(String packageName) {
		Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(
				packageName);
		if (intent != null)
			mContext.startActivity(intent);
	}

	// 下载应用
	private void downloadApk() {
		// TODO Auto-generated method stub
		// 启动新线程下载软件
		new mthread().start();
	}

	// 下载APP的线程
	private class mthread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 判断SD卡是否存在，并且是否具有读写权限

			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String sdpath = Environment.getExternalStorageDirectory() + "/";
				// "download为手机中的download 文件夹"
				// mSavePath = sdpath+"download";

				// 在内存卡更目录生成文件夹 玩转江大，来存放下载的apk
				mSavePath = sdpath + "玩转江大";
				try {
					URL url = new URL(murl);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();
					File file = new File(mSavePath);
					// 判断文件是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, filepath +".apk");
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					do {
						// 读取字节数
						int numread = is.read(buf);
						count = count + numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度条
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate); // 点击取消下载
					fos.close();
					is.close();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 取消对话框
				mDownloadDialog.dismiss();
			}
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD:
				// 下载设置进度条
				mProgress.setProgress(progress);
				proText.setText("loading..." + progress + "%");
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;
			default:
				break;
			}
		}
	};

	// 安装文件
	protected void installApk() {
		// TODO Auto-generated method stub
		/*
		 * File(String parent, String child) 根据 parent 路径名字符串和 child 路径名字符串创建一个新
		 * File 实例。 那么你这句File f=new file(path,File.text); 的意思就是 根据
		 * path和File里的静态变量text组合而成的路径 来创建一个新 File 实例。
		 */
		File apkfFile = new File(mSavePath, filepath +".apk");
		if (!apkfFile.exists()) {
			return;
		}
		/*
		 * Intent intent = new Intent(); intent.setAction(Intent.ACTION_VIEW);
		 * File file = new
		 * File(Environment.getExternalStorageDirectory(),"HtmlUI1.apk");
		 * intent.setDataAndType(Uri.fromFile(file),
		 * "application/vnd.android.package-archive"); startActivity(intent);
		 */
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfFile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}

	// 获取url网址
	private void GerUrl() {
		JSONObject jsons = new JSONObject();
		try {
			jsons.put("ids", "more");
			jsons.put("id", ID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GetHttpString mgGet = new GetHttpString();
		mgGet.execute(jsons);
	}

	// 异步线程获取下载网址
	public class GetHttpString extends AsyncTask<JSONObject, Void , String> {

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

			ArrayList<Map<String, Object>> mylist = new ArrayList<Map<String, Object>>();
			mylist = Tool.Json(result, "id","apkurl");
			for(Map<String, Object> var:mylist){
				if(var.get("id").equals(ID)){
					murl = Constant.URL3+var.get("apkurl");
				}
			}
			myShowDialog();
		}
	}

}
