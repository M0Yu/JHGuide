package jhunplay.com.fanjie.ovrideLayout;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.json.JSONObject;

import jhunplay.com.block.tool.ImageLoader;
import jhunplay.com.fanjie.Constant.Constant;
import jhunplay.com.fanjie.tool.Msharepreference;
import jhunplay.com.fanjie.tool.Tool;

import com.ab.bitmap.AbImageDownloader;

import com.block.jhguide.R;

import android.app.Activity;
import android.content.Context;

import android.graphics.Bitmap;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * 自定义的ScrollView，在其中动态地对图片进行添加。
 * 
 * @author guolin
 */
public class MyScrollView extends ScrollView implements OnTouchListener {

	/**
	 * 每页要加载的图片数量
	 */
	public static final int PAGE_SIZE = 7;

	// private ArrayList<Map<String, Object>> list = new ArrayList<Map<String,
	// Object>>();
	/**
	 * 记录当前已加载到第几页
	 */
	private int page = 0;

	/**
	 * 每一列的宽度
	 */
	private int columnWidth;

	/**
	 * 当前第一列的高度
	 */
	private int firstColumnHeight;

	/**
	 * 当前第二列的高度
	 */
	private int secondColumnHeight;

	/**
	 * 当前第三列的高度
	 */

	/**
	 * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
	 */
	private boolean loadOnce;
	private boolean loadOnce1;

	/**
	 * 对图片进行管理的工具类
	 */
	private ImageLoader imageLoader;

	/**
	 * 第一列的布局
	 */
	private LinearLayout firstColumn;

	/**
	 * 第二列的布局
	 */
	private LinearLayout secondColumn;

	/**
	 * 第三列的布局
	 */

	/**
	 * 记录所有正在下载或等待下载的任务。
	 */
	private static Set<LoadImageTask> taskCollection;

	/**
	 * MyScrollView下的直接子布局。
	 */
	private static View scrollLayout;

	/**
	 * MyScrollView布局的高度。
	 */
	private static int scrollViewHeight;

	/**
	 * 记录上垂直方向的滚动距离。
	 */
	private static int lastScrollY = -1;
	/**
	 * 记录所有界面上的图片，用以可以随时控制对图片的释放。
	 */

	private List<ImageView> imageViewList = new ArrayList<ImageView>();
	private Msharepreference msharepreference;
	private ArrayList<Map<String, Object>> myList = new ArrayList<Map<String, Object>>();
	private ImageView imageView;

