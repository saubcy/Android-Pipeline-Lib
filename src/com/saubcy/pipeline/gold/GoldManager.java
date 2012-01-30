package com.saubcy.pipeline.gold;

import net.miidi.credit.MiidiCredit;
import android.app.Activity;
import android.content.Context;

import com.saubcy.conf.Config;
import com.tapjoy.TapjoyConnect;
import com.tapjoy.TapjoyLog;
import com.uucun.adsdk.UUAppConnect;
import com.waps.AppConnect;
import com.wiyun.offer.WiOffer;
import com.wiyun.offer.WiOfferClient;

public class GoldManager implements InnerNotifier{

	private GoldNotifier gn = null;
	private Offers offerType = Offers.NONE;
	private int iconid = -1;

	public enum Offers {
		NONE, YOUMI, WIYUN, WAPS, TAPJOY, MIIDI, APPJOY,
	};

	private int golds = 0;

	private boolean isYoumiInit = false;
	private boolean isWiyunInit = false;
	private boolean isTapjoyInit = false;
	private boolean isAppjoyInit = false;
	
	public void setIcon(int id) {
		iconid = id;
	}

	public GoldManager() {

	}

	public int getGold() {
		return golds;
	}

	public void init(Offers offer, 
			Context content) {
		gn = (GoldNotifier)content;
		offerType = offer;

		switch (offer) {
		case YOUMI:
			initYOUMI(content);
			break;
		case WIYUN:
			initWIYUN(content);
			break;
		case WAPS:
			break;
		case TAPJOY:
			initTAPJOY(content);
			break;
		case MIIDI:
			initMIIDI(content);
			break;
		case APPJOY:
			initAPPJOY(content);
			break;
		}
	}

	public void showOffer(Offers offer, 
			Context content) {
		gn = (GoldNotifier)content;
		offerType = offer;

		switch (offer) {
		case YOUMI:
			showOfferYOUMI(content);
			break;
		case WIYUN:
			showOfferWIYUN();
			break;
		case WAPS:
			showOfferWAPS(content);
			break;
		case TAPJOY:
			showOfferTAPJOY();
			break;
		case MIIDI:
			showOfferMIIDI(content);
			break;
		case APPJOY:
			showOfferAPPJOY(content);
			break;
		}
	}

	public void refreshGold(Offers offer, 
			Context content) {
		gn = (GoldNotifier)content;
		offerType = offer;

		switch (offer) {
		case YOUMI:
			refreshYOUMI(content, true);
			break;
		case WIYUN:
			refreshWIYUN();
			break;
		case WAPS:
			refreshWAPS(content);
			break;
		case TAPJOY:
			refreshTAPJOY();
			break;
		case MIIDI:
			refreshMIIDI(content, true);
			break;
		case APPJOY:
			refreshAPPJOY(content);
			break;
		}
	}

	public void spendGold(Offers offer, 
			Activity content, int amount) {
		gn = (GoldNotifier)content;
		offerType = offer;

		switch (offer) {
		case YOUMI:
			spendGoldYOUMI(content, amount);
			break;
		case WIYUN:
			spendGoldWIYUN(amount);
			break;
		case WAPS:
			spendGoldWAPS(content, amount);
		case TAPJOY:
			spendGoldTAPJOY(amount);
			break;
		case MIIDI:
			spendGoldMIIDI(content, amount);
			break;
		case APPJOY:
			spendGoldAPPJOY(content, amount);
			break;
		}
	}
	
	public void awardGold(Offers offer, 
			Activity content, int amount) {
		gn = (GoldNotifier)content;
		offerType = offer;
		
		switch (offer) {
		case TAPJOY:
			awardGoldTAPJOY(amount);
			break;
		}
	}
	
	private void initYOUMI(Context content) {

		net.youmi.android.appoffers.AppOffersManager.init((Activity) content, 
				Config.getYoumi_Gold_APPID(), 
				Config.getYoumi_Gold_APPSEC(), 
				Config.getTESTMODE());
		isYoumiInit = true;
	}
	
	private void showOfferYOUMI(Context content) {

		net.youmi.android.appoffers.AppOffersManager.showAppOffers(content);

	}
	
	private void refreshYOUMI(Context content, boolean forceNotify) {

		if ( !isYoumiInit ) {
			gn.notifyFailed("youmi gold not init");
			return;
		}

		int tmp = net.youmi.android.appoffers
				.AppOffersManager.getPoints(content);
		if ( golds != tmp ) {
			golds = tmp;
			gn.notifyUpdate(golds);
		} else if ( forceNotify ) {
			gn.notifyUpdate(golds);
		}
	}

	private void spendGoldYOUMI(Context content, 
			int amount) {

		if ( !isYoumiInit ) {
			gn.notifyFailed("youmi gold not init");
			return;
		}

		refreshYOUMI(content, false);

		if ( golds < amount ) {
			gn.notifyFailed("youmi gold not enough");
			return;
		}

		boolean res = net.youmi.android.appoffers
				.AppOffersManager.spendPoints(content, amount);
		if ( res ) {
			golds -= amount;
			gn.notifyUpdate(golds);
		} else {
			gn.notifyFailed("youmi gold spend failed");
		}
	}

