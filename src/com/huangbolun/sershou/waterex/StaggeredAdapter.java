package com.huangbolun.sershou.waterex;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.block.jhguide.R;
import com.huangbolun.ershou.android.bitmapfun.util.ImageFetcher;
import com.huangbolun.ershou.model.DuitangInfo;

public class StaggeredAdapter extends BaseAdapter {
    private LinkedList<DuitangInfo> mInfos;
    ImageFetcher mImageFetcher;
   
Context context1;
DuitangInfo duitangInfo;
    public StaggeredAdapter(Context context, ImageFetcher f) {
        mInfos = new LinkedList<DuitangInfo>();
        mImageFetcher = f;
        context1=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
         duitangInfo = mInfos.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
            convertView = layoutInflator.inflate(R.layout.huangbolun_infos_list, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.news_pic);
            
            holder.contentView = (TextView) convertView.findViewById(R.id.news_title);
            holder.contentView1 =(TextView)convertView.findViewById(R.id.textView1);
            holder.contentView2 =(TextView)convertView.findViewById(R.id.news_time); 
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
       
        
//        float iHeight = ((float) 200 / 183 * duitangInfo.getHeight());
        holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) duitangInfo.getHeight()));
        final String msg=duitangInfo.getObject_description();
        final String object_name =duitangInfo.getObject_name(); 
        final String tel = duitangInfo.getTel();
        final String tel_type = duitangInfo.getTel_type();
        final String type = duitangInfo.getType();
        final String sub_time = duitangInfo.getSub_time();
        final String img = duitangInfo.getIsrc();
        
        holder.contentView.setText(object_name);
        holder.contentView1.setText(msg);
        holder.contentView2.setText(duitangInfo.getType());
        mImageFetcher.loadImage(duitangInfo.getIsrc(), holder.imageView);
        convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("msg",msg);
				intent.putExtra("object_name",object_name);
				intent.putExtra("tel",tel);
				intent.putExtra("tel_type",tel_type);
				intent.putExtra("type",type);
				intent.putExtra("sub_time",sub_time);
	            intent.putExtra("img", img);
				intent.setClass(context1, Activity1.class);
				v.getContext().startActivity(intent); 
			}
		});
        return convertView;
       
    }

    class ViewHolder {
        ImageView imageView;
        TextView contentView;
        TextView contentView1;
        TextView contentView2;
        TextView timeView;
    }

    @Override
    public int getCount() {
        return mInfos.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mInfos.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    public void addItemLast(List<DuitangInfo> datas) {
        mInfos.addAll(datas);
    }

    public void addItemTop(List<DuitangInfo> datas) {
        for (DuitangInfo info : datas) {
            mInfos.addFirst(info);
        }
    }
}
