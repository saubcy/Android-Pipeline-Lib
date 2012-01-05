package com.saubcy.pipeline.gold;

import com.tapjoy.TapjoyAwardPointsNotifier;
import com.tapjoy.TapjoyNotifier;
import com.tapjoy.TapjoySpendPointsNotifier;

public class TapjoyAgent implements TapjoyNotifier, 
TapjoySpendPointsNotifier, TapjoyAwardPointsNotifier {

	private InnerNotifier notifier = null;

	public TapjoyAgent(InnerNotifier in) {
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

	@Override
	public void getSpendPointsResponse(String currencyName, int pointTotal) {
		getUpdatePoints(currencyName, pointTotal);
	}

	@Override
	public void getSpendPointsResponseFailed(String error) {
		getUpdatePointsFailed(error);
	}

	@Override
	public void getAwardPointsResponse(String currencyName, int pointTotal) {
		getUpdatePoints(currencyName, pointTotal);
	}

	@Override
	public void getAwardPointsResponseFailed(String error) {
		getUpdatePointsFailed(error);
	}

}
