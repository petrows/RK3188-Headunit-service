package ws.petro.rk3188.headunit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;

public class SWCReceiver extends BroadcastReceiver {
	Context ctx;
	private final static String TAG = "SWCReceiver";
	public SWCReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		ctx = context;
		if (!Settings.get(ctx).getMediaKeysEnable())
		{
			Log.d(TAG, "SWC keys are disabled");
			return;
		}

		if (intent.getAction().equals(Settings.C200ActionNext)) {
			sendKeyNow(KeyEvent.KEYCODE_MEDIA_NEXT);
			Settings.get(context).showToast(">>");
		} else if (intent.getAction().equals(Settings.C200ActionPrev)) {
			sendKeyNow(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
			Settings.get(context).showToast("<<");
		} else if (intent.getAction().equals(Settings.C200ActionPlayPause)) {
			sendKeyNow(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
			Settings.get(context).showToast("||");
		}
	}

	public void sendKeyNow(int keycode) {
		Log.d(TAG, "Send key " + keycode);
		long eventtime = SystemClock.uptimeMillis();
		Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
		KeyEvent downEvent = new KeyEvent(eventtime, eventtime,
				KeyEvent.ACTION_DOWN, keycode, 0);
		downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
		ctx.sendBroadcast(downIntent);
		Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
		KeyEvent upEvent = new KeyEvent(eventtime, eventtime,
				KeyEvent.ACTION_UP, keycode, 0);
		upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent);
		ctx.sendBroadcast(upIntent);
	}
}
