package jhunplay.com.block.act;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.ab.activity.AbActivity;
import com.ab.view.sliding.AbBottomTabView;
import com.ab.view.sliding.AbSlidingPlayView;
import com.ab.view.sliding.AbSlidingTabView;
import com.ab.view.slidingmenu.SlidingMenu;
import com.ab.view.titlebar.AbTitleBar;
import com.block.jhguide.R;

public class BlockActivity extends AbActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// initDefaultTopBar();
	}

	/*
	 * 配置信息，activity需要配置的控件
	 */
	private Boolean isContainLeftSlider = false;
	private Boolean isContainRightSlider = false;
	private Boolean isContainTabs = false;
	private Boolean isShowTopBar = false;
//	private Boolean isShowSliderImage = false;
	/*
	 * 控件信息，activity所需要的控件对象
	 */
	AbTitleBar titleBar;
	BlockSlidingMenu blockSlidingMenu;
	BlockTabs blockTabs;
	AbSlidingPlayView slidingImages;
	private List<View> topbarRightViews = null;

	public class BlockSlidingMenu {

		SlidingMenu leftmenu, rightMenu;
		Boolean isLeftShow, isRightShow;

		public BlockSlidingMenu() {

			leftmenu = null;
			rightMenu = null;
			isLeftShow = false;
			isRightShow = false;

		}

		public void initLeftMenu() {
			leftmenu = new SlidingMenu(getApplicationContext());
			leftmenu.setMode(SlidingMenu.LEFT);
			leftmenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			leftmenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			leftmenu.setShadowWidthRes(R.dimen.shadow_width);
			leftmenu.setShadowDrawable(R.drawable.shadow);
			leftmenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
			leftmenu.setFadeDegree(0.35f);
			leftmenu.attachToActivity(BlockActivity.this,
					SlidingMenu.SLIDING_CONTENT);
		}

		public void setSlidingMenuScrollEnable(Boolean bool) {
			if (!bool) {
				if (leftmenu == null) {
					leftmenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
				}
				if (rightMenu == null) {
					rightMenu
							.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
				}
			} else {
				if (leftmenu != null) {
					leftmenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
				}
				if (rightMenu != null) {
					rightMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
				}
			}
		}

		public SlidingMenu getLeftmenu() {
			return leftmenu;
		}

		public SlidingMenu getRightMenu() {
			return rightMenu;
		}

		public Boolean getIsLeftShow() {
			return isLeftShow;
		}

		public Boolean getIsRightShow() {
			return isRightShow;
		}

		public void setLeftmenu(SlidingMenu leftmenu) {
			this.leftmenu = leftmenu;
		}

		public void setRightMenu(SlidingMenu rightMenu) {
			this.rightMenu = rightMenu;
		}

		public void setIsLeftShow(Boolean isLeftShow) {
			this.isLeftShow = isLeftShow;
		}

		public void setIsRightShow(Boolean isRightShow) {
			this.isRightShow = isRightShow;
		}

	}

	public class BlockTabs {

		Boolean isBottomTab;
		private AbBottomTabView bottomTabView;
		private List<Drawable> tabDrawables;
		private List<Fragment> mFragments;
		private AbSlidingTabView topTabView;
		private List<String> tabTexts;

		public BlockTabs() {

			isBottomTab = false;
			bottomTabView = null;
			tabDrawables = null;
			topTabView = null;
			tabTexts = null;

		}

		public void showBottomTabs() {
			if (!isBottomTab) {
				return;
			}
			// 设置样式
			bottomTabView.setTabTextColor(Color.BLACK);
			bottomTabView.setTabSelectColor(Color.rgb(231, 179, 37));
			// bottomTabView.setTabBackgroundResource(R.drawable.tab_bg2);
			bottomTabView
					.setTabLayoutBackgroundResource(R.drawable.tablayout_bg2);
			bottomTabView.getViewPager().setOffscreenPageLimit(5);
			bottomTabView.addItemViews(tabTexts, mFragments, tabDrawables);
			bottomTabView.setTabPadding(2, 0, 2, 2);
		}

//		public void showTopTabs() {
//			topTabView.getViewPager().setOffscreenPageLimit(5);
//			topTabView.setTabTextColor(Color.BLACK);
//			topTabView.setTabSelectColor(Color.rgb(30, 168, 131));
//			topTabView.setTabBackgroundResource(R.drawable.tab_bg);
//			topTabView.setTabLayoutBackgroundResource(R.drawable.slide_top);
//			topTabView.addItemViews(tabTexts, mFragments);
//			topTabView.setTabPadding(20, 8, 20, 8);
//		}

		public Boolean getIsBottomTab() {
			return isBottomTab;
		}

		public List<Fragment> getmFragments() {
			return mFragments;
		}

		public void setmFragments(List<Fragment> mFragments) {
			this.mFragments = mFragments;
		}

		public void setIsBottomTab(Boolean isBottomTab) {
			this.isBottomTab = isBottomTab;
		}

		public AbBottomTabView getBottomTabView() {
			return bottomTabView;
		}

		public void setBottomTabView(AbBottomTabView bottomTabView) {
			this.bottomTabView = bottomTabView;
		}

		public List<Drawable> getTabDrawables() {
			return tabDrawables;
		}

		public void setTabDrawables(List<Drawable> tabDrawables) {
			this.tabDrawables = tabDrawables;
		}

		public AbSlidingTabView getTopTabView() {
			return topTabView;
		}

		public void setTopTabView(AbSlidingTabView mAbSlidingTabView) {
			this.topTabView = mAbSlidingTabView;
		}

		public List<String> getTabTexts() {
			return tabTexts;
		}

		public void setTabTexts(List<String> tabTexts) {
			this.tabTexts = tabTexts;
		}

	}

	/*
	 * 外部调用 函数
	 */
	public void initTopBar(String title) {
		isShowTopBar = true;
		titleBar = this.getTitleBar();
		titleBar.setTitleText(title);
		titleBar.setTitleBarBackground(R.drawable.tab_13);
		titleBar.setTitleBarGravity(Gravity.CENTER_HORIZONTAL,
				Gravity.CENTER_HORIZONTAL);
		titleBar.setTitleTextMargin(5, 0, 0, 0);
	}

	// public void removeBar(){
	// titleBar.r
	// }
	public void initDefaultTopBar() {
		isShowTopBar = true;
		titleBar = this.getTitleBar();
		titleBar.setTitleText(R.string.app_name);
		titleBar.setTitleBarBackground(R.drawable.top_bg);
		titleBar.setTitleTextMargin(5, 0, 0, 0);
	}

	public void setLeftSliderView(Fragment view) {
		isContainLeftSlider = true;
		if (blockSlidingMenu == null) {
			blockSlidingMenu = new BlockSlidingMenu();
		}
		blockSlidingMenu.setIsLeftShow(true);
		initDefaultLeftSlider(view);
	}

	public void setleftSlider(SlidingMenu menu) {
		isContainLeftSlider = true;
		if (menu != null) {
			blockSlidingMenu.setLeftmenu(menu);
		} else {

		}
	}

	public void setRightSliderView(Fragment view) {
		isContainRightSlider = true;
		if (blockSlidingMenu == null) {
			blockSlidingMenu = new BlockSlidingMenu();
		}
		blockSlidingMenu.setIsRightShow(true);
		initDefaultRightSlider(view);
	}

	public void setRightSlider(SlidingMenu menu) {
		isContainRightSlider = true;
		if (menu != null) {
			blockSlidingMenu.setRightMenu(menu);
		} else {

		}
	}

	public void setBottomTabsView() {
		isContainTabs = true;
		blockTabs = new BlockTabs();
		blockTabs.setIsBottomTab(true);

	}

	public void setBottomTabs(List<Fragment> mFragments, List<String> tabTexts,
			ArrayList<Drawable> tabDrawables, int id) {
		AbBottomTabView mBottomTabView = (AbBottomTabView) findViewById(id);
		isContainTabs = true;
		blockTabs = new BlockTabs();
		blockTabs.setIsBottomTab(true);
		// 设置tabs
		blockTabs.setBottomTabView(mBottomTabView);
		// 设置content页面
		blockTabs.setmFragments(mFragments);
		// 设置tabs图片
		blockTabs.setTabDrawables(tabDrawables);
		// 设置tabs标签
		blockTabs.setTabTexts(tabTexts);

		blockTabs.showBottomTabs();

		if (blockSlidingMenu != null) {

		}
	}

	public void setTopTabs() {

	}

