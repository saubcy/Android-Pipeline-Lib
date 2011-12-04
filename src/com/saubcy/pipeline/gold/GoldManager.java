package com.saubcy.pipeline.gold;

import net.miidi.credit.MiidiCredit;
import android.app.Activity;

import com.saubcy.conf.Config;
import com.tapjoy.TapjoyConnect;
import com.tapjoy.TapjoyLog;
import com.waps.AppConnect;
import com.wiyun.offer.WiOffer;
import com.wiyun.offer.WiOfferClient;

public class GoldManager implements InnerNotifier{

	private GoldNotifier gn = null;
	private Offers offerType = Offers.NONE;

	public enum Offers {
		NONE, YOUMI, WIYUN, WAPS, TAPJOY, MIIDI
	};

	private int golds = 0;

	private boolean isYoumiInit = false;
	private boolean isWiyunInit = false;
	private boolean isTapjoyInit = false;

	public GoldManager() {

	}

	public int getGold() {
		return golds;
	}

	public void init(Offers offer, 
			Activity content) {
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
		}
	}

	public void showOffer(Offers offer, 
			Activity content) {
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
		}
	}

	public void refreshGold(Offers offer, 
			Activity content) {
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
		}
	}

	private void initYOUMI(Activity content) {

		net.youmi.android.appoffers.AppOffersManager.init(content, 
				Config.getYoumi_Gold_APPID(), 
				Config.getYoumi_Gold_APPSEC(), 
				Config.getTESTMODE());
		isYoumiInit = true;
	}

	private void showOfferYOUMI(Activity content) {

		net.youmi.android.appoffers.AppOffersManager.showAppOffers(content);

	}

	private void refreshYOUMI(Activity content, boolean forceNotify) {

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

	private void spendGoldYOUMI(Activity content, 
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

	private void initWIYUN(Activity content) {

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

	private void showOfferWAPS(Activity content) {
		AppConnect.getInstance(content).showOffers(content);
	}

	private void refreshWAPS(Activity content) {
		AppConnect.getInstance(content).getPoints(new WapsAgent(this));
	}

	private void spendGoldWAPS(Activity content, int amount) {

		refreshWAPS(content);

		AppConnect.getInstance(content)
		.spendPoints(amount, new WapsAgent(this));
	}

	private void initTAPJOY(Activity content) {

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

	private void initMIIDI(Activity content) {

		MiidiCredit.init(content, 
				Config.getMiidi_APPID(), 
				Config.getMiidi_APPSEC(), 
				Config.getTESTMODE());
	}

	private void showOfferMIIDI(Activity content) {

		MiidiCredit.showAppOffers(content);

	}

	private void refreshMIIDI(Activity content, boolean forceNotify) {

		int tmp = MiidiCredit.getPoints(content);
		if ( golds != tmp ) {
			golds = tmp;
			gn.notifyUpdate(golds);
		} else if ( forceNotify ) {
			gn.notifyUpdate(golds);
		}
	}

	private void spendGoldMIIDI(Activity content, 
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

	@Override
	public void notifyUpdate(String currencyName, int num) {
		int delta = 0;
		switch (offerType) {
		case TAPJOY:
			delta = num - golds;
			golds = num;
			gn.notifyUpdate(delta);
			break;
		case WAPS:
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
