package com.piscessera.pdsql;


import java.io.IOException;
import java.io.InputStream;

import com.piscessera.pdsql.utils.ApplicationUtil;
import com.piscessera.pdsql.utils.DebugUtil;
import com.piscessera.pdsql.utils.FileUtil;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseCore extends SQLiteOpenHelper {
	// private static String DB_PATH =
	// "/data/data/com.piscessera.thetn/databases/";
	//
	// private static String DB_NAME = "thetn_db.sqlite";
	// private static String DB_VERSION = "thetn_db_version";

	private String dbCorePath;
	private String dbCoreName;
	private String dbCoreVersion;
	private int version;

	private final Context mContext;
	private SQLiteDatabase mDb;

	public DatabaseCore(Context context, String dbCorePath, String dbCoreName,
			String dbCoreVersion, int version) {
		super(context, dbCoreName, null, version);

		this.mContext = context;
		this.dbCoreName = dbCoreName;
		this.dbCorePath = dbCorePath;
		this.dbCoreVersion = dbCoreVersion;
		this.version = version;

		FileUtil fileUtil = new FileUtil(context);

		int newVersion = ApplicationUtil.getAppVersionCode(context);
		int oldVersion = -99;
		InputStream in = fileUtil.getContextFile(dbCoreVersion);

		if (in != null) {
			String oldVersionStr = fileUtil.convertIs2String(in);
			if (!"".equals(oldVersionStr)) {
				oldVersion = Integer.parseInt(oldVersionStr);
			}
		}

		try {
			if (oldVersion < newVersion) {
				deleteDatabase();
				createDatabase();
				fileUtil.saveContextFile(dbCoreVersion,
						String.valueOf(newVersion));
				DebugUtil.i("Create database success!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SQLiteDatabase getmDb() {
		return mDb;
	}

	private void createDatabase() throws IOException {
		boolean dbExist = checkDatabase();
		if (dbExist) {

		} else {
			this.getReadableDatabase();
			try {
				copyDatabase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	private boolean checkDatabase() {
		SQLiteDatabase checkDB = null;
		try {
			String dbPath = dbCorePath + dbCoreName;
			checkDB = SQLiteDatabase.openDatabase(dbPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (checkDB != null) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	private void copyDatabase() throws IOException {
		FileUtil fileUtil = new FileUtil(mContext);
		fileUtil.copyAssets(dbCoreName, dbCorePath);
		// fileUtil.copyAssets(mContext.getAssets(), DB_PATH);
		DebugUtil.i("Copy database success!");
	}

	private void deleteDatabase() throws IOException {
		FileUtil fileUtil = new FileUtil(mContext);
		fileUtil.deleteFileIfExist(dbCorePath + dbCoreName);
		DebugUtil.i("Delete database success!");
	}

	public void openDatabase() throws SQLException {
		String dbPath = dbCorePath + dbCoreName;
		mDb = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	@Override
	public synchronized void close() {
		if (mDb != null) {
			mDb.close();
		}
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
