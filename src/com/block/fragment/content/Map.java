package com.block.fragment.content;

import jhunplay.com.fanjie.Constant.Constant;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.AMap.CancelableCallback;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
//import com.amapv1.apis.util.Constants;

import com.amap.api.maps2d.model.MyLocationStyle;
import com.block.activity.map.OverlayDemo;
import com.block.jhguide.R;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Map extends Fragment implements OnMarkerClickListener, OnMapLoadedListener, LocationSource, AMapLocationListener {

	private MapView mapView;
	private AMap aMap;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.home_map, null);
		mapView = (MapView) view.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		init();
		return view;
	}

	/**
	 * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
	 */
	private void changeCamera(CameraUpdate update, CancelableCallback callback) {
		aMap.animateCamera(update, 100, callback);
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(Constant.JIANGHAN_UNIVERSITY, 15, 0, 0)), null);
			setUpMap();
		}
//		aMap = mapView.getMap();
//		changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(Constant.JIANGHAN_UNIVERSITY, 15, 0, 0)), null);
//		setUpMap();
		aMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), OverlayDemo.class);
				startActivity(intent);
			}
		});
	}

	private void setUpMap() {

		MyLocationStyle myLocationStyle = new MyLocationStyle();

		myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.argb(240, 2, 135, 252));// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(60, 161, 202, 243));// 设置圆形的填充颜
		// myLocationStyle.anchor(0,0);//设置小蓝点的锚点
		myLocationStyle.strokeWidth(3.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// aMap.setMyLocationType()
//		aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器

		UiSettings uiSettings = aMap.getUiSettings();
		uiSettings.setCompassEnabled(true);
		uiSettings.setZoomControlsEnabled(false);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		mapView.destroyDrawingCache();
		deactivate();
	}

	
	/**
	 * 方法必须重写
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
	


	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		LatLngBounds bounds = new LatLngBounds.Builder().include(Constant.JIANGHAN_UNIVERSITY).build();
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 15));
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(getActivity());
			mAMapLocationManager.setGpsEnable(true);
			mAMapLocationManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
		System.gc();
	}
	

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		// TODO Auto-generated method stub
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
		}
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		Log.i("onMarkerClick", "点击我的位置");
		Intent intent = new Intent(getActivity(), OverlayDemo.class);
		startActivity(intent);
		onPause();
		return false;
	}
	
	
	


}
