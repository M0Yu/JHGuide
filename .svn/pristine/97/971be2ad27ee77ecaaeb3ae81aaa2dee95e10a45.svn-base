package com.block.activity.main;

import com.block.jhguide.R;

import jhunpaly.com.zmj.network.judge.NetWorkJudgeTool;
import jhunplay.com.fanjie.tool.Tool;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public class NetWorkJudge extends Activity {
	private NetWorkJudgeTool netWorkJudge;
	private Tool tool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.judgenetwork);

		netWorkJudge = new NetWorkJudgeTool();
		tool = new Tool(NetWorkJudge.this);
		if (netWorkJudge.isNetWorkConnected(NetWorkJudge.this)) {

			if (!netWorkJudge.isMobileConnected(NetWorkJudge.this)
					&& !netWorkJudge.isWifiConnected(NetWorkJudge.this)) {

				Toast.makeText(NetWorkJudge.this, "当前网络连接不可用",
						Toast.LENGTH_SHORT);
				NetWorkJudge.this.finish();
				
			}

			else {
				
				Intent intent = new Intent(NetWorkJudge.this,WelcomeActivity.class);
				NetWorkJudge.this.startActivity(intent);
				NetWorkJudge.this.finish();
				
			}
		}

		else {

			tool.showNetDialog("未连接网络", "是否连接网络");
			

		}
		
	}
	
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (tool.haveShowDialog()) {
			tool.removeDialog();
		}
	}

}
