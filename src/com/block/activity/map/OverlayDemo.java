package com.block.activity.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jhunplay.com.db.DBService;
import jhunplay.com.db.MapPoint;
import jhunplay.com.fanjie.Constant.Constant;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbFileUtil;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.CancelableCallback;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
//import com.amapv1.apis.util.Constants;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.block.jhguide.R;

public class OverlayDemo extends Activity implements OnMarkerClickListener,
		OnMapLoadedListener, InfoWindowAdapter, LocationSource,
		AMapLocationListener, OnClickListener {

	private MapView mapView;
	private AMap aMap;
	private Location mLocation;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	public EditText top_search_text;
	public Button top_back, top_search_btn, top_jhun, bottom_scenery_btn,
			bottom_department_btn, bottom_serve_btn, bottom_eat_btn,
			bottom_dormitory_btn;
	public TextView bottom_scenery_text, bottom_department_text,
			bottom_serve_text, bottom_eat_text, bottom_dormitory_text;
	public View bottom_scenery_view, bottom_department_view, bottom_serve_view,
			bottom_eat_view, bottom_dormitory_view;
	public ImageView MapStyle, goMyLocation;

	DBService dbService = new DBService();
	ArrayList<MapPoint> mapPointList = new ArrayList<MapPoint>();// 地图坐标点的实体类list
	ArrayList<MarkerOptions> mapMarkerOptions = new ArrayList<MarkerOptions>();// 地图显示点
	MyLocationStyle myLocationStyle;
	ListView searchList;
	TopSearchListAdapter topSearchListAdapter;
	public int map_level = 15;
	public boolean map_style = true;
	LatLngBounds centerLatLngBounds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ActivitySplitAnimationUtil.prepareAnimation(this);
		setContentView(R.layout.school_map);
		centerLatLngBounds = new LatLngBounds.Builder().include(
				Constant.JIANGHAN_UNIVERSITY).build();
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		init();

	}

	/**
	 * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
	 */
	private void changeCamera(CameraUpdate update, CancelableCallback callback) {
		aMap.animateCamera(update, 500, callback);
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			changeCamera(
					CameraUpdateFactory.newCameraPosition(new CameraPosition(
							Constant.JIANGHAN_UNIVERSITY, map_level, 0, 0)),
					null);
			setUpMap();
		}
		top_search_text = (EditText) findViewById(R.id.school_map_top_search_text);
		top_back = (Button) findViewById(R.id.school_map_top_back);
		top_search_btn = (Button) findViewById(R.id.school_map_top_search_btn);
		top_jhun = (Button) findViewById(R.id.school_map_top_jhun);
		bottom_scenery_btn = (Button) findViewById(R.id.school_map_bottom_scenery_btn);
		bottom_scenery_text = (TextView) findViewById(R.id.school_map_bottom_scenery_text);
		bottom_scenery_view = (View) findViewById(R.id.school_map_bottom_scenery_view);

		bottom_department_btn = (Button) findViewById(R.id.school_map_bottom_department_btn);
		bottom_department_text = (TextView) findViewById(R.id.school_map_bottom_department_text);
		bottom_department_view = (View) findViewById(R.id.school_map_bottom_department_view);

		bottom_serve_btn = (Button) findViewById(R.id.school_map_bottom_sever_btn);
		bottom_serve_text = (TextView) findViewById(R.id.school_map_bottom_sever_text);
		bottom_serve_view = (View) findViewById(R.id.school_map_bottom_sever_view);

		bottom_eat_btn = (Button) findViewById(R.id.school_map_bottom_eat_btn);
		bottom_eat_text = (TextView) findViewById(R.id.school_map_bottom_eat_text);
		bottom_eat_view = (View) findViewById(R.id.school_map_bottom_eat_view);

		bottom_dormitory_btn = (Button) findViewById(R.id.school_map_bottom_dormitory_btn);
		bottom_dormitory_text = (TextView) findViewById(R.id.school_map_bottom_dormitory_text);
		bottom_dormitory_view = (View) findViewById(R.id.school_map_bottom_dormitory_view);

		searchList = (ListView) findViewById(R.id.msearchListView);
		topSearchListAdapter = new TopSearchListAdapter(mapPointList);
		searchList.setAdapter(topSearchListAdapter);

		findViewById(R.id.change_map_style).setOnClickListener(this);
		findViewById(R.id.goMyLocation).setOnClickListener(this);

		top_back.setOnClickListener(this);
		top_search_btn.setOnClickListener(this);
		top_jhun.setOnClickListener(this);
		bottom_scenery_view.setOnClickListener(this);
		bottom_department_view.setOnClickListener(this);
		bottom_serve_view.setOnClickListener(this);
		bottom_eat_view.setOnClickListener(this);
		bottom_dormitory_view.setOnClickListener(this);

		searchList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				addMarkersToMap(mapPointList.get(arg2));
				// onMapLoaded();
				aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
						centerLatLngBounds, map_level));
				searchList.setVisibility(View.GONE);
			}
		});

		// ActivitySplitAnimationUtil.animate(this, 1000);

	}

	private void setUpMap() {

		myLocationStyle = new MyLocationStyle();

		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.argb(240, 2, 135, 252));// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(60, 161, 202, 243));// 设置圆形的填充颜
		// myLocationStyle.anchor(0,0);//设置小蓝点的锚点
		myLocationStyle.strokeWidth(3.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// aMap.setMyLocationType()

		ChangeMapStyle();
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		// addMarkersToMap();// 往地图上添加marker

		UiSettings uiSettings = aMap.getUiSettings();
		uiSettings.setMyLocationButtonEnabled(false);
		uiSettings.setCompassEnabled(true);
		uiSettings.setZoomControlsEnabled(false);
	}

	// 添加单个图标
	private void addMarkersToMap(MapPoint mapPoint) {
		clearOtherMarket();
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.snippet(mapPoint.id + "-0");
		markerOptions.icon(BitmapDescriptorFactory
				.fromResource(getResId("map_pop_big")));
		markerOptions.anchor(0.5f, 0.5f);
		markerOptions.position(new LatLng(mapPoint.xlat, mapPoint.xlong));
		markerOptions.title(mapPoint.name).draggable(true);
		aMap.addMarker(markerOptions);
		aMap.invalidate();
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
				new LatLngBounds.Builder().include(markerOptions.getPosition())
						.build(), map_level));
	}

	// 按照类别在地图中添加图标
	private void addTypemarkersToMap(ArrayList<MarkerOptions> markerOptionLists) {
		clearOtherMarket();
		for (Iterator iterator = markerOptionLists.iterator(); iterator
				.hasNext();) {
			aMap.addMarker((MarkerOptions) iterator.next());
		}
		aMap.invalidate();
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(centerLatLngBounds,
				map_level));
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		mapView.destroyDrawingCache();
		deactivate();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		finish();
		System.gc();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BREAK) {
			finish();
			System.gc();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public View getInfoContents(Marker marker) {
		// TODO Auto-generated method stub
		View infoWindow = getLayoutInflater().inflate(R.layout.map_popup, null);
		render(marker, infoWindow);
		return infoWindow;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

	// 清除除当前位置以外的图标
	public void clearOtherMarket() {
		List<Marker> markList = aMap.getMapScreenMarkers();
		for (int i = 0; i < markList.size(); i++) {
			try {
				if (!TextUtils.isEmpty(markList.get(i).getTitle())) {
					markList.get(i).destroy();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void render(final Marker marker, View view) {

		String title = marker.getTitle();
		changeMarkerIcon(marker);
		TextView titleUi = ((TextView) view.findViewById(R.id.text));
		if (title != null) {
			SpannableString titleText = new SpannableString(title);
			titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
					titleText.length(), 0);
			titleUi.setTextSize(15);
			titleUi.setText(titleText);
		} else {
			titleUi.setText("");
		}
		LinearLayout popLayout = (LinearLayout) view
				.findViewById(R.id.LinearLayoutPopup);
		popLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (marker.getSnippet().equals("-1-0")) {
					onBackPressed();
					finish();
				} else {
					Bundle bundle = new Bundle();
					Log.i("id_bundle", marker.getSnippet());
					bundle.putString("id", getMarkerId(marker.getSnippet()));
					gotoActivity(MapPointDetails.class, bundle);
				}
			}
		});
		aMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				if (marker.isInfoWindowShown()) {
					marker.hideInfoWindow();
				}
			}
		});
	}

	public int getResId(String name) {
		// String packageName = getApplicationInfo().packageName;
		return getResources().getIdentifier(name, "drawable",
				"com.block.jhguide");
	}

	public void changeMarkerIcon(Marker marker) {
		List<Marker> markList = aMap.getMapScreenMarkers();
		for (int i = 0; i < markList.size(); i++) {
			try {
				if (markList.get(i).getTitle().equals(marker.getTitle())) {
					marker.setIcon(BitmapDescriptorFactory
							.fromResource(getResId("map_pop_big")));
				} else if (!TextUtils.isEmpty(markList.get(i).getTitle())) {
					markList.get(i).setIcon(
							BitmapDescriptorFactory
									.fromResource(getResId("map_pop_"
											+ getMarkerIconName(markList.get(i)
													.getSnippet()))));
					// markList.get(i).setIcons(markList.get(i).getIcons());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * public void jumpPoint(final Marker marker) { final LatLng tempLatLng =
	 * marker.getPosition(); final Handler handler = new Handler(); final long
	 * start = SystemClock.uptimeMillis(); Projection proj =
	 * aMap.getProjection(); Point startPoint =
	 * proj.toScreenLocation(tempLatLng); final LatLng startLatLng =
	 * proj.fromScreenLocation(startPoint); final long duration = 1500;
	 * 
	 * final Interpolator interpolator = new BounceInterpolator();
	 * handler.post(new Runnable() {
	 * 
	 * @Override public void run() { long elapsed = SystemClock.uptimeMillis() -
	 * start; float t = interpolator.getInterpolation((float) elapsed /
	 * duration); double lng = t * tempLatLng.longitude + (1 - t) *
	 * startLatLng.longitude; double lat = t * tempLatLng.latitude + (1 - t) *
	 * startLatLng.latitude; marker.setPosition(new LatLng(lat, lng));
	 * aMap.invalidate();// 刷新地图 if (t < 1.0) { handler.postDelayed(this, 5); }
	 * } });
	 * 
	 * }
	 */
	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(centerLatLngBounds,
				map_level));

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			MapPoint mapPoint = new MapPoint();
			mapPoint.name = bundle.getString("name");
			mapPoint.xlong = bundle.getDouble("xlong");
			mapPoint.xlat = bundle.getDouble("xlat");
			mapPoint.icon = "map_pop_big";
			mapPoint.id = bundle.getString("style");
			addMarkersToMap(mapPoint);
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		return false;
	}

	@Override
	public void onLocationChanged(Location arg0) {

	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
		}
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			mAMapLocationManager.setGpsEnable(true);
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.school_map_top_back:
			onBackPressed();
			break;
		case R.id.school_map_top_jhun:
			Bundle bundle = new Bundle();
			bundle.putString("id", "0");
			gotoActivity(MapPointDetails.class, bundle);
			break;
		case R.id.school_map_top_search_btn:
			searchMapPoints(top_search_text.getText().toString());
			break;
		case R.id.school_map_bottom_scenery_view:
			showTypeMarket(0);
			setBottomBottonBG(true, false, false, false, false);
			break;
		case R.id.school_map_bottom_department_view:
			showTypeMarket(4);
			setBottomBottonBG(false, true, false, false, false);
			break;
		case R.id.school_map_bottom_eat_view:
			showTypeMarket(2);
			setBottomBottonBG(false, false, false, true, false);
			break;
		case R.id.school_map_bottom_sever_view:
			showTypeMarket(3);
			setBottomBottonBG(false, false, true, false, false);
			break;
		case R.id.school_map_bottom_dormitory_view:
			showTypeMarket(1);
			setBottomBottonBG(false, false, false, false, true);
			break;
		case R.id.change_map_style:
			ChangeMapStyle();
			break;
		case R.id.goMyLocation:
			goMyLocation();
			break;

		default:
			break;
		}
	}

	public void ChangeMapStyle() {

		if (map_style) {
			aMap.setMapType(AMap.MAP_TYPE_NORMAL);
		} else {
			aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
		}
		map_style = !map_style;
	}

	public void goMyLocation() {
		if (aMap.isMyLocationEnabled()) {
			mLocation = aMap.getMyLocation();
			if (mLocation != null) {
				LatLng mLatLng = new LatLng(mLocation.getLatitude(),
						mLocation.getLongitude());
				mListener.onLocationChanged(aMap.getMyLocation());
				changeCamera(
						CameraUpdateFactory.newCameraPosition(new CameraPosition(
								mLatLng, map_level, 0, 0)), null);
			}
		}
	}

	// 显示某个类型的图标
	public void showTypeMarket(int type) {
		Log.i("显示图标类别", type + "");
		searchList.setVisibility(View.GONE);
		mapPointList.clear();
		mapPointList = dbService.selectTypePoint(OverlayDemo.this, type + "");
		PointToMarket(mapPointList);
		addTypemarkersToMap(mapMarkerOptions);
	}

	// mapPoint 转化为地图坐标点
	public void PointToMarket(ArrayList<MapPoint> mapPointList) {

		mapMarkerOptions.clear();
		for (int i = 0; i < mapPointList.size(); i++) {
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.snippet(mapPointList.get(i).id + "-" + (i + 1));
			markerOptions.icon(BitmapDescriptorFactory
					.fromResource(getResId(mapPointList.get(i).icon)));
			markerOptions.anchor(0.5f, 0.5f);
			markerOptions.position(new LatLng(mapPointList.get(i).xlat,
					mapPointList.get(i).xlong));
			markerOptions.title(mapPointList.get(i).name).draggable(true);
			mapMarkerOptions.add(markerOptions);
		}
	}

	public String getMarkerId(String str) {
		int start = str.indexOf("-");
		return str.substring(0, start);
	}

	public String getMarkerIconName(String str) {
		int start = str.indexOf("-");
		return str.substring(start + 1, str.length());
	}

	// Activity跳转
	public void gotoActivity(Class<? extends Activity> to, Bundle bundle) {
		Intent intent = new Intent(this, to);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void searchMapPoints(String str) {
		if (str.length() > 0) {
			mapPointList.clear();
			mapPointList = dbService.selectFuzzyPoint(OverlayDemo.this, str);
			if (mapPointList.size() > 0) {
				searchList.setVisibility(View.VISIBLE);
				topSearchListAdapter.setData(mapPointList);
				topSearchListAdapter.notifyDataSetChanged();
			} else {
				Toast.makeText(OverlayDemo.this, "查无结果", 100).show();
			}

		} else {
			Toast.makeText(OverlayDemo.this, "请输入内容查找", 100).show();
		}
	}

	// 设置底部按钮背景
	public void setBottomBG(Button btn, TextView text, boolean show, int bg_n,
			int bg_p) {
		if (show) {
			btn.setBackgroundResource(bg_p);
			text.setTextColor(getResources().getColor(R.color.textorange));
		} else {
			btn.setBackgroundResource(bg_n);
			text.setTextColor(getResources().getColor(R.color.gray_black));
		}
	}

	public void setBottomBottonBG(boolean b1, boolean b2, boolean b3,
			boolean b4, boolean b5) {
		setBottomBG(bottom_scenery_btn, bottom_scenery_text, b1,
				R.drawable.school_map_bottom_scenery,
				R.drawable.school_map_scenery_p);
		setBottomBG(bottom_department_btn, bottom_department_text, b2,
				R.drawable.school_map_bottom_department,
				R.drawable.school_map_department_p);
		setBottomBG(bottom_serve_btn, bottom_serve_text, b3,
				R.drawable.school_map_bottom_sever,
				R.drawable.school_map_serve_p);
		setBottomBG(bottom_eat_btn, bottom_eat_text, b4,
				R.drawable.school_map_bottom_eat, R.drawable.school_map_eat_p);
		setBottomBG(bottom_dormitory_btn, bottom_dormitory_text, b5,
				R.drawable.school_map_bottom_dormitory,
				R.drawable.school_map_dormitory_p);
	}

	class TopSearchListAdapter extends BaseAdapter {

		public ArrayList<MapPoint> mapPoints = new ArrayList<MapPoint>();

		public TopSearchListAdapter(ArrayList<MapPoint> mapPoints) {
			this.mapPoints = mapPoints;
		}

		public void setData(ArrayList<MapPoint> mapPoints) {
			this.mapPoints = mapPoints;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mapPoints.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView search_item;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.top_search_listitem, null);
				search_item = (TextView) convertView
						.findViewById(R.id.search_item);
				convertView.setTag(search_item);
			} else {
				search_item = (TextView) convertView.getTag();
			}
			search_item.setText(mapPoints.get(position).name);

			return convertView;
		}
	}
}
