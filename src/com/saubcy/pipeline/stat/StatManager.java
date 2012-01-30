package com.saubcy.pipeline.stat;

import android.app.Activity;
import android.content.Context;

import com.mobclick.android.MobclickAgent;

public class StatManager {
	
	private static boolean isUmengInit = false;

	public enum Offers {
		NONE, UMENG
	};

	public static void onInit(Offers offer, 
			Context content) {
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
			Context content) {
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
	
	public static void onEvent(Offers offer, 
			Activity content, 
			String eventName,
			String eventLabel, int acc) {
		switch (offer) {
		case UMENG:
			onEventUMENG(content, eventName, eventLabel, acc);
			break;
		}
	}
	
	private static void onInitUMENG(Context content) {
		
		if ( isUmengInit ) {
			return;
		}
		
		MobclickAgent.onError(content);
		
		isUmengInit = true;
	}

	private static void onResumeUMENG(Context content) {
		MobclickAgent.onResume(content);
	}

	private static void onPauseUMENG(Context content) {
		MobclickAgent.onPause(content);
	}
	
	private static void onEventUMENG(Context content, 
			String eventName, String eventLabel) {
		if ( null == eventLabel ) {
			MobclickAgent.onEvent(content, eventName);
			return;
		} 
		MobclickAgent.onEvent(content, eventName, eventLabel);
	}
	
	private static void onEventUMENG(Context content, 
			String eventName, String eventLabel, int acc) {
		if ( null == eventLabel ) {
			MobclickAgent.onEvent(content, eventName, acc);
			return;
		} 
		MobclickAgent.onEvent(content, eventName, eventLabel, acc);
	}
	
}
