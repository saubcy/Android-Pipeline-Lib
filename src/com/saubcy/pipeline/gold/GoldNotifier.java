package com.saubcy.pipeline.gold;

public interface GoldNotifier {
	
	public void notifyUpdate(int num);
	
	public void notifyFailed(String reason);
}