	private void initWIYUN(Context content) {

		if ( isWiyunInit ) {
			return;
		}

		WiOffer.init(content, 
				Config.getWiyun_Gold_APPID(), 
				Config.getWiyun_Gold_APPSEC());

		WiOffer.addWiOfferClient(new WiOfferClient() {
			@Override
			public void wyOfferCompleted(String offerId, String offerName, int bonus) {
				WiOffer.getCoins();
			}

			@Override
			public void wyCoinsGot(int count) {
				golds = count;
				gn.notifyUpdate(count);
			}

			@Override
			public void wyGetCoinsFailed(String error) {
				gn.notifyFailed(error);
			}

			@Override
			public void wyAddCoinsFailed(String error) {
				gn.notifyFailed(error);
			}

			@Override
			public void wyCoinsAdded(int count) {
				golds += count;
				gn.notifyUpdate(count);
			}

			@Override
			public void wyCoinsUsed(int count) {
				golds -= count;
				gn.notifyUpdate(count*(-1));
			}

			@Override
			public void wyUseCoinsFailed(String error) {
				gn.notifyFailed(error);
			}
		});

		WiOffer.setSandboxMode(Config.getTESTMODE());

		isWiyunInit = true;
	}

	private void showOfferWIYUN() {

		if ( !isWiyunInit ) {
			return;
		}

		WiOffer.showOffers();
	}

	private void refreshWIYUN() {
		WiOffer.getCoins();
	}

	private void spendGoldWIYUN(int amount) {
		if ( !isWiyunInit ) {
			return;
		}

		if ( golds < amount ) {
			gn.notifyFailed("wiyun gold not enough");
			return;
		}

		WiOffer.useCoins(amount);
	}

	private void showOfferWAPS(Context content) {
		AppConnect.getInstance(content).showOffers(content);
	}

	private void refreshWAPS(Context content) {
		AppConnect.getInstance(content).getPoints(new WapsAgent(this));
	}

	private void spendGoldWAPS(Context content, int amount) {

		refreshWAPS(content);

		AppConnect.getInstance(content)
		.spendPoints(amount, new WapsAgent(this));
	}

	private void initTAPJOY(Context content) {

		if ( isTapjoyInit ) {
			return;
		}

		TapjoyLog.enableLogging(Config.getLOGGING());
		TapjoyConnect.requestTapjoyConnect(content.getApplicationContext(), 
				Config.getTapjoy_Gold_APPID(), 
				Config.getTapjoy_Gold_APPSEC());
		isTapjoyInit = true;
	}

	private void showOfferTAPJOY() {
		if ( !isTapjoyInit ) {
			return;
		}

		TapjoyConnect.getTapjoyConnectInstance().showOffers();
	}

	private void refreshTAPJOY() {
		if ( !isTapjoyInit ) {
			return;
		}

		TapjoyConnect.getTapjoyConnectInstance()
		.getTapPoints(new TapjoyAgent(this));
	}

	private void spendGoldTAPJOY(int amount) {
		if ( !isTapjoyInit ) {
			return;
		}

		if ( golds < amount ) {
			gn.notifyFailed("tapjoy gold not enough");
			return;
		}

		TapjoyConnect.getTapjoyConnectInstance()
		.spendTapPoints(amount, new TapjoyAgent(this));
	}

	private void awardGoldTAPJOY(int amount) {
		if ( !isTapjoyInit ) {
			return;
		}

		if ( amount < 0 ) {
			gn.notifyFailed("award must more then 0");
			return;
		}

		TapjoyConnect.getTapjoyConnectInstance()
		.awardTapPoints(amount, new TapjoyAgent(this));
	}
	
	private void initMIIDI(Context content) {

		MiidiCredit.init(content, 
				Config.getMiidi_Gold_APPID(), 
				Config.getMiidi_Gold_APPSEC(), 
				Config.getTESTMODE());
		
		MiidiCredit.setPushAdIcon(iconid);
	}

	private void showOfferMIIDI(Context content) {

		MiidiCredit.showAppOffers(content);

	}

	private void refreshMIIDI(Context content, boolean forceNotify) {

		int tmp = MiidiCredit.getPoints(content);
		if ( golds != tmp ) {
			golds = tmp;
			gn.notifyUpdate(golds);
		} else if ( forceNotify ) {
			gn.notifyUpdate(golds);
		}
	}

	private void spendGoldMIIDI(Context content, 
			int amount) {

		refreshMIIDI(content, false);

		if ( golds < amount ) {
			gn.notifyFailed("miidi gold not enough");
			return;
		}

		boolean res = MiidiCredit.spendPoints(content, amount);
		if ( res ) {
			golds -= amount;
			gn.notifyUpdate(golds);
		} else {
			gn.notifyFailed("miidi gold spend failed");
		}
	}
	
	private void initAPPJOY(Context content) {
		if ( isAppjoyInit ) {
			return;
		}
		UUAppConnect.getInstance(content).initSdk();
		isAppjoyInit = true;
	}
	
	private void showOfferAPPJOY(Context content) {
		if ( !isAppjoyInit ) {
			return;
		}
		UUAppConnect.getInstance(content).showOffers();
	}
	
	private void refreshAPPJOY(Context content) {
		if ( !isAppjoyInit ) {
			return;
		}
		UUAppConnect.getInstance(content)
		.getPoints(new AppjoyAgent(this));
	}
	
	private void spendGoldAPPJOY(Context content, 
			int amount) {
		if ( !isAppjoyInit ) {
			return;
		}

		if ( golds < amount ) {
			gn.notifyFailed("appjoy gold not enough");
			return;
		}

		UUAppConnect.getInstance(content)
		.spendPoints(amount,new AppjoyAgent(this));
	}

	@Override
	public void notifyUpdate(String currencyName, int num) {
		int delta = 0;
		switch (offerType) {
		case TAPJOY:
		case WAPS:
		case APPJOY:
			delta = num - golds;
			golds = num;
			gn.notifyUpdate(delta);
			break;
		}
	}

	@Override
	public void notifyFailed(String reason) {
		gn.notifyFailed(reason);
	}
}
