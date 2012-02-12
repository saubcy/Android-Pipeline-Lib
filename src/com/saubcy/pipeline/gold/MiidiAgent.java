package com.saubcy.pipeline.gold;

import net.miidi.credit.IOffersNotifier;

public class MiidiAgent implements IOffersNotifier {

	InnerNotifier notifier = null;
	
	public MiidiAgent(InnerNotifier in) {
		notifier = in;
	}
	
	@Override
	public void onShowAppsFinishCb() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdatePointsCb(String currencyName, int pointTotal) {
		notifier.notifyUpdate(currencyName, pointTotal);
	}

}
