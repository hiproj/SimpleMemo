package com.simplememo;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by USER on 2016-12-06.
 */
public class NetworkMgr {
	public static boolean isNetWork(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
		boolean isMobileAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isAvailable();
		boolean isMobileConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		boolean isWifiAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable();
		boolean isWifiConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		Debug.d("isMobileAvailable : " + isMobileAvailable);
		Debug.d("isMobileConnect : " + isMobileConnect);
		Debug.d("isWifiAvailable : " + isWifiAvailable);
		Debug.d("isWifiConnect : " + isWifiConnect);
		if ((isWifiAvailable && isWifiConnect) || (isMobileAvailable && isMobileConnect)){
			return true;
		}else{
			return false;
		}
	}
}
