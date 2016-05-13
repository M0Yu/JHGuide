package com.block.fragment.content;

import jhunplay.com.fanjie.ovrideLayout.MyImageView;
import jhunplay.com.fanjie.ovrideLayout.MyImageView.OnImageClick;
import jhunplay.com.fanjie.ovrideLayout.MyScrollView;

import com.block.jhguide.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class CommunityNews extends Fragment {

	private MyImageView imageView;
	private MyScrollView scrollView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.view_photo, null);
		initDate(view);
		initView(view);
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		if (imageView == null) {
			imageView = new MyImageView(getActivity());
			scrollView = (MyScrollView) view.findViewById(R.id.my_scroll_view);
		}
	}

	private void initDate(View view) {
		// TODO Auto-generated method stub

	}

	// @Override
	// public void onResume() {
	// // TODO Auto-generated method stub
	// super.onResume();
	// imageView.onReturn();
	// }

	// @Override
	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // TODO Auto-generated method stub
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// imageView.onReturn();
	//
	// }

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		scrollView.onReturn();
	}








	// @Override
	// public void onImagClick(Intent intent) {
	// // TODO Auto-generated method stub
	// startActivity(intent);
	// getActivity().overridePendingTransition(
	// R.anim.appear_bottom_right_in,
	// R.anim.disappear_top_left_out);
	// }

}
