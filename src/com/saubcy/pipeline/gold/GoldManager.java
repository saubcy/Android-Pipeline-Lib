package com.saubcy.pipeline.gold;

import android.app.Activity;
import android.content.Context;

import com.saubcy.conf.Config;
import com.saubcy.pipeline.ads.AdsManager;
import com.uucun.adsdk.UUAppConnect;
import com.waps.AppConnect;
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
		case MIIDI:
			awardGoldMIIDI(amount);
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

		com.wiyun.offer.WiOffer.init(content, 
				Config.getWiyun_Gold_APPID(), 
				Config.getWiyun_Gold_APPSEC());

		com.wiyun.offer.WiOffer.addWiOfferClient(new WiOfferClient() {
			@Override
			public void wyOfferCompleted(String offerId, String offerName, int bonus) {
				com.wiyun.offer.WiOffer.getCoins();
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

		com.wiyun.offer.WiOffer.setSandboxMode(Config.getTESTMODE());

		isWiyunInit = true;
	}

	private void showOfferWIYUN() {

		if ( !isWiyunInit ) {
			return;
		}

		com.wiyun.offer.WiOffer.showOffers();
	}

	private void refreshWIYUN() {
		com.wiyun.offer.WiOffer.getCoins();
	}

	private void spendGoldWIYUN(int amount) {
		if ( !isWiyunInit ) {
			return;
		}

		if ( golds < amount ) {
			gn.notifyFailed("wiyun gold not enough");
			return;
		}

		com.wiyun.offer.WiOffer.useCoins(amount);
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

		com.tapjoy.TapjoyLog.enableLogging(Config.getLOGGING());
		com.tapjoy.TapjoyConnect.requestTapjoyConnect(content.getApplicationContext(), 
				Config.getTapjoy_Gold_APPID(), 
				Config.getTapjoy_Gold_APPSEC());
		isTapjoyInit = true;
	}

	private void showOfferTAPJOY() {
		if ( !isTapjoyInit ) {
			return;
		}

		com.tapjoy.TapjoyConnect.getTapjoyConnectInstance().showOffers();
	}

	private void refreshTAPJOY() {
		if ( !isTapjoyInit ) {
			return;
		}

		com.tapjoy.TapjoyConnect.getTapjoyConnectInstance()
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

		com.tapjoy.TapjoyConnect.getTapjoyConnectInstance()
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

		com.tapjoy.TapjoyConnect.getTapjoyConnectInstance()
		.awardTapPoints(amount, new TapjoyAgent(this));
	}

	private void initMIIDI(Context content) {

		/**
		 * 1.x 接口
		 */
		//		net.miidi.credit.MiidiCredit.init(content, 
		//				Config.getMiidi_Gold_APPID(), 
		//				Config.getMiidi_Gold_APPSEC(), 
		//				Config.getTESTMODE());
		//		net.miidi.credit.MiidiCredit.setPushAdIcon(iconid);

		/**
		 * 2.x 接口
		 */
		if ( !AdsManager.getMiidiStatus() ) {
			AdsManager.initMiidi(content);
		}
		net.miidi.credit.MiidiCredit.setOffersListener(new MiidiAgent(this));
		net.miidi.credit.MiidiCredit.setPushAdIcon(iconid);
	}

	private void showOfferMIIDI(Context content) {
		/**
		 * 1.x 接口
		 */
		//		net.miidi.credit.MiidiCredit.showAppOffers(content);
		/**
		 * 2.x 接口
		 */
		net.miidi.credit.MiidiCredit.showAppOffers();
	}

	private void refreshMIIDI(Context content, boolean forceNotify) {

		/**
		 * 1.x 接口
		 */
		//		int tmp = net.miidi.credit.MiidiCredit.getPoints(content);
		//		if ( golds != tmp ) {
		//			golds = tmp;
		//			gn.notifyUpdate(golds);
		//		} else if ( forceNotify ) {
		//			gn.notifyUpdate(golds);
		//	}
		/**
		 * 2.x 接口
		 */
		net.miidi.credit.MiidiCredit.getPoints();
	}

	private void spendGoldMIIDI(Context content, 
			int amount) {

		/**
		 * 1.x 接口
		 */
		//		refreshMIIDI(content, false);
		//
		//		if ( golds < amount ) {
		//			gn.notifyFailed("miidi gold not enough");
		//			return;
		//		}
		//
		//		boolean res = net.miidi.credit.MiidiCredit.spendPoints(content, amount);
		//		if ( res ) {
		//			golds -= amount;
		//			gn.notifyUpdate(golds);
		//		} else {
		//			gn.notifyFailed("miidi gold spend failed");
		//		}
		
		/**
		 * 2.x 接口
		 */
		if ( golds < amount ) {
			gn.notifyFailed("miidi gold not enough");
			return;
		}
		
		net.miidi.credit.MiidiCredit.spendPoints(amount);
	}
	
	private void awardGoldMIIDI(int amount) {

		if ( amount < 0 ) {
			gn.notifyFailed("award must more then 0");
			return;
		}

		net.miidi.credit.MiidiCredit.awardPoints(amount);
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
