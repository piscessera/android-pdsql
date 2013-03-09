package com.piscessera.pdsql.utils;

import android.content.Context;

public class ApplicationUtil {
	public static int getAppVersionCode(Context ctx) {
		int result = -1;

		try {
			String pkg = ctx.getPackageName();
			result = ctx.getPackageManager().getPackageInfo(pkg, 0).versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String getAppVersionName(Context ctx) {
		String result = "?";

		try {
			String pkg = ctx.getPackageName();
			result = ctx.getPackageManager().getPackageInfo(pkg, 0).versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
