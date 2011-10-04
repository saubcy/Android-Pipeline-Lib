package com.saubcy.pipeline.gold;

public interface InnerNotifier {
	
	public void notifyUpdate(String currencyName, int num);
	
	public void notifyFailed(String reason);
}
