package jhunplay.com.fanjie.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Parser;

import android.R.integer;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.bitmap.AbImageDownloader;
import com.ab.util.AbImageUtil;
import com.block.jhguide.R;

import com.lidroid.xutils.BitmapUtils;


//import com.andbase.global.Constant;

public class Msc_ImageListAdapter extends BaseAdapter {
	
	private Context mContext;
	//xml转View锟斤拷锟斤拷
    private LayoutInflater mInflater;
    //锟斤拷锟叫的诧拷锟斤拷
    private int mResource;
    //锟叫憋拷展锟街碉拷锟斤拷锟�
    private List mData;
    //Map锟叫碉拷key
    private String[] mFrom;
    //view锟斤拷id
    private int[] mTo;
    //图片锟斤拷锟斤拷锟斤拷
    private AbImageDownloader mAbImageDownloader = null;
    private BitmapUtils bitmapUtils;
    private boolean isPraise=true;
    private boolean isCollection=true;
    private Msharepreference msharepreference;
    private String sessionkey;
    private String user_id;
    private final int storeType=0;
    private boolean isDianZanClick=false;
    private boolean isCollectionClick=false;
//    private ViewHolder holder;
    private List<ViewHolder> holders=new ArrayList<Msc_ImageListAdapter.ViewHolder>();
    private int itemPosition;
    private  Map<String, Object>  obj;
    private int position;
    private View convertView;
   // private ViewHolder holder=null;
//    private Button solidPraiseButton;
   /**
    * 锟斤拷锟届方锟斤拷
    * @param context
    * @param data 锟叫憋拷展锟街碉拷锟斤拷锟�
    * @param resource 锟斤拷锟叫的诧拷锟斤拷
    * @param from Map锟叫碉拷key
    * @param to view锟斤拷id
    */
    public Msc_ImageListAdapter(Context context, List data,
            int resource, String[] from, int[] to){
    	this.mContext = context;
    	this.mData = data;
    	this.mResource = resource;
    	this.mFrom = from;
    	this.mTo = to;
        //锟斤拷锟节斤拷xml转为View
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.bitmapUtils = new BitmapUtils(context);

        //图片锟斤拷锟斤拷锟斤拷
        mAbImageDownloader = new AbImageDownloader(mContext);
//        mAbImageDownloader.setWidth(100);
//        mAbImageDownloader.setHeight(100);
//        mAbImageDownloader.setType(AbImageUtil.SCALEIMG);
//        mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
//        mAbImageDownloader.setErrorImage(R.drawable.image_error);
//        mAbImageDownloader.setNoImage(R.drawable.image_no);
        
        msharepreference=new Msharepreference(context,"information");
        sessionkey=msharepreference.getStringProperty("sessionkey");
        user_id=msharepreference.getStringProperty("user_id");
        
        
    }   
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(  int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//holders=new ArrayList<Msc_ImageListAdapter.ViewHolder>();		
		final ViewHolder holder;
//		View view=convertView;
	    obj = (Map<String, Object>)mData.get(position);
	    this.position = position;
	    this.convertView = convertView;
		if(convertView == null){
	           //使锟斤拷锟皆讹拷锟斤拷锟絣ist_items锟斤拷为Layout
			 holder = new ViewHolder();
		 	
			 convertView = mInflater.inflate(mResource, parent, false);
//	           view=LayoutInflater.from(mContext).inflate(R.layout.list,null);
	
			holder.image = (ImageView) convertView.findViewById(mTo[0]);
			holder.name = (TextView) convertView.findViewById(mTo[1]);
			holder.info = (TextView) convertView.findViewById(mTo[2]);
//			holder.ratingBar = (RatingBar) convertView.findViewById(mTo[3]);
//			holder.dianzan = (ImageButton) convertView.findViewById(mTo[4]);
//			holder.solidPraiseTextView = (TextView) convertView.findViewById(R.id.solidPraiseTextView);
     		holder.shoucang = (ImageButton) convertView.findViewById(R.id.sc);
     		holder.shoucang.setTag(R.string.storeid, obj.get("id").toString());
//			holder.collectTextView = (TextView) convertView.findViewById(R.id.collectTextView);
//			holder.pinglun = (ImageButton) convertView.findViewById(R.id.pl);
//			holder.commentTextView = (TextView) convertView.findViewById(R.id.commentTextView);
	        convertView.setTag( holder);
       }
		else{
    	   holder = (ViewHolder) convertView.getTag();
    	   
       }	   
		
//		holder.dianzan.setOnClickListener(new OnClickListener() {
//
//			//private	int i = 1;
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				//i++;
//				
//				System.out.println("wsh------->ajdkasdfnsladfndsnfds;lkfcnsdklfn");
//				isDianZanClick=true;
//				itemPosition=position;
//				if(isPraise==false){
//					JSONObject jsonObject=new JSONObject();
//					try {
//						jsonObject.put("ids","praise_store");
//						jsonObject.put("user_id",user_id);
//						jsonObject.put("store_id",obj.get("id").toString() );
//						jsonObject.put("type", storeType);
//						jsonObject.put("sessionkey", sessionkey);
//						getStringHttp gsh=new getStringHttp();
//						gsh.execute(jsonObject);
//						Thread.sleep(1000);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}else if(isPraise==true){
//					JSONObject jsonObject=new JSONObject();
//					try {
//						jsonObject.put("ids","delete_parise");
//						jsonObject.put("user_id",user_id);
//						jsonObject.put("store_id",obj.get("id").toString());
//						jsonObject.put("type", storeType);
//						jsonObject.put("sessionkey", sessionkey);
//						getStringHttp gsh=new getStringHttp();
//						gsh.execute(jsonObject);
//						Thread.sleep(1000);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				
//				
//			}	
//		});
		    
			holder.shoucang.setOnClickListener(new OnClickListener() {
//			private int j = 1;
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//				j++;

//					isCollectionClick=true;
					if (isCollection==false) {
						holder.shoucang.setBackgroundResource(R.drawable.solid_star);
						isCollection=true;
						JSONObject jsonObject=new JSONObject();
						try {
							jsonObject.put("ids","collection_store");
							jsonObject.put("user_id",user_id);
							jsonObject.put("store_id", v.getTag(R.string.storeid));
							jsonObject.put("type", storeType);
//							jsonObject.put("sessionkey", sessionkey);
							getStringHttp gsh=new getStringHttp();
							gsh.execute(jsonObject);
	
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if(isCollection==true){
						holder.shoucang.setBackgroundResource(R.drawable.solid_star_before);
						isCollection=false;
						JSONObject jsonObject=new JSONObject();
						try {
							jsonObject.put("ids","delete_collection");
							jsonObject.put("user_id",user_id);
							jsonObject.put("store_id",obj.get("id").toString());
							jsonObject.put("type",storeType);
//							jsonObject.put("sessionkey", sessionkey);
							getStringHttp gsh=new getStringHttp();
							gsh.execute(jsonObject);
		
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}			
				}
			});	
			
			//锟斤拷取锟斤拷锟叫碉拷锟斤拷锟�
		   
			String imageUrl = (String)obj.get("image");
			holder.name.setText((String)obj.get("name"));
			holder.info.setText((String)obj.get("info"));   
//			if(obj.get("ratingBar").equals("null")){                    //判断是否为空无需转成string
//				holder.ratingBar.setRating(0);
//			}
//			else{
//				//holder.ratingBar.setRating(3);
//				holder.ratingBar.setRating(Float.parseFloat(obj.get("ratingBar").toString()));                
//			}
//			if(obj.get("dianzan")=="null"){
//				isPraise=false;
//				holder.dianzan.setBackgroundResource(R.drawable.dianzan_before);
//			}else{
//				isPraise=true;
//				holder.dianzan.setBackgroundResource(R.drawable.dianzan_after);
//			}
			holder.shoucang.setBackgroundResource(R.drawable.solid_star);
			mAbImageDownloader.display(holder.image, imageUrl);
			return convertView;
		}
		
//		Handler handler=new Handler(){
//			public void handleMessage(Message msg) {
//				String string =msg.obj.toString();
//				if(isDianZanClick==true){
//					int x=(int) (itemPosition+Math.pow(2, itemPosition/2+1)-2);
//					if(isPraise==false){		
//						if(string.contains("执行成功")){
//							Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT).show();
//							isPraise=true;							
//							holders.get(x).dianzan.setBackgroundResource(R.drawable.dianzan_after);
//							
//						}else {
//							Toast.makeText(mContext, "点赞失败", Toast.LENGTH_SHORT).show();
//							isPraise=false;
//							holders.get(x).dianzan.setBackgroundResource(R.drawable.dianzan_before);
//							
//						}
//					}else if(isPraise==true){
//						if(string.contains("执行成功")){
//							Toast.makeText(mContext, "取消点赞成功", Toast.LENGTH_SHORT).show();
//							isPraise=false;
//							holders.get(x).dianzan.setBackgroundResource(R.drawable.dianzan_before);
//							
//						}else {
//							Toast.makeText(mContext, "取消点赞失败", Toast.LENGTH_SHORT).show();
//							isPraise=true;
//							holders.get(x).dianzan.setBackgroundResource(R.drawable.dianzan_after);
//							
//						}
//					}
//					isDianZanClick=false;
//					
//				}else if(isCollectionClick==true){
//					int x=(int) (itemPosition+Math.pow(2, itemPosition/2+1)-2);
//					if(isCollection==false){
//						if(string.contains("执行成功")){						
//							Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
//							holders.get(x).shoucang.setBackgroundResource(R.drawable.solid_star);
//							isCollection=true;
//						}else{
//							Toast.makeText(mContext, "收藏失败", Toast.LENGTH_SHORT).show();
//							holders.get(x).shoucang.setBackgroundResource(R.drawable.solid_star_before);
//							isCollection=false;
//						}			
//					}else if(isCollection==true){
//						if(string.contains("执行成功")){
//							Toast.makeText(mContext, "取消收藏成功", Toast.LENGTH_SHORT).show();
//							holders.get(x).shoucang.setBackgroundResource(R.drawable.solid_star_before);
//							isCollection=false;
//						}else{
//							Toast.makeText(mContext, "取消收藏失败", Toast.LENGTH_SHORT).show();
//							holders.get(x).shoucang.setBackgroundResource(R.drawable.solid_star);
//							isCollection=true;
//						}
//					}
//					isCollectionClick=false;
//				}
//				
//				
//			};
//		};
		
		public class getStringHttp extends AsyncTask<JSONObject,Integer,String>{
			@Override
			protected String doInBackground(JSONObject... params) {
				// TODO Auto-generated method stub
				String result = Tool.registPost(params[0]);
//				String result = null;
//				try {
//					List<NameValuePair> parms = new ArrayList<NameValuePair>();
//					String sessionkey;
//					String id = params[0].optString("ids");
//					params[0].remove("ids");
//					
//					HttpClient client = new DefaultHttpClient();
//					HttpPost post = new HttpPost(Constant.URL2);
//					sessionkey = params[0].optString("sessionkey");
//					params[0].remove("sessionkey");
//					parms.add(new BasicNameValuePair("sessionkey", sessionkey));
//					parms.add(new BasicNameValuePair("ids", id));
//					parms.add(new BasicNameValuePair("params", params[0].toString()));
//					post.setEntity(new UrlEncodedFormEntity(parms));
//					HttpResponse response = client.execute(post);
//					if (response.getStatusLine().getStatusCode() == 200) {
//						result = EntityUtils.toString(response.getEntity());
//						Message msg=new Message();
//						msg.obj=result;
//						handler.sendMessage(msg);
						
	
				//return result;
				return result;
				
			}
						
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
				if(result.contains("执行成功")){
				Toast.makeText(mContext, "操作成功", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
			}
			
				
			super.onPostExecute(result);
		}
	}

 public static class ViewHolder {
		ImageView image;
		TextView name;
		TextView info;
//		RatingBar ratingBar;
//		ImageButton dianzan;
		ImageButton shoucang;
//		ImageButton pinglun;
		
	}
}
		
				
				
						
						
						
					
				
				
			
		
		
