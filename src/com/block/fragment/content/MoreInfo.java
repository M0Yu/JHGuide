package com.block.fragment.content;

import com.block.jhguide.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MoreInfo extends Fragment {
    
	private ImageButton myLibraryBtn,myTimeTableBtn,myComingBtn;
	private OnClickListener clickListener;
	private MyLibrary mMyLibrary;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.view_tabcontent_more, null);
		initDate(view);
		initView(view);
		return view ;
	}
     
	
	
	
	
	private void initView(View view) {
		// TODO Auto-generated method stub
		findViewById(view);
		setOnclickListener();
	}
	
	
	private void findViewById(View view) {
		// TODO Auto-generated method stub
		myLibraryBtn = (ImageButton) view.findViewById(R.id.myLibrary);
		myTimeTableBtn=(ImageButton) view.findViewById(R.id.myClassTable);
		myComingBtn = (ImageButton) view.findViewById(R.id.myComing);
	}


	private void setOnclickListener() {
		// TODO Auto-generated method stub
		clickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v==myLibraryBtn ){
					mMyLibrary = new MyLibrary(getActivity(),"com.example.liberary");
				}else if(v==myTimeTableBtn){
					mMyLibrary = new MyLibrary(getActivity(), "com.example.simpleclasstable");
				}else {
					
				}
				
			}
		};
		myLibraryBtn.setOnClickListener(clickListener);
		myTimeTableBtn.setOnClickListener(clickListener);
	}




	private void initDate(View view) {
		// TODO Auto-generated method stub
		
	}

	
}
