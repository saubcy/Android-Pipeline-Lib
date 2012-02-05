package com.saubcy.pipeline.share;

import android.app.Activity;
import android.content.Intent;

public class ShareManager {

	public enum Offers {
		NONE, SELF, UMENG,
	};

	public enum Targets {
		CHOOSE, SINA, TENCENT, RENREN, FACEBOOK, TWITTER,
	}

	public static void share(Activity content, 
			Targets target, String msg) {
		switch (target) {
		case CHOOSE:
			shareByChoose(content, msg);
			break;
		}
	}

	private static void shareByChoose(Activity content, 
			String msg) {
		Intent intent = 
				new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, msg);
		content.startActivity(Intent.createChooser(intent, 
				content.getTitle()));
	}
}
