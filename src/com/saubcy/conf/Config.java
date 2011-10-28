package com.saubcy.conf;

import com.saubcy.pipeline.gold.GoldManager;
import com.saubcy.pipeline.stat.StatManager;

public class Config
{
	public static GoldManager gm = new GoldManager();

	public static StatManager.Offers StatOffer = StatManager.Offers.NONE;

	public static StatManager.Offers getStatOffer() {
		return StatOffer;
	}

	public static boolean getLOGGING() {
		return true;
	}

	public static boolean getTESTMODE() {
		return true;
	}
	
	public static GoldManager.Offers getGoldOffer() {
		return GoldManager.Offers.NONE;
	}

	// AD OFFSER IDS
	public static String getAdwo_PID() {
		return "@CONFIG.ADS.ADWO.PID@";
	}

	public static String getAdMob_PID() {
		return "@CONFIG.ADS.ADMOB.PID@";
	}

	public static String getYoumi_APPID() {
		return "@CONFIG.ADS.YOUMI.APPID@";
	}

	public static String getYoumi_APPSEC() {
		return "@CONFIG.ADS.YOUMI.APPSEC@";
	}

	public static String getAppMedia_PID() {
		return "@CONFIG.ADS.APPMEDIA.PID@";
	}
	
	public static String getMiidi_APPID() {
		return "@CONFIG.ADS.MIIDI.APPID@";
	}
	
	public static String getMiidi_APPSEC() {
		return "@CONFIG.ADS.MIIDI.APPSEC@";
	}
	
	public static String getWIYUN_WIADID() {
		return "@CONFIG.ADS.WIYUN.WIADID@";
	}

	// GOLD OFFER IDS
	public static String getYoumi_Gold_APPID() {
		return "@CONFIG.GOLD.YOUMI.APPID@";
	}

	public static String getYoumi_Gold_APPSEC() {
		return "@CONFIG.GOLD.YOUMI.APPSEC@";
	}

	public static String getWiyun_Gold_APPID() {
		return "@CONFIG.GOLD.WIYUN.APPID@";
	}

	public static String getWiyun_Gold_APPSEC() {
		return "@CONFIG.GOLD.WIYUN.APPSEC@";
	}

	public static String getWaps_Gold_ID() {
		return "@CONFIG.GOLD.Waps.ID@";
	}

	public static String getTapjoy_Gold_APPID() {
		return "@CONFIG.GOLD.TAPJOY.APPID@";
	}

	public static String getTapjoy_Gold_APPSEC() {
		return "@CONFIG.GOLD.TAPJOY.APPSEC@";
	}
	
	public static String getMiidi_Gold_APPID() {
		return "@CONFIG.GOLD.MIIDI.APPID@";
	}
	
	public static String getMiidi_Gold_APPSEC() {
		return "@CONFIG.GOLD.MIIDI.APPSEC@";
	}
	
}