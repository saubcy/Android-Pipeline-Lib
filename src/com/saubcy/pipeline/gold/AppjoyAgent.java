package com.saubcy.pipeline.gold;

import com.uucun.adsdk.UpdatePointListener;

public class AppjoyAgent implements UpdatePointListener{

	InnerNotifier notifier = null;

	public AppjoyAgent(InnerNotifier in) {
		notifier = in;
	}
	
	@Override
	public void onError(String msg) {
		notifier.notifyFailed(msg);
	}

	@Override
	public void onSuccess(String unit, int total) {
		notifier.notifyUpdate(unit, total);
	}
}
