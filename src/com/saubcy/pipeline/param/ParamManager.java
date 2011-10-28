package com.saubcy.pipeline.param;

import android.app.Activity;
import android.util.Log;

import com.mobclick.android.MobclickAgent;
import com.saubcy.conf.Config;

public class ParamManager {
	
	private static String TAG = "ParamManager";
	
	public static final String AdsPost = "_ads";
	public static final String GoldPost = "_gold";
	
	private static boolean isAdStatusGot = false;
	private static boolean isGoldStatusGot = false;
	private static boolean adStatus = false;
	private static boolean goldStatus = false;

	public enum Offers {
		NONE, UMENG
	};

	public static boolean getAdsStatus(Offers offer, 
			Activity content, String versionName) {
		
		String TAG_USE = TAG + "getAdsStatus";
		if ( Config.getLOGGING() ) {
			Log.d(TAG_USE, "in");
		}
		
		if ( isAdStatusGot ) {
			return adStatus;
		}
		
		MobclickAgent.updateOnlineConfig(content);
		String switcher= 
				MobclickAgent.getConfigParams(content, 
						versionName+AdsPost);
		if("".equals(switcher)){
			if ( Config.getLOGGING() ) {
				Log.d(TAG_USE, "get failed");
			}
		} else{
			isAdStatusGot = true;
			if(switcher.equals("on")) {
				adStatus = true;
				if ( Config.getLOGGING() ) {
					Log.d(TAG_USE, "on");
				}
			} else {
				adStatus = false;
				if ( Config.getLOGGING() ) {
					Log.d(TAG_USE, "off");
				}
			}
		}
		return adStatus;
	}
	
	public static boolean getGoldStatus(Offers offer, 
			Activity content, String versionName) {
		
		String TAG_USE = TAG + "getGoldStatus";
		if ( Config.getLOGGING() ) {
			Log.d(TAG_USE, "in");
		}
		
		if ( isGoldStatusGot ) {
			return goldStatus;
		}
		
		MobclickAgent.updateOnlineConfig(content);
		String switcher= 
				MobclickAgent.getConfigParams(content, 
						versionName+GoldPost);
		if("".equals(switcher)){
			if ( Config.getLOGGING() ) {
				Log.d(TAG_USE, "get failed");
			}
		} else{
			if(switcher.equals("on")) {
				goldStatus = true;
				if ( Config.getLOGGING() ) {
					Log.d(TAG_USE, "on");
				}
			} else {
				goldStatus = false;
				if ( Config.getLOGGING() ) {
					Log.d(TAG_USE, "off");
				}
			}
		}
		return goldStatus;
	}
}
