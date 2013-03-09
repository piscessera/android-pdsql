package com.piscessera.pdsql.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class DebugUtil {
	private static final String TAG = "PDSQL";

	public static void d(String msg) {
		Log.d(TAG, msg + " with " + System.currentTimeMillis());
	}

	public static void i(String msg) {
		Log.i(TAG, msg + " with " + System.currentTimeMillis());
	}

	public static void w(String msg) {
		Log.w(TAG, msg + " with " + System.currentTimeMillis());
	}

	public static void e(String msg) {
		Log.e(TAG, msg + " with " + System.currentTimeMillis());
	}

	public static void t(Context mContext, String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}
}