	/**
	 * 在Handler中进行图片可见性检查的判断，以及加载更多图片的操作。
	 */
	private static Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			MyScrollView myScrollView = (MyScrollView) msg.obj;
			int scrollY = myScrollView.getScrollY();
			// 如果当前的滚动位置和上次相同，表示已停止滚动
			if (scrollY == lastScrollY) {
				// 当滚动的最底部，并且当前没有正在下载的任务时，开始加载下一页的图片
				if (scrollViewHeight + scrollY >= scrollLayout.getHeight()
						&& taskCollection.isEmpty()) {
					myScrollView.loadMoreImages();
				}
				myScrollView.checkVisibility();
			} else {
				lastScrollY = scrollY;
				Message message = new Message();
				message.obj = myScrollView;
				// 5毫秒后再次对滚动位置进行判断
				handler.sendMessageDelayed(message, 5);
			}
		};

	};

	/**
	 * MyScrollView的构造函数。
	 * 
	 * @param context
	 * @param attrs
	 */

	private Context context;

	public void onReturn() {
		if (msharepreference.getStringProperty("user_id").equals("")) {
			UUID uuid = UUID.randomUUID();
			String user_id = uuid.toString();
			msharepreference.setStringProperty("user_id", user_id);
			Constant.user = true;
		}
		JSONObject jsonObject = Tool.getJSONObject(0, "user_id",
				msharepreference.getStringProperty("user_id"), "ids",
				"myreme_pic");
		Refresh load = new Refresh();
		load.execute(jsonObject);

	}

	// public void onRefresh() {
	// JSONObject jsonObject = Tool.getJSONObject(0, "user_id",
	// msharepreference.getStringProperty("user_id"), "ids",
	// "myreme_pic");
	// RefreshList load = new RefreshList();
	// load.execute(jsonObject);
	//
	// }
	private AbImageDownloader downloader;

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		msharepreference = new Msharepreference(context, "information");
		imageLoader = ImageLoader.getInstance();
		taskCollection = new HashSet<LoadImageTask>();
		downloader = new AbImageDownloader(context);
		setOnTouchListener(this);
	}

	/**
	 * 进行一些关键性的初始化操作，获取MyScrollView的高度，以及得到第一列的宽度值。并在这里开始加载第一页的图片。
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed && !loadOnce1) {

			scrollViewHeight = getHeight();
			scrollLayout = getChildAt(0);
			firstColumn = (LinearLayout) findViewById(R.id.first_column);
			secondColumn = (LinearLayout) findViewById(R.id.second_column);
			columnWidth = firstColumn.getWidth();
			loadOnce1 = true;
			// JSONObject jsonObject = Tool.getJSONObject(0, "user_id",
			// msharepreference.getStringProperty("user_id"), "ids",
			// "myreme_pic");
			// downLoad load = new downLoad();
			// load.execute(jsonObject);
			// loadMoreImages();
		}
	}

	/**
	 * 监听用户的触屏事件，如果用户手指离开屏幕则开始进行滚动检测。
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Message message = new Message();
			message.obj = this;
			handler.sendMessageDelayed(message, 5);
		}
		return false;
	}

	/**
	 * 开始加载下一页的图片，每张图片都会开启一个异步线程去下载。
	 */
	public void loadMoreImages() {
		if (hasSDCard()) {

			int startIndex = page * PAGE_SIZE;
			int endIndex = page * PAGE_SIZE + PAGE_SIZE;

			if (startIndex < myList.size()) {
				Toast.makeText(getContext(), "正在加载...", Toast.LENGTH_SHORT)
						.show();
				if (endIndex > myList.size()) {
					endIndex = myList.size();
				}
				String praise;
				String pic_id;
				String pic_collection;
				String pic_urls;
				for (int i = startIndex; i < endIndex; i++) {
					LoadImageTask task = new LoadImageTask();
					taskCollection.add(task);
					pic_urls = myList.get(i).get("pic_url").toString();
					pic_id = myList.get(i).get("id").toString();
					praise = myList.get(i).get("praise").toString();
					pic_collection = myList.get(i).get("collection").toString();
					task.execute(Constant.URL3 + pic_urls, pic_id, praise,
							pic_collection);

				}

				page++;

			} else {
				Toast.makeText(getContext(), "已没有更多图片", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(getContext(), "未发现SD卡", Toast.LENGTH_SHORT).show();
		}

	}

	public void reFreshImage() {
		// String pic_id;
		// String praise;
		// String pic_collection;
		// String pic_position;
		for (int i = 0; i < imageViewList.size(); i++) {
			// pic_id = myList.get(i).get("id").toString();
			// praise = myList.get(i).get("praise").toString();
			// pic_collection = myList.get(i).get("collection").toString();
			imageViewList.get(i).setTag(R.string.mpraise,
					myList.get(i).get("praise").toString());
			imageViewList.get(i).setTag(R.string.mcollnation,
					myList.get(i).get("collection").toString());
			imageViewList.get(i).setTag(R.string.mid,
					myList.get(i).get("id").toString());
		}
	}

	/**
	 * 遍历imageViewList中的每张图片，对图片的可见性进行检查，如果图片已经离开屏幕可见范围，则将图片替换成一张空图。
	 */
	public void checkVisibility() {
		for (int i = 0; i < imageViewList.size(); i++) {
			ImageView imageView = imageViewList.get(i);
			int borderTop = (Integer) imageView.getTag(R.string.border_top);
			int borderBottom = (Integer) imageView
					.getTag(R.string.border_bottom);
			String imageUrl = null;
			String pic_id;
			String praise;
			String pic_collection;
			if (borderBottom > getScrollY()
					&& borderTop < getScrollY() + scrollViewHeight) {
				imageUrl = (String) imageView.getTag(R.string.image_url);
				pic_id = (String) imageView.getTag(R.string.mid);
				praise = (String) imageView.getTag(R.string.mpraise);
				pic_collection = (String) imageView
						.getTag(R.string.mcollnation);
				Bitmap bitmap = imageLoader.getBitmapFromMemoryCache(imageUrl);
				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);
				} else {
					LoadImageTask task = new LoadImageTask(imageView);
					task.execute(imageUrl, pic_id, praise, pic_collection);
				}
			} else {
				imageView.setImageResource(R.drawable.empty_photo);
			}
		}
	}
