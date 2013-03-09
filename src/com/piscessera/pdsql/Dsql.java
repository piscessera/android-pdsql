package com.piscessera.pdsql;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Dsql<T> extends DatabaseCore {
	private static final String SELECT_ALL = "SELECT * FROM TBL_NAME ";
	private static final String SELECT_BY_PK = "SELECT * FROM TBL_NAME WHERE _id = ?";
	private static final String DELETE_ALL = "DELETE FROM TBL_NAME";
	private static final String WHERE_BY_PK = " _id = ? ";

	private DatabaseCursorUtil<T> cUtil;
	private DatabaseContentValuesUtil<T> cvUtil;

	/**
	 * @param context
	 * @param dbCorePath
	 *            example: /data/data/com.piscessera.example/databases/
	 * @param dbCoreName
	 *            example: example_db.sqlite
	 * @param dbCoreVersion
	 *            example: example_db_version
	 * @param version
	 *            this value at least 1
	 */
	public Dsql(Context context, String dbCorePath, String dbCoreName,
			String dbCoreVersion, int version) {
		super(context, dbCorePath, dbCoreName, dbCoreVersion, version);

		cUtil = new DatabaseCursorUtil<T>(getClassName());
		cvUtil = new DatabaseContentValuesUtil<T>(getClassName());
	}

	/**
	 * SELECT * FROM TABLE_NAME
	 * 
	 * @return a list of class instance with a database record value
	 */
	public List<T> getAll() {
		List<T> result = new ArrayList<T>();
		openDatabase();
		SQLiteDatabase db = getmDb();
		Cursor c = db.rawQuery(getSQL(SELECT_ALL), null);
		while (c.moveToNext()) {
			result.add(cUtil.getValueByCursor(c));
		}
		c.close();
		db.close();
		return result;
	}

	/**
	 * SELECT * FROM TABLE_NAME WHERE _id = ?
	 * 
	 * @param _id
	 *            is a primary key of record which return a value into a class
	 *            instance
	 * @return a class instance with a database record value
	 */
	public T getByPk(String _id) {
		T result = null;
		openDatabase();
		SQLiteDatabase db = getmDb();
		Cursor c = db.rawQuery(getSQL(SELECT_BY_PK), new String[] { _id });
		if (c.moveToNext()) {
			result = cUtil.getValueByCursor(c);
		}
		c.close();
		db.close();
		return result;
	}

	/**
	 * SELECT * FROM TABLE_NAME WHERE _id = ?
	 * 
	 * @param _id
	 *            is a primary key of record which return a value into a class
	 *            instance
	 * @return a class instance with a database record value
	 */
	public T getByPk(int _id) {
		return getByPk(String.valueOf(_id));
	}

	/**
	 * SELECT * FROM TABLE_NAME
	 * 
	 * @return a cursor of database records
	 */
	public Cursor getCursorAll() {
		openDatabase();
		SQLiteDatabase db = getmDb();
		Cursor c = db.rawQuery(getSQL(SELECT_ALL), null);
		return c;
	}

	public void deleteAll() {
		openDatabase();
		SQLiteDatabase db = getmDb();
		db.execSQL(DELETE_ALL);
		db.close();
	}

	public int deleteByPk(String _id) {
		openDatabase();
		SQLiteDatabase db = getmDb();
		int affectedRow = db.delete(getTableName(), WHERE_BY_PK,
				new String[] { _id });
		db.close();
		return affectedRow;
	}

	public int deleteByPk(int _id) {
		return deleteByPk(String.valueOf(_id));
	}

	public long insert(T d) {
		openDatabase();
		SQLiteDatabase db = getmDb();
		ContentValues cv = cvUtil.getContentValues(d);
		long insertedId = db.insert(getTableName(), null, cv);
		db.close();
		return insertedId;
	}

	public int insertByArray(String[] row) {
		T d = cvUtil.newInstance();

		try {
			// loop all variable in table class
			int index = 0;
			for (Field f : d.getClass().getDeclaredFields()) {
				// set accessible
				f.setAccessible(true);

				// set first character is UpperCase
				char[] nameCharArray = f.getName().toCharArray();
				nameCharArray[0] = Character.toUpperCase(nameCharArray[0]);
				String methodName = new String(nameCharArray);

				// create getter/setter method name
				String setMethodName = "set" + methodName;

				// create method
				Method setterMethod = d.getClass().getMethod(setMethodName,
						f.getType());

				// put field value into ContentValues
				if (int.class == f.getType()) {
					setterMethod.invoke(d, Integer.parseInt(row[index]));
				} else if (float.class == f.getType()) {
					setterMethod.invoke(d, Float.parseFloat(row[index]));
				} else if (double.class == f.getType()) {
					setterMethod.invoke(d, Double.parseDouble(row[index]));
				} else if (String.class == f.getType()) {
					setterMethod.invoke(d, String.valueOf(row[index]));
				}
				index++;
			}

			openDatabase();
			SQLiteDatabase db = getmDb();
			ContentValues cv = cvUtil.getContentValues(d);
			db.insert(getTableName(), null, cv);
			db.close();

			return 1;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int update(T d) {
		try {
			String getMethodName = "get_id";

			openDatabase();
			SQLiteDatabase db = getmDb();
			ContentValues cv = cvUtil.getContentValues(d);

			String whereClause = "_id = ?";
			Method getterMethod = d.getClass().getMethod(getMethodName);
			String[] whereArgs = { String.valueOf(getterMethod.invoke(d,
					new Object[] {})) };

			int affectedRow = db.update(getTableName(), cv, whereClause,
					whereArgs);
			db.close();

			return affectedRow;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String[] getArrayByCursor(Cursor c) {
		return cUtil.getArrayByCursor(c);
	}

	public String[] getColumnByCursor(Cursor c) {
		return cUtil.getColumnByCursor(c);
	}

	private String getSQL(String sql) {
		return sql.replace("TBL_NAME", getTableName().toLowerCase());
	}

	private String getTableName() {
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) genericSuperclass;
			Type type = pt.getActualTypeArguments()[0];
			String[] pkgName = type.toString().replace(".", "-").split("-");
			return pkgName[pkgName.length - 1];
		}
		return null;
	}

	private String getClassName() {
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) genericSuperclass;
			Type type = pt.getActualTypeArguments()[0];
			String[] pkgName = type.toString().split(" ");
			return pkgName[pkgName.length - 1];
		}
		return null;
	}

}
