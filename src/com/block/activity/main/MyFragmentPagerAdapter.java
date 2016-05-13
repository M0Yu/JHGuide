package com.block.activity.main;

import java.util.ArrayList;

import com.block.fragment.content.Government;
import com.block.fragment.content.JhSpace;
import com.block.fragment.content.Map;
import com.block.fragment.content.MoreInfo;
import com.block.fragment.content.PlayWithLife;
import com.block.fragment.content.CommunityNews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import android.widget.Toast;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	// ArrayList<Fragment> list;
	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
		// this.list = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		Fragment fragment = null;
		switch (arg0) {
		case 0:
			fragment = new PlayWithLife();
			break;
		case 1:
			fragment = new Map();
			break;
		case 2:
			fragment = new Government();
			break;
		case 3:
			fragment = new JhSpace();
			break;
		case 4:
			fragment = new MoreInfo();
			break;

		default:
			break;
		}
		return fragment;
		// fragment[0] = new PlayWithLife();
		// fragment[1] = new Map();
		// fragment[2] = new Government();
		// fragment[3] = new ThoseYear();
		// return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		
		return super.instantiateItem(container, position);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		System.out.println("----------------"+position);
		super.destroyItem(container, position, object);
	}

	
}
