package com.saubcy.pipeline.ads;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.adpooh.adscast.banner.AdkBannerView;
import com.adpooh.adscast.banner.AdkManager;
import com.adwo.adsdk.AdwoAdView;
import com.saubcy.conf.Config;
import com.uucun.adsdk.UUAppConnect;

public class AdsManager {

	private static final String TAG = "TRACE_AdsFactory_";

	private static boolean isYoumiInit = false;
	private static boolean isAppMediaInit = false;
	private static boolean isMiidiInit = false;

	public enum Offers {
		NONE, ADMOB, ADWO, YOUMI, APPMEDIA, MIIDI, WIYUN,
		ADULTMODA, APPJOY,
	};

	public static View showAds(Offers offer, 
			Activity content, 
			LinearLayout layout, 
			View adView) {
		switch (offer) {
		case ADMOB:
			return showADMOB(content, layout, adView);
		case ADWO:
			return showADWO(content, layout, adView);
		case YOUMI:
			return showYOUMI(content, layout, adView);
		case APPMEDIA:
			return showAPPMEDIA(content, layout, adView);
		case MIIDI:
			return showMIIDI(content, layout, adView);
		case WIYUN:
			return showWIYUN(content, layout, adView);
		case ADULTMODA:
			return showADULTMODA(content, layout, adView);
		case APPJOY:
			return showAPPJOY(content, layout);
		}
		return null;
	}

	public static void destoryAds(Offers offer, 
			View adView, Activity content) {
		switch (offer) {
		case ADWO:
			destoryADWO(adView);
			break;
		case APPJOY:
			destoryAPPJOY(content);
			break;
		}
	}

	private static View showADWO(Activity content, 
			LinearLayout layout, 
			View adView) {
		String TAG_USE = TAG + "showADWO";

		if ( Config.getLOGGING() ) {
			Log.d(TAG_USE, "add adwo pid: "+Config.getAdwo_PID());
		}
		try {
			Class<?> AdwoAdView = 
					Class.forName("com.adwo.adsdk.AdwoAdView");

			Object[] args = new Object[6];
			args[0] = content.getApplicationContext();
			args[1] = Config.getAdwo_PID();
			args[2] = 0x0040080;
			args[3] = 0x00FF0000;
			args[4] = Config.getTESTMODE();
			args[5] = 30;

			Class<?>[] argsClass = new Class[args.length];
			argsClass[0] = android.content.Context.class;
			argsClass[1] = java.lang.String.class;
			argsClass[2] = int.class;
			argsClass[3] = int.class;
			argsClass[4] = boolean.class;
			argsClass[5] = int.class;

			if ( Config.getLOGGING() ) {
				for (int i=0; i<args.length; ++i) {
					Log.d(TAG_USE, "input param name: " 
							+ argsClass[i].getName());
				}
				Constructor<?>[] conslist = AdwoAdView.getConstructors();
				for (int i=0; i<conslist.length; ++i) {
					Log.d(TAG_USE, "constructor name: " + conslist[i].getName());
					Class<?>[] params = conslist[i].getParameterTypes();
					for (int j=0; j<params.length; ++j) {
						Log.d(TAG_USE, "param name: " + params[j].getName());
					}
				}
			}

			Constructor<?> cons = AdwoAdView.getConstructor(argsClass);
			if ( Config.getLOGGING() ) {
				Log.d(TAG_USE, "Constructor get successful");
			}
			adView = (View) cons.newInstance(args);
			if ( null == adView ) {
				if ( Config.getLOGGING() ) {
					Log.d(TAG_USE, "ads instance get failed");
				}
			} else {
				layout.addView(adView, new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT) );
			}
		} catch (ClassNotFoundException e) {
			if ( Config.getLOGGING() ) {
				Log.d(TAG_USE, "ClassNotFoundException");
			}
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			if ( Config.getLOGGING() ) {
				Log.d(TAG, "NoSuchMethodException");
			}
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		return adView;
	}

	private static void destoryADWO(View adView) {
		if ( null == adView ) {
			return;
		}

		AdwoAdView view = (AdwoAdView)adView;
		view.finalize();
	}

