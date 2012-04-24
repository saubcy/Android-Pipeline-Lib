package com.saubcy.pipeline.ads;

import com.hiad.HiProxyListener;
import com.saubcy.pipeline.gold.InnerNotifier;

public class HuaweiAgent implements HiProxyListener{
	
	InnerNotifier notifier = null;
	
	public HuaweiAgent(InnerNotifier in) {
		notifier = in;
	}

	@Override
	public void onFailedReceiveAd(String arg0) {
	}

	@Override
	public void onReceiveAd(String arg0) {
	}

}
