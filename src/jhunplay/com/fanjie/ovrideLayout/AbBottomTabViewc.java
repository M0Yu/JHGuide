package jhunplay.com.fanjie.ovrideLayout;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.ab.view.sliding.AbTabItemView;
import com.block.fragment.content.Government;

public class AbBottomTabViewc extends LinearLayout{
	
	private int phoneHeight=0;
	private int phoneWidth=0;
	public AbBottomTabViewc(Context context, AttributeSet attrs) {
		super(context, attrs);
        this.context = context;
        
        this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.rgb(255, 255, 255));
		// TODO Auto-generated constructor stub
		WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        phoneWidth = wm.getDefaultDisplay().getWidth();
        phoneHeight = wm.getDefaultDisplay().getHeight();
		layoutParamsFW = new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                (int) (phoneHeight * 0.08f + 0.5f));
		//layoutParamsFW = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 37);
		
		mTabLayout = new LinearLayout(context);
		mTabLayout.setOrientation(LinearLayout.HORIZONTAL);
		mTabLayout.setGravity(Gravity.CENTER);
		
		addView(mTabLayout, layoutParamsFW);
		
		//定义Tab栏
  		tabItemList = new ArrayList<TextView>();
  		tabItemTextList = new ArrayList<String>();
	}



	/** tab的线性布局. */
	private LinearLayout mTabLayout = null;
	/** tab的列表. */
	private ArrayList<TextView> tabItemList = null;
	/** tab的文字. */
	private List<String> tabItemTextList = null;
	/** 当前选中编号. */
	private int mSelectedTabIndex = 0;
	/** The context. */
	private Context context;
	/** tab的文字大小. */
	private int tabTextSize = 17;
	
	/**tab的文字字体*/
//	private int tabTextFont=  1;            //增加的字体
	
	private Typeface tabTextFont;
	
	/** tab的文字颜色. */
	private int tabTextColor = Color.BLACK;
	/** tab的选中文字颜色. */
	private int tabSelectColor = Color.WHITE;
	/** tab的背景. */
    private int tabBackgroundResource = -1;
    /** The layout params fw. */
	public LinearLayout.LayoutParams layoutParamsFW = null;
	
	
	
	private Government government=new Government();
	private ArrayList<AbTabItemView> tabViews=new ArrayList<AbTabItemView>();
	
	
    /** The m tab click listener. */
	private OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
        	AbTabItemView tabView = (AbTabItemView)view;
        	mSelectedTabIndex=tabView.getIndex();
        
        	
            setCurrentItem(mSelectedTabIndex);
        }
    };
	
	   /**
     * 描述：创造一个Tab.
     *
     * @param text the text
     * @param index the index
     */
    private void addTab(String text, int index) {
    	addTab(text,index,null);
    }
    
    /**
     * 描述：创造一个Tab.
     *
     * @param text the text
     * @param index the index
     * @param top the top
     */
    private void addTab(String text, int index,Drawable top) {
    	AbTabItemView tabView = new AbTabItemView(this.context);
       
        if(top!=null){
        	tabView.setTabCompoundDrawables(null, top, null, null);
        }
    	tabView.setTabTextColor(tabTextColor);
    	tabView.setTabTextSize(tabTextSize);
        tabView.setTabTextSize(tabTextSize);
        tabView.getTextView().setTypeface(tabTextFont);
        tabView.init(index,text);
        tabItemList.add(tabView.getTextView());
        tabView.setOnClickListener(mTabClickListener);
        tabViews.add(tabView);
        mTabLayout.addView(tabView, new LayoutParams(0,LayoutParams.FILL_PARENT,1));
    }
	
	/**
     * 描述：tab有变化刷新.
     */
    public void notifyTabDataSetChanged() {    	
        mTabLayout.removeAllViews();
        tabItemList.clear();
        
        for(int i=0;i<tabItemTextList.size();i++)
        {
        	addTab(tabItemTextList.get(i), i);
        }
       
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }
	
	/**
	 * 描述:增加一个类容与tab.
	 * @params tabText the text
	 */
	public void addItemViews(List<String>tabTexts){
		tabItemTextList.addAll(tabTexts);
		//mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	
	
	/**
	 * 描述：设置显示哪一个.
	 *
	 * @param index the new current item
	 */
    public void setCurrentItem(int index) {
       /* if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }*/
        mSelectedTabIndex = index;
        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final AbTabItemView child = (AbTabItemView)mTabLayout.getChildAt(i);
            final boolean isSelected = (i == index);
            child.setSelected(isSelected);
            if (isSelected) {
            	child.setTabTextColor(tabSelectColor);
            	if(tabBackgroundResource!=-1){
            		 child.setTabBackgroundResource(tabBackgroundResource);
                }
            	/*if(tabItemDrawableList.size() >= tabCount*2){
             		 child.setTabCompoundDrawables(null, tabItemDrawableList.get(index*2+1), null, null);
             	}else if(tabItemDrawableList.size() >= tabCount){
    			     child.setTabCompoundDrawables(null, tabItemDrawableList.get(index), null, null);
    		    }
            	mViewPager.setCurrentItem(index);*/
            	
            }else{
            	if(tabBackgroundResource!=-1){
           		   child.setBackgroundDrawable(null);
                }
            	/*if(tabItemDrawableList.size() >= tabCount*2){
            		child.setTabCompoundDrawables(null, tabItemDrawableList.get(i*2), null, null);*/
            	}
            	child.setTabTextColor(tabTextColor);
            }
        }
    /**
	 * 描述：设置单个tab的背景选择器.
	 *
	 * @param resid the new tab background resource
	 */
	public void setTabBackgroundResource(int resid) {
    	tabBackgroundResource = resid;
    }
	
	/**
	 * 描述：设置Tab的背景.
	 *
	 * @param resid the new tab layout background resource
	 */
	public void setTabLayoutBackgroundResource(int resid) {
		this.mTabLayout.setBackgroundResource(resid);
	}

	/**
	 * 描述：设置选中的颜色.
	 *
	 * @param tabColor the new tab select color
	 */
	public void setTabSelectColor(int tabColor) {
		this.tabSelectColor = tabColor;
	}
	
	/**
	 * 描述：设置tab文字的颜色.
	 *
	 * @param tabColor the new tab text color
	 */
	public void setTabTextColor(int tabColor) {
		this.tabTextColor = tabColor;
	}
	
	/*
	 * 描述:设置tab文字的大小.
	 * 
	 *  @param tabTextSize the new tab text size
	 */
	public void setTabTextSize(int tabTextSize){
		this.tabTextSize=tabTextSize;
	}
	/*
	 * 描述:设置tab文字的字体.
	 * 
	 * @param tabTextFont the new tab text font
	 */
	public void setTabTextFont(Typeface tabTextFont){
		this.tabTextFont=tabTextFont;
	}
	/**
	 * 描述：设置每个tab的边距.
	 *
	 * @param left the left
	 * @param top the top
	 * @param right the right
	 * @param bottom the bottom
	 */
	public void setTabPadding(int left, int top, int right, int bottom) {
		for(int i = 0;i<tabItemList.size();i++){
			TextView tv = tabItemList.get(i);
			tv.setPadding(left, top, right, bottom);
		}
	}
	
	public int getCurrentItem(){
		return mSelectedTabIndex;
	}
     public ArrayList<AbTabItemView> gettabview(){
    	 return tabViews;
     }
}


