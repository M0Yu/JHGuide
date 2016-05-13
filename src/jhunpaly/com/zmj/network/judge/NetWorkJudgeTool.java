package jhunpaly.com.zmj.network.judge;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkJudgeTool {

	// 判断是否有网络连接
	public boolean isNetWorkConnected(Context context) {
		if (context != null) {

			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetWorkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetWorkInfo != null) {

				return mNetWorkInfo.isAvailable();
			}

		}
		return false;
	}

	// 判断 WiFi网络是否可用
	public boolean isWifiConnected(Context context) {

		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {

				return mWiFiNetworkInfo.isAvailable();
			}

		}
		return false;

	}

	// 判断WIFI网络是否可用
	public boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	// 判断当前网络连接的类型信息
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetWorkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetWorkInfo != null && mNetWorkInfo.isAvailable()) {
				return mNetWorkInfo.getType();
			}
		}

		return -1;
	}

}