//decodeSampledBitmapFromResource
	/**
	 * 判断手机是否有SD卡。
	 * 
	 * @return 有SD卡返回true，没有返回false。
	 */
	private boolean hasSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	/**
	 * 异步下载图片的任务。
	 * 
	 * @author guolin
	 */
	class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

		/**
		 * 图片的URL地址
		 */
		private String mImageUrl;
		private String pic_id;
		private String praise;
		private String pic_collection;

		/**
		 * 可重复使用的ImageView
		 */
		private ImageView mImageView;

		public LoadImageTask() {
		}

		/**
		 * 将可重复使用的ImageView传入
		 * 
		 * @param imageView
		 */
		public LoadImageTask(ImageView imageView) {
			mImageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap = null;
			mImageUrl = params[0];
			pic_id = params[1];
			praise = params[2];
			pic_collection = params[3];
			Bitmap imageBitmap = imageLoader
					.getBitmapFromMemoryCache(mImageUrl);
			if (imageBitmap == null) {
				imageBitmap = loadImage(mImageUrl);
			}if(imageBitmap!=null){
			double ratio = imageBitmap.getWidth() / (columnWidth * 1.0);
			int scaledHeight = (int) (imageBitmap.getHeight() / ratio);
			bitmap = Bitmap.createScaledBitmap(imageBitmap, columnWidth,
					scaledHeight, true);
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap != null) {
				double ratio = bitmap.getWidth() / (columnWidth * 1.0);
				int scaledHeight = (int) (bitmap.getHeight() / ratio);
				addImage(bitmap, columnWidth, scaledHeight);

			}
			taskCollection.remove(this);
		}

		/**
		 * 根据传入的URL，对图片进行加载。如果这张图片已经存在于SD卡中，则直接从SD卡里读取，否则就从网络上下载。
		 * 
		 * @param imageUrl
		 *            图片的URL地址
		 * @return 加载到内存的图片。
		 */
		private Bitmap loadImage(String imageUrl) {
			File imageFile = new File(getImagePath(imageUrl));
			if (!imageFile.exists()) {
				downloadImage(imageUrl);
			}
			if (imageUrl != null) {
				Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(
						imageFile.getPath(), columnWidth);
				if (bitmap != null) {
					imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
					return bitmap;
				}
			}
			return null;
		}

		/**
		 * 向ImageView中添加一张图片
		 * 
		 * @param bitmap
		 *            待添加的图片
		 * @param imageWidth
		 *            图片的宽度
		 * @param imageHeight
		 *            图片的高度
		 */

		private void addImage(Bitmap bitmap, int imageWidth, int imageHeight) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					imageWidth, imageHeight);
			params.setMargins(0, 0, 0, 5);

			if (mImageView != null) {
				mImageView.setImageBitmap(bitmap);
			} else {
				imageView = new MyImageView(getContext());
				imageView.setLayoutParams(params);
				imageView.setImageBitmap(bitmap);
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView.setPadding(5, 5, 5, 5);
				imageView.setBackgroundResource(R.color.white);
				imageView.setTag(R.string.image_url, mImageUrl);
				imageView.setTag(R.string.mpraise, praise);
				imageView.setTag(R.string.mcollnation, pic_collection);
				imageView.setTag(R.string.mid, pic_id);

				findColumnToAdd(imageView, imageHeight).addView(imageView);
				imageViewList.add(imageView);
			}
		}

		/**
		 * 找到此时应该添加图片的一列。原则就是对三列的高度进行判断，当前高度最小的一列就是应该添加的一列。
		 * 
		 * @param imageView
		 * @param imageHeight
		 * @return 应该添加图片的一列
		 */
		private LinearLayout findColumnToAdd(ImageView imageView,
				int imageHeight) {
			if (firstColumnHeight <= secondColumnHeight) {
				imageView.setTag(R.string.border_top, firstColumnHeight);
				firstColumnHeight += imageHeight;
				imageView.setTag(R.string.border_bottom, firstColumnHeight);
				return firstColumn;
			} else {
				imageView.setTag(R.string.border_top, secondColumnHeight);
				secondColumnHeight += imageHeight;
				imageView.setTag(R.string.border_bottom, secondColumnHeight);
				return secondColumn;
			}
		}

		/**
		 * 将图片下载到SD卡缓存起来。
		 * 
		 * @param imageUrl
		 *            图片的URL地址。
		 */
		private void downloadImage(String imageUrl) {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				Log.d("TAG", "monted sdcard");
			} else {
				Log.d("TAG", "has no sdcard");
			}
			HttpURLConnection con = null;
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			BufferedInputStream bis = null;
			File imageFile = null;
			try {
				URL url = new URL(imageUrl);
				con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5 * 1000);
				con.setReadTimeout(15 * 1000);
				con.setDoInput(true);
				con.setDoOutput(true);
				bis = new BufferedInputStream(con.getInputStream());
				imageFile = new File(getImagePath(imageUrl));
				fos = new FileOutputStream(imageFile);
				bos = new BufferedOutputStream(fos);
				byte[] b = new byte[1024];
				int length;
				while ((length = bis.read(b)) != -1) {
					bos.write(b, 0, length);
					bos.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (bis != null) {
						bis.close();
					}
					if (bos != null) {
						bos.close();
					}
					if (con != null) {
						con.disconnect();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (imageFile != null) {
				Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(
						imageFile.getPath(), columnWidth);
				if (bitmap != null) {
					imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
				}
			}
		}

		/**
		 * 获取图片的本地存储路径。
		 * 
		 * @param imageUrl
		 *            图片的URL地址。
		 * @return 图片的本地存储路径。
		 */
		private String getImagePath(String imageUrl) {
			int lastSlashIndex = imageUrl.lastIndexOf("/");
			String imageName = imageUrl.substring(lastSlashIndex + 1);
			String imageDir = Environment.getExternalStorageDirectory()
					.getPath() + "/PhotoWallFalls/";
			File file = new File(imageDir);
			if (!file.exists()) {
				file.mkdirs();
			}
			String imagePath = imageDir + imageName;
			return imagePath;
		}
	}

	private getMyActivity activity;

	public void onGetName(getMyActivity activity) {
		this.activity = activity;
	}

	public interface getMyActivity {
		public Class<? extends Activity> getName();
	}

	public class downLoad extends AsyncTask<JSONObject, Integer, String> {

		String[] id = new String[] { "id" };
		String[] parise = new String[] { "parise" };
		String[] collection = new String[] { "collection" };

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

			myList.clear();
			myList = Tool.Json(result, "id", "pic_url", "praise", "collection");

			loadMoreImages();

		}
	}

	public class Refresh extends AsyncTask<JSONObject, Integer, String> {

		String[] id = new String[] { "id" };
		String[] parise = new String[] { "parise" };
		String[] collection = new String[] { "collection" };

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

			myList = Tool.Json(result, "id", "pic_url", "praise", "collection");
			if (!loadOnce) {
				reFreshImage();
				loadMoreImages();
				loadOnce = true;
			} else {
				reFreshImage();
			}
		}
	}

	// public class RefreshList extends AsyncTask<JSONObject, Integer, String> {
	//
	// String[] id = new String[] { "id" };
	// String[] parise = new String[] { "parise" };
	// String[] collection = new String[] { "collection" };
	//
	// @Override
	// protected String doInBackground(JSONObject... params) {
	// // TODO Auto-generated method stub
	// String result = Tool.registPost(params[0]);
	// return result;
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// // TODO Auto-generated method stub
	// super.onPostExecute(result);
	// myList.clear();
	// myList = Tool.Json(result, "id", "pic_url", "praise", "collection");
	//
	// }
	// }
}