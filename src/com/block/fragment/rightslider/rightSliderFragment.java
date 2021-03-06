package com.block.fragment.rightslider;

import jhunplay.com.fanjie.tool.Msharepreference;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.block.jhguide.R;
import com.cx.second_deal.Sundries_deal_history;
import com.fanjie.activity.rightSlider.AboutUs;
import com.fanjie.activity.rightSlider.Login;
import com.fanjie.activity.rightSlider.MessageActivity;
import com.fanjie.activity.rightSlider.MyInformation;
import com.fanjie.activity.rightSlider.MyUpdate;
import com.fanjie.activity.rightSlider.SuggestionBack;
import com.fanjie.activity.rightSlider.lw_submit_myGoods;
import com.fanjie.activity.rightSlider.lw_submit_myGoods;;

public class rightSliderFragment extends Fragment {

	private ImageButton userInfoBtn;
	private ImageButton suggestBtn;
	private ImageButton aboutUsBtn;
	private ImageButton checkUpdateBtn;
	private ImageButton exitBtn;
	private ImageButton myMessageButton;
	
	private ImageButton myBusniessButton;
	
	private Msharepreference msharepreference;
	private Msharepreference msharepreference2;
	private OnClickListener clickListener;
	private View view;
	private MyUpdate myUp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyReceiver myReceiver = new MyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.qq.xgdemo.activity.UPDATE_LISTVIEW");
		getActivity().registerReceiver(myReceiver, intentFilter);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		msharepreference2 = new Msharepreference(getActivity(), "flag");
		view = inflater.inflate(R.layout.view_slider_menu, null);
		msharepreference = new Msharepreference(getActivity(), "information");
		initDate(view);
		initView(view);
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		findViewById(view);
		setOnclickListener();
	}

	private void findViewById(View view) {
		// TODO Auto-generated method stub
		userInfoBtn = (ImageButton) view.findViewById(R.id.right_menu_btn1);
		suggestBtn = (ImageButton) view.findViewById(R.id.right_menu_btn3);
		aboutUsBtn = (ImageButton) view.findViewById(R.id.right_menu_btn2);
		checkUpdateBtn = (ImageButton) view.findViewById(R.id.right_menu_btn4);
		exitBtn = (ImageButton) view.findViewById(R.id.right_menu_btn5);
		myMessageButton = (ImageButton) view.findViewById(R.id.myMessages);
		
		myBusniessButton=(ImageButton)view.findViewById(R.id.right_menu_btn6);
		
		boolean a = msharepreference2.getBooleanProperty("flag");
		if (msharepreference2.getBooleanProperty("flag")) {
			myMessageButton.setBackgroundResource(R.drawable.side_new_message_after);
		} else {
			myMessageButton
					.setBackgroundResource(R.drawable.view_slider_menu_mymessages);
		}

	}

	private void setOnclickListener() {
		// TODO Auto-generated method stub
		clickListener = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (arg0 == userInfoBtn) {
					Intent intent = new Intent();
					// User user = MyApplication.getCurrentUser(getActivity());
					String a = msharepreference.getStringProperty("sessionkey");
					if (msharepreference.getStringProperty("sessionkey")
							.equals("")) {
						intent.setClass(getActivity(), Login.class);
						getActivity().startActivity(intent);
					} else {
						intent.setClass(getActivity(), MyInformation.class);
						getActivity().startActivity(intent);
					}
					// getActivity().overridePendingTransition(R.anim.unzoom_in,
					// R.anim.unzoom_out);
					getActivity().overridePendingTransition(
							R.anim.flip_horizontal_in,
							R.anim.flip_horizontal_out);
				} /*
				 * else { intent.setClass(getActivity(), MyInformation.class);
				 * getActivity().startActivity(intent);
				 */

				// }

				else if (arg0 == suggestBtn) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), SuggestionBack.class);
					getActivity().startActivity(intent);
					getActivity().overridePendingTransition(R.anim.appear_in,
							R.anim.appear_out);
				} else if (arg0 == aboutUsBtn) {

					Intent intent = new Intent();
					intent.setClass(getActivity(), AboutUs.class);
					getActivity().startActivity(intent);
					getActivity().overridePendingTransition(
							R.anim.in_translate_bottom,
							R.anim.out_translate_top);

				} else if (arg0 == checkUpdateBtn) {
					myUp = new MyUpdate(getActivity());

				} else if (arg0 == myMessageButton) {
					Intent it = new Intent(getActivity(), MessageActivity.class);
					getActivity().startActivity(it);
					myMessageButton
							.setBackgroundResource(R.drawable.view_slider_menu_mymessages);
					msharepreference2.setBooleanProperty("flag", false);
				}
				else if(arg0 ==myBusniessButton){
					
					
					Intent intent = new Intent();
					if (msharepreference.getStringProperty("sessionkey")
							.equals("")) {
						Toast.makeText(getActivity(), "您还未登陆！请前往登陆", 0).show();
						intent.setClass(getActivity(), Login.class);
						
						getActivity().startActivity(intent);
					}else{
						
						intent.setClass(getActivity(), Sundries_deal_history.class);
						getActivity().startActivity(intent);
					}
					getActivity().overridePendingTransition(R.anim.appear_in,
							R.anim.appear_out);
				
				}
				
				else {
					showExit();
				}
			}
		};
		userInfoBtn.setOnClickListener(clickListener);
		suggestBtn.setOnClickListener(clickListener);
		aboutUsBtn.setOnClickListener(clickListener);
		checkUpdateBtn.setOnClickListener(clickListener);
		exitBtn.setOnClickListener(clickListener);
		myMessageButton.setOnClickListener(clickListener);
		
		myBusniessButton.setOnClickListener(clickListener);
	}

	private AlertDialog log;

	public void showExit() {
		// TODO Auto-generated method stub
		log = new AlertDialog.Builder(getActivity()).create();
		log.show();
		Window window = log.getWindow();
		log.setCanceledOnTouchOutside(false);
		window.setContentView(R.layout.exit_dialog_lw);
		View view = window.findViewById(R.id.finish);
		Animation anim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.in_translate_right);
		view.startAnimation(anim);
		Button ok = (Button) window.findViewById(R.id.exit_ok);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				getActivity().finish();
				getActivity().overridePendingTransition(
						R.anim.flip_vertical_in, R.anim.flip_vertical_out);

			}
		});

		final Button no = (Button) window.findViewById(R.id.exit_no);
		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				log.cancel();
			}
		});
	}

	private void initDate(View view) {
		// TODO Auto-generated method stub

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		try {
			log.dismiss();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onDestroy();
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			myMessageButton.setBackgroundResource(R.drawable.new_message);
		}

	}

}
