package ws.petro.rk3188.headunit;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Settings {
	private final static String TAG = "Settings";

	private static Settings instance = null;
	private static SharedPreferences prefs;

	private Context ctx;

	private Settings(Context context) {
		ctx = context;
		prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

		Log.d(TAG, "Settings created");
	}

	public static Settings get(Context context) {
		if (null == instance)
			instance = new Settings(context);
		return (instance);
	}

	public static void destroy() {
		instance = null;
	}

	private void setCfgBool(String name, boolean val) {
		Editor editor = prefs.edit();
		editor.putBoolean(name, val);
		editor.apply();
	}

	private void setCfgString(String name, String val) {
		Editor editor = prefs.edit();
		editor.putString(name, val);
		editor.apply();
	}

	public void startMyServices() {
		if (getServiceEnable()) {
			if (!ServiceMain.isRunning) {
				Log.d(TAG, "Starting service!");
				ctx.startService(new Intent(ctx, ServiceMain.class));
			}
		} else {
			ctx.stopService(new Intent(ctx, ServiceMain.class));
		}
	}

	public String getVersion() {
		String version = "?";
		try {
			PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(
					ctx.getPackageName(), 0);
			version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	public static String getPropValue(String value) {
		Process p;
		String ret = "";
		try {
			p = new ProcessBuilder("/system/bin/getprop", value).redirectErrorStream(true).start();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				ret = line;
			}
			p.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static boolean getServiceEnable() {
		return prefs.getBoolean("service.enable", true);
	}

	public void setServiceEnable(boolean enable) {
		setCfgBool("service.enable", enable);
	}

	public static boolean getServiceToast() {
		return prefs.getBoolean("service.toast", true);
	}

	public void showToast(String text) {
		showToast(text, Toast.LENGTH_SHORT);
	}

	public void showToast(String text, int length) {
		Log.d(TAG, "Toast: " + text);
		if (getServiceToast())
			Toast.makeText(ctx, text, length).show();
	}
}
