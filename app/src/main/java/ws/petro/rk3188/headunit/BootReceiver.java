package ws.petro.rk3188.headunit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	private final static String TAG = "BootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// Check out this
		Log.d(TAG, "Starting services");
		Settings.get(context).startMyServices();
	}
}
