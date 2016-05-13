package com.block.activity.map;

import org.casey.bitmap.KJBitmap;
import org.casey.bitmap.KJBitmapConfig;

import jhunplay.com.db.DBService;
import jhunplay.com.db.MapPoint;
import jhunplay.com.fanjie.Constant.Constant;

import com.ab.bitmap.AbImageDownloader;
import com.ab.util.AbImageUtil;
import com.block.jhguide.R;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MapPointDetails extends Activity {

	MapPoint mapPoint;
	DBService dbService;
	ImageView mappoint_image;
	TextView mappoint_name, mappoint_details;

	private AbImageDownloader mAbImageDownloader = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.school_map_pointdetails);

		initview();

	}

	private void initview() {
		mappoint_image = (ImageView) findViewById(R.id.mappoint_image);
		mappoint_name = (TextView) findViewById(R.id.mappoint_name);
		mappoint_details = (TextView) findViewById(R.id.mappoint_details);

		KJBitmap kjBitmap = KJBitmap.create();

		Bundle bundle = getIntent().getExtras();
		dbService = new DBService();
		String id = bundle.getString("id");
		if (!TextUtils.isEmpty(id)) {
			mapPoint = dbService.selectPoint(this, id);
			kjBitmap.display(mappoint_image, Constant.URL3 + mapPoint.pic);
			mappoint_name.setText(mapPoint.name);
			if (id.equals("0")) {
				mappoint_details.setText(getResources().getString(R.string.jhun_university));
			}else {
				mappoint_details.setText(mapPoint.details);
			}
			
		}

	}

	public void Back(View view) {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.finish();
		super.onBackPressed();
	}
}