	private static View showADMOB(Activity content, 
			LinearLayout layout, 
			View adView) {
		String TAG_USE = TAG + "showADMOB";

		if ( Config.getLOGGING() ) {
			Log.d(TAG_USE, "add AdMob PID: "+Config.getAdMob_PID());
		}
		try {
			Class<?> AdView = 
					Class.forName("com.google.ads.AdView");
			Class<?> AdSize = 
					Class.forName("com.google.ads.AdSize");
			Class<?> AdRequest =
					Class.forName("com.google.ads.AdRequest");

			Field field = AdSize.getField("BANNER");

			Object[] args = new Object[3];
			args[0] = content;
			args[1] = field.get(AdSize);;
			args[2] = Config.getAdMob_PID();
			//
			Class<?>[] argsClass = new Class[args.length];
			argsClass[0] = android.app.Activity.class;
			argsClass[1] = AdSize;
			argsClass[2] = java.lang.String.class;

			if ( Config.getLOGGING() ) {
				for (int i=0; i<args.length; ++i) {
					Log.d(TAG_USE, "input param name: " 
							+ argsClass[i].getName());
				}
				Constructor<?>[] conslist = AdView.getConstructors();
				Log.d(TAG_USE, "constructor number: " + conslist.length);
				for (int i=0; i<conslist.length; ++i) {
					Log.d(TAG_USE, "constructor name: " + conslist[i].getName());
					Class<?>[] params = conslist[i].getParameterTypes();
					for (int j=0; j<params.length; ++j) {
						Log.d(TAG_USE, "param name: " + params[j].getName());
					}
				}
			}

			Constructor<?> cons = AdView.getConstructor(argsClass);
			if ( Config.getLOGGING() ) {
				Log.d(TAG_USE, "Constructor get successful");
			}
			adView = (View) cons.newInstance(args);
			if ( null == adView ) {
				if ( Config.getLOGGING() ) {
					Log.d(TAG_USE, "ads instance get failed");
				}
			} else {
				layout.addView(adView, new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT) );
			}

			args = new Object[1];
			args[0] = AdRequest.newInstance();
			argsClass = new Class[args.length];
			argsClass[0] = AdRequest;
			if ( Config.getLOGGING() ) {
				Method[] methods = AdView.getMethods();
				for (int i=0; i<methods.length; ++i) {
					Log.d(TAG_USE, "method name: " + methods[i].getName());
					Class<?>[] params = methods[i].getParameterTypes();
					for (int j=0; j<params.length; ++j) {
						Log.d(TAG_USE, "param name: " + params[j].getName());
					}
				}
			}

			Method method = AdView.getMethod("loadAd", argsClass);
			method.invoke(adView, args);
		} catch (ClassNotFoundException e) {
			if ( Config.getLOGGING() ) {
				Log.d(TAG_USE, "ClassNotFoundException");
			}
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			if ( Config.getLOGGING() ) {
				Log.d(TAG_USE, "NoSuchMethodException");
			}
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			if ( Config.getLOGGING() ) {
				Log.d(TAG_USE, "IllegalArgumentException");
			}
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		return adView;
	}

	private static View showYOUMI(Activity content, 
			LinearLayout layout, 
			View adView) {
		initYOUMI();
		String TAG_USE = TAG + "showYOUMI";

		if ( Config.getLOGGING() ) {
			Log.d(TAG_USE, "add youmi");
		}
		try {
			Class<?> AdView = 
					Class.forName("net.youmi.android.AdView");

			Object[] args = new Object[1];
			args[0] = content;

			Class<?>[] argsClass = new Class[args.length];
			argsClass[0] = android.app.Activity.class;

			Constructor<?> cons = AdView.getConstructor(argsClass);
			if ( Config.getLOGGING() ) {
				Log.d(TAG_USE, "Constructor get successful");
			}
			adView = (View) cons.newInstance(args);
			if ( null == adView ) {
				if ( Config.getLOGGING() ) {
					Log.d(TAG_USE, "ads instance get failed");
				}
			} else {
				layout.addView(adView, new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT) );
			}
		} catch (ClassNotFoundException e) {
			if ( Config.getLOGGING() ) {
				Log.d(TAG_USE, "ClassNotFoundException");
			}
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			if ( Config.getLOGGING() ) {
				Log.d(TAG, "NoSuchMethodException");
			}
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		return adView;
	}

