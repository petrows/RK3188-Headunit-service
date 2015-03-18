package ws.petro.rk3188.headunit;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

public class ServiceMain extends Service implements LocationListener {

	private final static String TAG = "ServiceMain";

	public static boolean isRunning = false;
	public double last_speed = 0;

	public SWCReceiver swc;

	@Override
	public IBinder onBind(Intent intent) {
		return (null);
	}

	@Override
	public void onCreate() {
		if (isRunning) return;

		isRunning = true;

		Log.d(TAG, "MTCService onCreate");
		Settings.get(this).startMyServices();
	}

	@Override
	public void onDestroy() {
		isRunning = false;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (Settings.getServiceEnable()) {
			Log.d(TAG, "MTCService onStartCommand");

			Settings.get(this).announce(this, startId);

			LocationManager locationManager = (LocationManager) this
					.getSystemService(Context.LOCATION_SERVICE);
			if (null != locationManager) {
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
			}

			IntentFilter intf = new IntentFilter();
			intf.addAction(Settings.C200ActionNext);
			intf.addAction(Settings.C200ActionPrev);
			intf.addAction(Settings.C200ActionPlayPause);
			swc = new SWCReceiver();
			registerReceiver(swc, intf);
			Log.d(TAG, "SWCReceiver registerReceiver");

			return (START_STICKY);
		}
		stopSelf();
		return (START_NOT_STICKY);
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}
}
