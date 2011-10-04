package com.saubcy.pipeline.gold;

import com.waps.UpdatePointsNotifier;

public class WapsAgent implements UpdatePointsNotifier{

	InnerNotifier notifier = null;

	public WapsAgent(InnerNotifier in) {
		notifier = in;
	}

	@Override
	public void getUpdatePoints(String currencyName, int pointTotal) {
		notifier.notifyUpdate(currencyName, pointTotal);
	}

	@Override
	public void getUpdatePointsFailed(String error) {
		notifier.notifyFailed(error);
	}
}