//	public void setTopTabsView(List<Fragment> mFragments,
//			List<String> tabTexts, int id) {
//		isContainTabs = true;
//		blockTabs = new BlockTabs();
//		blockTabs.setIsBottomTab(false);
//		AbSlidingTabView mAbSlidingTabView;
//		mAbSlidingTabView = (AbSlidingTabView) findViewById(id);
//		blockTabs.setTopTabView(mAbSlidingTabView);
//		blockTabs.setmFragments(mFragments);
//		blockTabs.setTabTexts(tabTexts);
//		blockTabs.showTopTabs();
//	}

	public void setSliderScrollerEnable(boolean bool) {
		if (blockSlidingMenu != null) {
			blockSlidingMenu.setSlidingMenuScrollEnable(bool);
		}
	}

	// public void setSliderShowImage(List<View>imgViews , int id){
	// slidingImages = (AbSlidingPlayView)findViewById(R.id.mAbSlidingPlayView)
	// ;
	// slidingImages.setNavHorizontalGravity(Gravity.RIGHT);
	// if (imgViews == null) {
	// System.err.println("imageview 列表为空");
	// }else {
	// for (int i = 0; i < imgViews.size(); i++) {
	// slidingImages.addView(imgViews.get(i));
	// }
	// }
	// }
	/*
	 * 私有成员
	 */
	private void initDefaultLeftSlider(Fragment view) {
		if (titleBar == null) {
			initDefaultTopBar();
		}
		titleBar.setLogo(R.drawable.button_select_menu_block);
		titleBar.setLogoLine(R.drawable.line);

		blockSlidingMenu.setLeftmenu(initDefaultSlidingMenu(SlidingMenu.LEFT,
				view));
		titleBar.getLogoView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (blockSlidingMenu.getLeftmenu().isMenuShowing()) {
					blockSlidingMenu.getLeftmenu().showContent();
				} else {
					blockSlidingMenu.getLeftmenu().showMenu();
					blockSlidingMenu.getRightMenu().setScrollContainer(false);
				}
			}
		});
	}

	private void initDefaultRightSlider(Fragment view) {
		if (titleBar == null) {
			initDefaultTopBar();
		}
		titleBar.clearRightView();
		View rightViewApp = mInflater.inflate(R.layout.app_btn, null);
		titleBar.addRightView(rightViewApp);
		if (topbarRightViews == null) {
			topbarRightViews = new ArrayList<View>();
		}
		topbarRightViews.add(rightViewApp);
		ImageButton appBtn = (ImageButton) rightViewApp.findViewById(R.id.appBtn);
		appBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (blockSlidingMenu.getRightMenu().isMenuShowing()) {
					blockSlidingMenu.getRightMenu().showContent();
				} else {
					blockSlidingMenu.getRightMenu().showMenu();
				}
			}
		});

		blockSlidingMenu.setRightMenu(initDefaultSlidingMenu(SlidingMenu.RIGHT,
				view));
	}

	private SlidingMenu initDefaultSlidingMenu(int type, Fragment view) {
		SlidingMenu menu;
		menu = new SlidingMenu(this);
		menu.setMode(type);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.sliding_menu_menu);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, view).commit();
		return menu;
	}

	/*
	 * Getter And Setter
	 */
	public List<View> getTopbarRightViews() {
		return topbarRightViews;
	}

	public void setTopbarRightViews(List<View> topbarRightViews) {
		this.topbarRightViews = topbarRightViews;
	}

	public Boolean getIsContainLeftSlider() {
		return isContainLeftSlider;
	}

	public Boolean getIsContainRightSlider() {
		return isContainRightSlider;
	}

	public Boolean getIsContainTabs() {
		return isContainTabs;
	}

	public Boolean getIsShowTopBar() {
		return isShowTopBar;
	}

}
