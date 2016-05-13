package com.block.activity.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jhunplay.com.block.act.BlockActivity;
import jhunplay.com.fanjie.ovrideLayout.MyImageView.OnImageClick;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.view.sliding.AbBottomTabView;
import com.ab.view.sliding.callBackindex;
import com.block.activity.play.HaveFunListActivity;
import com.block.fragment.content.Government;
import com.block.fragment.content.JhSpace;
import com.block.fragment.content.Map;
import com.block.fragment.content.MoreInfo;
import com.block.fragment.content.PlayWithLife;
import com.block.fragment.content.PlayWithLife.OnPlayWithLifeItemClickListener;
import com.block.fragment.content.CommunityNews;
import com.block.fragment.rightslider.rightSliderFragment;
import com.block.jhguide.R;
import com.huangbolun.sershou.waterex.ExchangeFragment;
import com.wudi.comment.activity.StoreDetailActivity;

public class MainActivity extends BlockActivity implements callBackindex,
		OnImageClick {

	// private ViewPager viewpager;
	// private ArrayList<Fragment> fragmentlist;
	// private LinearLayout layout;
	// private int[] image = { R.id.imageview1, R.id.imageview2,
	// R.id.imageview3,
	// R.id.imageview4, R.id.imageview5 };
	// private int[] text = { R.id.text1, R.id.text2, R.id.text3, R.id.text4,
	// R.id.text5 };
	// private int[] drawa = { R.drawable.tab_life_after_21,
	// R.drawable.tab_map_after_22, R.drawable.tab_news_after_22,
	// R.drawable.tab_picture_after_22, R.drawable.tab_more_after_22 };
	// private int[] drawb = { R.drawable.tab_life_before_21,
	// R.drawable.tab_map_before_22, R.drawable.tab_news_before_22,
	// R.drawable.tab_picture_before_22, R.drawable.tab_more_before_22 };
	// private LinearLayout[] tv = new LinearLayout[5];
	// private int currIndex;
	// private static PlayWithLife page1 = new PlayWithLife();
	// private static Map page2 = new Map();
	// private static Government page3 = new Government();
	// private static ThoseYear page4 = new ThoseYear();
	// private static MoreInfo page5 = new MoreInfo();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		AbBottomTabView tabView = new AbBottomTabView(this);
		setAbContentView(R.layout.view_tab_bottom);// mylayout

		// frameLayout = (FrameLayout) this.findViewById(R.id.content_frame);
		// 设置右侧菜单
		setRightSliderView(new rightSliderFragment());
		// 添加底部tabs 和contentview
		addTab();
	}

	private String[] title = { "玩转生活", "江大地图", "校园信息", "江大空间", "更多" };

	private void addTab() {
		PlayWithLife page1 = new PlayWithLife();
		Map page2 = new Map();
		Government page3 = new Government();
		// CommunityNews page4 = new CommunityNews();
		// ExchangeFragment page4 = new ExchangeFragment();
		JhSpace page4 = new JhSpace();
		// ThoseYear page4 = new ThoseYear();
		MoreInfo page5 = new MoreInfo();

		page1.setOnPlayWithLifeClickListener(new OnPlayWithLifeItemClickListener() {

			@Override
			public void onclick(int type, int position,
					final ArrayList<HashMap<String, Object>> inforlist) {
				// TODO Auto-generated method stub
				if (type == 1) {

					Intent intent = new Intent(MainActivity.this,
							HaveFunListActivity.class);
					Bundle mybundle = new Bundle();
					mybundle.putInt("position", position);
					intent.putExtra("style", mybundle);

					MainActivity.this.startActivity(intent);
					overridePendingTransition(R.anim.left_in, R.anim.left_out);
				} else {

					Intent intent = new Intent(MainActivity.this,
							StoreDetailActivity.class);
					Bundle bundle = new Bundle();
					ArrayList list = new ArrayList();// 这个list用于在budnle中传递
														// 需要传递的ArrayList<Object>
					list.add(inforlist);
					bundle.putParcelableArrayList("listinfo", list);
					intent.putExtra("bundlelist", bundle);
					MainActivity.this.startActivity(intent);
					overridePendingTransition(R.anim.right_in, R.anim.right_out);
				}
				// MainActivity.this.overridePendingTransition(android.R.anim.fade_in,
				// android.R.anim.fade_in);
			}
		});
		List<Fragment> mFragments = new ArrayList<Fragment>();
		mFragments.add(page1);
		mFragments.add(page2);
		mFragments.add(page3);
		mFragments.add(page4);
		mFragments.add(page5);

		List<String> tabTexts = new ArrayList<String>();
		tabTexts.add("玩转生活");
		tabTexts.add("江大地图");
		tabTexts.add("校园信息");
		tabTexts.add("江大空间");
		tabTexts.add("更多");

		ArrayList<Drawable> tabDrawables = new ArrayList<Drawable>();
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.tab_life_before_21));
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.tab_life_after_21));
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.tab_map_before_22));
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.tab_map_after_22));
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.tab_news_before_22));
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.tab_news_after_22));
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.tab_picture_before_22));
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.tab_picture_after_22));
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.tab_more_before_22));
		tabDrawables.add(this.getResources().getDrawable(
				R.drawable.tab_more_after_22));

		setBottomTabs(mFragments, tabTexts, tabDrawables, R.id.mBottomTabView);
		// setTopTabsView(mFragments, tabTexts, R.id.mAbSlidingTabView) ;
		// tv[0] = (LinearLayout) this.findViewById(R.id.layut1);
		// tv[1] = (LinearLayout) this.findViewById(R.id.layut2);
		// tv[2] = (LinearLayout) this.findViewById(R.id.layut3);
		// tv[3] = (LinearLayout) this.findViewById(R.id.layut4);
		// tv[4] = (LinearLayout) this.findViewById(R.id.layut5);
		// layout = (LinearLayout) this.findViewById(R.id.mylayout);
		// tv[0].setOnClickListener(new txListener(0));
		// tv[1].setOnClickListener(new txListener(1));
		// tv[2].setOnClickListener(new txListener(2));
		// tv[3].setOnClickListener(new txListener(3));
		// tv[4].setOnClickListener(new txListener(4));
		// viewpager = (ViewPager) findViewById(R.id.viewpager);
		// // fragmentlist = new ArrayList<Fragment>();
		// fragmentlist.add(page1);
		// fragmentlist.add(page2);
		// fragmentlist.add(page3);
		// fragmentlist.add(page4);
		// fragmentlist.add(page5);
		// viewpager.setAdapter(new MyFragmentPagerAdapter(
		// getSupportFragmentManager()));
		// setCurrentItem(0);// 设置当前显示标签页为第一页
		// viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	// public class txListener implements OnClickListener {
	//
	// private int index = 0;
	//
	// public txListener(int i) {
	// index = i;
	// }
	//
	// @Override
	// public void onClick(View v) {
	// // viewpager.setCurrentItem(index);
	// // tv[index].setTextColor(Color.RED);
	// setCurrentItem(index);
	// }
	//
	// }

	// public class MyOnPageChangeListener implements OnPageChangeListener {
	//
	// @Override
	// public void onPageScrollStateChanged(int arg0) {
	//
	// }
	//
	// @Override
	// public void onPageScrolled(int arg0, float arg1, int arg2) {
	//
	// }
	//
	// @Override
	// public void onPageSelected(int arg0) {
	// // Animation animation = new TranslateAnimation(currIndex * one,
	// // arg0
	// // * one, 0, 0);
	// setCurrentItem(arg0);
	// currIndex = arg0;
	// // animation.setFillAfter(true);
	// // animation.setDuration(200);
	// // img.startAnimation(animation);
	// // int i = currIndex + 1;
	// // Toast.makeText(MainActivity.this, "您选择了第" + i + "个页卡",
	// // Toast.LENGTH_SHORT).show();
	// }
	// }

	// private void setCurrentItem(int index) {
	// initTopBar(title[index]);
	// viewpager.setCurrentItem(index);
	// int count = layout.getChildCount();
	// for (int i = 0; i < count; i++) {
	// if (i == index) {
	// ((ImageView) findViewById(image[i]))
	// .setImageDrawable(getResources().getDrawable(drawa[i]));
	// ((TextView) findViewById(text[i])).setTextColor(Color.rgb(255,
	// 168, 51));
	// } else {
	// // tv[i].setTextColor(Color.BLACK);
	// ((ImageView) findViewById(image[i]))
	// .setImageDrawable(getResources().getDrawable(drawb[i]));
	// ((TextView) findViewById(text[i])).setTextColor(Color.BLACK);
	// }
	// }
	// }

	// 连续点击两次返回键退出程序

	public long lastTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getEventTime() - lastTime > 2000) {
				Toast.makeText(MainActivity.this, "再点击一次退出程序",
						Toast.LENGTH_SHORT).show();
			} else {
				finish();

				overridePendingTransition(R.anim.flip_vertical_in,
						R.anim.flip_vertical_out);
			}
			lastTime = event.getEventTime();
			return false;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void set_Head_Title(int arg0) {
		// TODO Auto-generated method stub
		initTopBar(title[arg0]);
	}

	@Override
	public void onImagClick(Intent intent) {
		// TODO Auto-generated method stub
		startActivity(intent);
		overridePendingTransition(R.anim.appear_bottom_right_in,
				R.anim.disappear_top_left_out);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
		System.gc();
	}

}
