package com.wudi.comment.activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/***
 * 自定义ListView子类，继承ListView
 * @author Administrator
 *
 */
public class ListViewNoScroll extends ListView {

	public ListViewNoScroll(Context context) {
		super(context);
	}

	public ListViewNoScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewNoScroll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}