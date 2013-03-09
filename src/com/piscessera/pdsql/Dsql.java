package com.piscessera.pdsql;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.piscessera.pdsql.utils.DebugUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Dsql<T> extends DatabaseCore {
	private static final String SELECT_ALL = "SELECT * FROM TBL_NAME ";

	private DatabaseCursorUtil<T> cUtil;
	private DatabaseContentValuesUtil<T> cvUtil;

	public Dsql(Context context, String dbCorePath, String dbCoreName,
			String dbCoreVersion, int version) {
		super(context, dbCorePath, dbCoreName, dbCoreVersion, version);

		cUtil = new DatabaseCursorUtil<T>(getClassName());
		cvUtil = new DatabaseContentValuesUtil<T>();
	}

	public List<T> getAll() {
		List<T> result = new ArrayList<T>();
		openDatabase();
		SQLiteDatabase db = getmDb();
		Cursor c = db.rawQuery(getSQLAll(), null);
		while (c.moveToNext()) {
			result.add(cUtil.getValueByCursor(c));
		}
		c.close();
		db.close();
		return result;
	}

	// public long insert(Account data) {
	// openDatabase();
	// SQLiteDatabase db = getmDb();
	// ContentValues cv = getContentValues(data);
	// long insertedId = db.insert(TBL_NAME, null, cv);
	// db.close();
	// return insertedId;
	// }
	//
	// public int update(Account data) {
	// openDatabase();
	// SQLiteDatabase db = getmDb();
	// ContentValues cv = getContentValues(data);
	// String whereClause = Account._ID + " = ?";
	// String[] whereArgs = { String.valueOf(data.get_id()) };
	// int affectedRow = db.update(TBL_NAME, cv, whereClause, whereArgs);
	// db.close();
	// return affectedRow;
	// }
	//
	// public int delete(Account data) {
	// openDatabase();
	// SQLiteDatabase db = getmDb();
	// String whereClause = Account._ID + " = ?";
	// String[] whereArgs = { String.valueOf(data.get_id()) };
	// int affectedRow = db.delete(TBL_NAME, whereClause, whereArgs);
	// db.close();
	// return affectedRow;
	// }
	//
	// public List<Account> getAll() {
	// List<Account> result = new ArrayList<Account>();
	// openDatabase();
	// SQLiteDatabase db = getmDb();
	// Cursor c = db.rawQuery(getSQLAll(), null);
	// while (c.moveToNext()) {
	// result.add(getValueByCursor(c));
	// }
	// c.close();
	// db.close();
	// return result;
	// }
	//
	// public List<Account> getAllWithOutCredit() {
	// List<Account> result = new ArrayList<Account>();
	// openDatabase();
	// SQLiteDatabase db = getmDb();
	// Cursor c = db.rawQuery(getSQLAll() + " WHERE account_category_id <> 2",
	// null);
	// while (c.moveToNext()) {
	// result.add(getValueByCursor(c));
	// }
	// c.close();
	// db.close();
	// return result;
	// }
	//
	// public List<Account> getAllCredit() {
	// List<Account> result = new ArrayList<Account>();
	// openDatabase();
	// SQLiteDatabase db = getmDb();
	// Cursor c = db.rawQuery(getSQLAll() + " WHERE account_category_id = 2",
	// null);
	// while (c.moveToNext()) {
	// result.add(getValueByCursor(c));
	// }
	// c.close();
	// db.close();
	// return result;
	// }
	//
	// public Cursor getCursorAll() {
	// openDatabase();
	// SQLiteDatabase db = getmDb();
	// Cursor c = db.rawQuery(getSQLAll(), null);
	// return c;
	// }
	//
	// public Account getById(int id) {
	// Account result = null;
	// openDatabase();
	// SQLiteDatabase db = getmDb();
	// Cursor c = db.rawQuery(getSQLAll() + " WHERE _id = " + id, null);
	// while (c.moveToNext()) {
	// result = getValueByCursor(c);
	// }
	// c.close();
	// db.close();
	// return result;
	// }
	//
	// private Account getValueByCursor(Cursor c) {
	// Account data = new Account();
	// data.set_id(getInt(c, Account._ID));
	// data.setAccountCategoryId(getInt(c, Account.ACCOUNT_CATEGORY_ID));
	// data.setDescription(getString(c, Account.DESCRIPTION));
	// data.setName(getString(c, Account.NAME));
	// data.setStartBalance(getDouble(c, Account.START_BALANCE));
	// return data;
	// }
	//
	// public String[] getArrayByCursor(Cursor c) {
	// String result[] = { String.valueOf(getInt(c, Account._ID)),
	// String.valueOf(getInt(c, Account.ACCOUNT_CATEGORY_ID)),
	// getString(c, Account.DESCRIPTION), getString(c, Account.NAME),
	// String.valueOf(getDouble(c, Account.START_BALANCE)) };
	// return result;
	// }
	//
	// public void insertByArray(String[] array) {
	// Account data = new Account();
	// data.set_id(Integer.parseInt(array[0]));
	// data.setAccountCategoryId(Integer.parseInt(array[1]));
	// data.setDescription(array[2]);
	// data.setName(array[3]);
	// data.setStartBalance(Double.parseDouble(array[4]));
	//
	// openDatabase();
	// SQLiteDatabase db = getmDb();
	// ContentValues cv = new ContentValues();
	// cv.put(Account._ID, data.get_id());
	// cv.put(Account.ACCOUNT_CATEGORY_ID, data.getAccountCategoryId());
	// cv.put(Account.NAME, data.getName());
	// cv.put(Account.DESCRIPTION, data.getDescription());
	// cv.put(Account.START_BALANCE, data.getStartBalance());
	// long insertedId = db.insert(TBL_NAME, null, cv);
	// db.close();
	// }

	private String getSQLAll() {
		return SELECT_ALL.replace("TBL_NAME", getTableName().toLowerCase());
	}

	private String getTableName() {
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) genericSuperclass;
			Type type = pt.getActualTypeArguments()[0];
			String[] pkgName = type.toString().replace(".", "-").split("-");
			return pkgName[pkgName.length-1];
		}
		return null;
	}
	
	private String getClassName(){
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) genericSuperclass;
			Type type = pt.getActualTypeArguments()[0];
			String[] pkgName = type.toString().split(" ");
			return pkgName[pkgName.length-1];
		}
		return null;
	}

	// public void deleteAll() {
	// openDatabase();
	// SQLiteDatabase db = getmDb();
	// db.execSQL("DELETE FROM " + TBL_NAME);
	// db.close();
	// }

}