	private static void initYOUMI() {
		if ( isYoumiInit ) {
			return;
		}
		String TAG_USE = TAG + "initYOUMI";

		if ( Config.getLOGGING() ) {
			Log.d(TAG_USE, "init youmi appid: "+Config.getYoumi_APPID());
			Log.d(TAG_USE, "init youmi appsec: "+Config.getYoumi_APPSEC());
		}
		try {
			Class<?> AdManager = 
					Class.forName("net.youmi.android.AdManager");

			Object[] args = new Object[4];
			args[0] = Config.getYoumi_APPID();
			args[1] = Config.getYoumi_APPSEC();
			args[2] = 30;
			args[3] = Config.getTESTMODE();

			Class<?>[] argsClass = new Class[args.length];
			argsClass[0] = java.lang.String.class;
			argsClass[1] = java.lang.String.class;
			argsClass[2] = int.class;
			argsClass[3] = boolean.class;

			Method method = AdManager.getMethod("init", argsClass);
			method.invoke(null, args);
			isYoumiInit = true;

		} catch (ClassNotFoundException e) {
			if ( Config.getLOGGING() ) {
				Log.d(TAG_USE, "ClassNotFoundException");
			}
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			if ( Config.getLOGGING() ) {
				Log.d(TAG, "NoSuchMethodException");
			}
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private static View showAPPMEDIA(Activity content, 
			LinearLayout layout, 
			View adView) {
		initAPPMEDIA();
		String TAG_USE = TAG + "showAPPMEDIA";

		if ( Config.getLOGGING() ) {
			Log.d(TAG_USE, "add appmedia");
		}
		try {
			Class<?> BannerAdView = 
					Class.forName("cn.appmedia.ad.BannerAdView");

			Object[] args = new Object[1];
			args[0] = content;

			Class<?>[] argsClass = new Class[args.length];
			argsClass[0] = android.content.Context.class;

			Constructor<?> cons = BannerAdView.getConstructor(argsClass);
			if ( Config.getLOGGING() ) {
				Log.d(TAG_USE, "Constructor get successful");
			}
			adView = (View) cons.newInstance(args);
			if ( null == adView ) {
				if ( Config.getLOGGING() ) {
					Log.d(TAG_USE, "ads instance get failed");
				}
			} else {
				layout.addView(adView, new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT) );
			}
		} catch (ClassNotFoundException e) {
			if ( Config.getLOGGING() ) {
				Log.d(TAG_USE, "ClassNotFoundException");
			}
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			if ( Config.getLOGGING() ) {
				Log.d(TAG, "NoSuchMethodException");
			}
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		return adView;
	}

	private static View showMIIDI(Activity content, 
			LinearLayout layout, 
			View adView) {
		initMiidi();

		adView = new AdkBannerView(content);
		layout.addView(adView, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT) );
		
		return adView;
	}

	private static View showWIYUN(Activity content, 
			LinearLayout layout, 
			View adView) {

		adView = new com.wiyun.ad.AdView(content);
		((com.wiyun.ad.AdView) adView).setResId(Config.getWIYUN_WIADID());
		((com.wiyun.ad.AdView) adView).setGoneIfFail(true);

		layout.addView(adView, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT) );
		((com.wiyun.ad.AdView) adView).requestAd();
		
		return adView;
	}

	private static void initAPPMEDIA() {
		String TAG_USE = TAG + "initAPPMEDIA";
		if ( isAppMediaInit ) {
			Log.d(TAG_USE, "already inited");
			return;
		}

		if ( Config.getLOGGING() ) {
			Log.d(TAG_USE, "init appmedia pid: "+Config.getAppMedia_PID());
		}
		try {
			Class<?> AdManager = 
					Class.forName("cn.appmedia.ad.AdManager");

			Object[] args = new Object[1];
			args[0] = Config.getAppMedia_PID();

			Class<?>[] argsClass = new Class[args.length];
			argsClass[0] = java.lang.String.class;

			Method method = AdManager.getMethod("setAid", argsClass);
			method.invoke(null, args);
			isAppMediaInit = true;

		} catch (ClassNotFoundException e) {
			if ( Config.getLOGGING() ) {
				Log.d(TAG_USE, "ClassNotFoundException");
			}
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			if ( Config.getLOGGING() ) {
				Log.d(TAG, "NoSuchMethodException");
			}
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private static void initMiidi() {
		if ( isMiidiInit ) {
			return;
		}

		AdkManager.initialParam(Integer.parseInt(Config.getMiidi_APPID()),
				Config.getMiidi_APPSEC());
	}

	private static void destoryAPPJOY(Activity content) {
		UUAppConnect.getInstance(content).exitSdk();
	}

	private static View showADULTMODA(Activity content, 
			LinearLayout layout, 
			View adView) {

		adView = new com.admoda.AdView(content);
		((com.admoda.AdView) adView).setTextZoneId
		(Integer.parseInt(Config.getADULTMODA_TEXT_ZONEID()));
		((com.admoda.AdView) adView).setBannerZoneId
		(Integer.parseInt(Config.getADULTMODA_BANNER_ZONEID()));
		layout.addView(adView, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT) );
		return adView;
	}

	private static View showAPPJOY(Activity content, 
			LinearLayout layout) {
		UUAppConnect.getInstance(content)
		.showBanner(layout , 20);
		return null;
	}

}
