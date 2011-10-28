package com.saubcy.pipeline.stat;

import android.app.Activity;

import com.mobclick.android.MobclickAgent;

public class StatManager {
	
	private static boolean isUmengInit = false;

	public enum Offers {
		NONE, UMENG
	};

	public static void onInit(Offers offer, 
			Activity content) {
		switch (offer) {
		case UMENG:
			onInitUMENG(content);
			break;
		}
	}

	public static void onResume(Offers offer, 
			Activity content) {
		switch (offer) {
		case UMENG:
			onResumeUMENG(content);
			break;
		}
	}

	public static void onPause(Offers offer, 
			Activity content) {
		switch (offer) {
		case UMENG:
			onPauseUMENG(content);
			break;
		}
	}
	
	public static void onEvent(Offers offer, 
			Activity content, 
			String eventName,
			String eventLabel) {
		switch (offer) {
		case UMENG:
			onEventUMENG(content, eventName, eventLabel);
			break;
		}
	}
	
	private static void onInitUMENG(Activity content) {
		
		if ( isUmengInit ) {
			return;
		}
		
		MobclickAgent.onError(content);
		
		isUmengInit = true;
	}

	private static void onResumeUMENG(Activity content) {
		MobclickAgent.onResume(content);
	}

	private static void onPauseUMENG(Activity content) {
		MobclickAgent.onPause(content);
	}
	
	private static void onEventUMENG(Activity content, 
			String eventName, String eventLabel) {
		MobclickAgent.onEvent(content, eventName, eventLabel);
	}
	
}
