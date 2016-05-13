package jhunplay.com.fanjie.adapter;

import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ab.bitmap.AbImageDownloader;
import com.ab.util.AbImageUtil;
import com.ab.util.AbViewHolder;
import com.block.jhguide.R;
import com.lidroid.xutils.BitmapUtils;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{
	private Context mContext;
	// xml转View对象
	private LayoutInflater mInflater;
	// 单行的布局
	private int mResource;
	// 列表展现的数据
	private List mData;
	// Map中的key
	private String[] mFrom;
	// view的id
	private int[] mTo;
	private AbImageDownloader mAbImageDownloader = null;
	private BitmapUtils bitmapUtils;
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}
    
	public MyAdapter(Context context, List data, int resource,
			String[] from, int[] to)
	{
		this.mContext = context;
    	this.mData = data;
    	this.mResource = resource;
    	this.mFrom = from;
    	this.mTo = to;
    	this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	 mAbImageDownloader = new AbImageDownloader(mContext);
    	 bitmapUtils=new BitmapUtils(context);
//         mAbImageDownloader.setWidth(100);
//         mAbImageDownloader.setHeight(100);
//         mAbImageDownloader.setType(AbImageUtil.SCALEIMG);
//         mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
//         mAbImageDownloader.setErrorImage(R.drawable.image_error);
//         mAbImageDownloader.setNoImage(R.drawable.image_no);
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (arg1 == null) {
			//使用自定义的list_items作为Layout
			arg1 = mInflater.inflate(mResource, arg2, false);
			
		}

		ImageView imageview   = AbViewHolder.get(arg1,mTo[0] );
		TextView LifeInfoTxt2 = AbViewHolder.get(arg1, mTo[1]);
		TextView LifeInfoTxt3 = AbViewHolder.get(arg1, mTo[2]);
//		RelativeLayout relative=(RelativeLayout)arg1.findViewById(R.id.schinforl);
		RelativeLayout relative = AbViewHolder.get(arg1, R.id.schinforl);
//		ImageView itemsIcon_3 = AbViewHolder.get(arg2, mTo[2]);
		
		/*LayoutParams params = itemsIcon_1.getLayoutParams() ;
		params.width = (int) (size.width/3);
		itemsIcon_1.setLayoutParams(params);
		
		params = itemsIcon_2.getLayoutParams() ;
		params.width = (int) (size.width/3);
		itemsIcon_2.setLayoutParams(params);
		
		params = itemsIcon_3.getLayoutParams() ;
		params.width = (int) (size.width/3);
		itemsIcon_3.setLayoutParams(params);
		itemsIcon_1.setLayoutParams(params);*/
		// 获取该行的数据
		final Map<String, Object> obj = (Map<String, Object>) mData
				.get(arg0);
        String imageUrl=(String)obj.get(mFrom[0]);
        if( imageUrl.equals("null")){
         	imageview.setVisibility(View.GONE);
            relative.setVisibility(View.GONE);
        }else{
        	imageview.setVisibility(View.VISIBLE);
            relative.setVisibility(View.VISIBLE);
//        	mAbImageDownloader.display(imageview, imageUrl);
            MyDown  down =new MyDown(imageview);
            down.execute(imageUrl);
  //      	bitmapUtils.display(imageview, imageUrl);
        }
		LifeInfoTxt2.setText((String)obj.get(mFrom[1]));
		LifeInfoTxt3.setText((String)obj.get(mFrom[2]));
		
		/*android.view.ViewGroup.LayoutParams params=arg1.getLayoutParams();
		params.width=400;
		arg1.setLayoutParams(primes);*/
		return arg1;
	}
	
	public class MyDown extends AsyncTask<String, Integer, Bitmap> {

		String text;
		ImageView imageView;
		public MyDown(ImageView imageview){
			this.imageView=imageview;
		}
		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			Bitmap bitmap = loadImage(params[0]);
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			// TODO Auto-generated method stub
			super.onPostExecute(bitmap);
			imageView.setImageBitmap(bitmap);
		}

		private Bitmap loadImage(String imageUrl) {
			Bitmap bitmap = null;
			if (imageUrl != null) {
				bitmap = downloadImage(imageUrl);

			}
			return bitmap;
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
		
		

		
		

		