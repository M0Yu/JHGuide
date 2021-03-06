package com.block.fragment.content;

import java.util.ArrayList;
import java.util.List;

import jhunplay.com.fanjie.Constant.Constant;
import jhunplay.com.fanjie.ovrideLayout.AbBottomTabViewc;

import com.block.activity.government.XueGongBu;
import com.block.jhguide.R;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.ab.adapter.AbFragmentPagerAdapter;
import com.ab.view.sliding.AbTabItemView;

//import com.andbase.R;

public class Government extends Fragment {

	// content页面容器
	private ViewPager pager;
	private AbBottomTabViewc mBottomTabView;
	private AbFragmentPagerAdapter adapter;
	private GestureDetector gestureDetector;
	private AbTabItemView tabView;
	private OnClickListener mTabClickListener = new OnClickListener() {
		public void onClick(View view) {
			tabView = (AbTabItemView) view;
			pager.setCurrentItem(tabView.getIndex());
			// System.out.println(tabView.getIndex());
		}
	};

	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		view = inflater.inflate(R.layout.tab_bottom, null);
		gestureDetector = new GestureDetector(getActivity(),
				gestureDetectorListenner);
		mBottomTabView = (AbBottomTabViewc) view
				.findViewById(R.id.mBottomTabView1);// 加一个view就可以在fragment未加到activity时不用getview()就可得到ＩＤ
		// currentitem=mBottomTabView.getCurrentItem();
		// ActionBar abtitlebar=this.getActivity().getActionBar();

		// abtitlebar.hide();
		initBasic();
		initView(view);
		setListener();
		return view;
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	GestureDetector.OnGestureListener gestureDetectorListenner = new GestureDetector.SimpleOnGestureListener() {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			float x = e2.getX() - e1.getX();
			Toast.makeText(getActivity(), "oooooooo", 0).show();
			if (x > 200) {
				getActivity().overridePendingTransition(R.anim.left_in,
						R.anim.right_out);

				// Animation anim = AnimationUtils.loadAnimation(getActivity(),
				// R.anim.left_out);
				// view.startAnimation(anim);
			} else if (x < -200) {
				// 动画
				getActivity().overridePendingTransition(R.anim.right_in,
						R.anim.left_out);
				// Animation anim = AnimationUtils.loadAnimation(getActivity(),
				// R.anim.right_out);
				// view.startAnimation(anim);
			}
			return true;
		}
	};

	public void initBasic() {

		// 如果里面的页面列表不能下载原因：
		// Fragment里面用的AbTaskQueue,由于有多个tab，顺序下载有延迟，还没下载好就被缓存了。改成用AbTaskPool，就ok了。
		// 或者setOffscreenPageLimit(0)

		// 缓存数量
		// mBottomTabView.getViewPager().setOffscreenPageLimit(5);

		// 禁止滑动
		/*
		 * mBottomTabView.getViewPager().setOnTouchListener(new
		 * OnTouchListener(){
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { return
		 * true; }
		 * 
		 * });
		 */

		// mBottomTabView.setOnPageChangeListener(listener);

		List<String> tabTexts = new ArrayList<String>();
		tabTexts.add("生活信息");
		tabTexts.add("教学信息");
		tabTexts.add("学工活动");
		tabTexts.add("社团活动");
		tabTexts.add("失物招领");

//		Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),
//				"fonts/lanting.TTF");
		// 设置样式
		mBottomTabView.setTabTextColor(Color.WHITE);
		mBottomTabView.setTabSelectColor(Color.rgb(255, 255, 255));
		mBottomTabView.setTabBackgroundResource(R.drawable.infotabs);
		mBottomTabView.setTabLayoutBackgroundResource(R.drawable.infotab);
        mBottomTabView.setTabTextSize(15);
//        mBottomTabView.setTabTextFont(typeface);
		mBottomTabView.addItemViews(tabTexts);

		mBottomTabView.setTabPadding(2, 10, 2, 2);
	}

	private XueGongBu[] page = new XueGongBu[5];

	private void initView(View view) {
		// TODO Auto-generated method stub
		page[0] = new XueGongBu(Constant.PAGE_ONE);
		page[1] = new XueGongBu(Constant.PAGE_TWO);
		page[2]= new XueGongBu(Constant.PAGE_THREE);
		page[3] = new XueGongBu(Constant.PAGE_FOUR);
		page[4] = new XueGongBu(Constant.PAGE_FIVE);
		ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
		mFragments.add(page[0]);
		mFragments.add(page[1]);
		mFragments.add(page[2]);
		mFragments.add(page[3]);
		mFragments.add(page[4]);
		// TODO Auto-generated method stub
		pager = (ViewPager) view.findViewById(R.id.GovernPv);
		adapter = new AbFragmentPagerAdapter(this.getChildFragmentManager(),
				mFragments);
		pager.setAdapter(adapter);

	}

	private ArrayList<AbTabItemView> tabViews;
	private boolean isScrolling = false;
	private int lastValue = -1;

	public void setListener() {
		tabViews = mBottomTabView.gettabview();
		for (int i = 0; i < tabViews.size(); i++) {
			tabViews.get(i).setOnClickListener(mTabClickListener);
		}

		if (pager != null) {
			pager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
					if (arg0 == 1) {
						isScrolling = true;
					} else {
						isScrolling = false;
					}
					mBottomTabView.setCurrentItem(arg0);

				}
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					if (isScrolling) {
						if (lastValue > arg2) {
							// 递减，向右侧滑动
							// right = true;
							// left = false;
						
//							Animation anim = AnimationUtils.loadAnimation(
//									page[arg0].getActivity(), R.anim.right_out);
//							page[arg0].getView().startAnimation(anim);
							// getActivity().overridePendingTransition(R.anim.left_in,
							// R.anim.right_out);
						
						} else if (lastValue < arg2) {
							// 递减，向右侧滑动
							// right = false;
							// left = true;
//							Animation anim = AnimationUtils.loadAnimation(
//									getActivity(), R.anim.left_out);
//							page[arg0].getView().startAnimation(anim);
							// getActivity().overridePendingTransition(R.anim.right_in,
							// R.anim.left_out);
						} else if (lastValue == arg2) {
							// right = left = false;
						}
					}
					// Log.i("meityitianViewPager",
					// "meityitianViewPager onPageScrolled  last :arg2  ,"
					// + lastValue + ":" + arg2);
					lastValue = arg2;
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub

				}
			});
		}
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println("-------------------goverment------------------");
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
