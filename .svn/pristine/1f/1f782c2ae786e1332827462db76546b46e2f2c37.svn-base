package com.fanjie.activity.infor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.block.jhguide.R;
import com.block.jhguide.R.id;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;


public class Information extends Activity {
	private ImageButton imagebutton;
	private TextView textview1;
	private TextView textview2;
	private TextView textview3;
	private TextView textview4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lifeinformation);
		getActionBar().hide();
		imagebutton=(ImageButton)findViewById(R.id.GovernInfomIB);
		imagebutton.setOnClickListener(new onBackClickLitener());
		textview1=(TextView)findViewById(R.id.schinfotxt1);
		textview2=(TextView)findViewById(R.id.schinfotxt2);       
		textview3=(TextView)findViewById(R.id.schinfotxt3);
		textview4=(TextView)findViewById(R.id.schinfotxt4);
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		ArrayList list=bundle.getParcelableArrayList("Info");
		textview1.setText(list.get(0).toString());
		textview3.setText(list.get(2).toString());
        textview4.setText(list.get(3).toString());
	    String intentSpace="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";			
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	    Matcher m = p.matcher(list.get(1).toString());
	    String info=m.replaceAll("");
	    StringBuffer sb=new StringBuffer();
	    sb.append("<html>");
        sb.append("<body>");
	    sb.append("<pre>");
		sb.append(intentSpace);
		sb.append(info);
		sb.append("</pre>");
		sb.append("</body>");
		sb.append("</html>");
		textview2.setText(Html.fromHtml(sb.toString()));
	    //textview2.setText(m.replaceAll(""));
	}
	class onBackClickLitener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
	 
	}
		


}
