package com.piscessera.pdsql;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.piscessera.pdsql.utils.DebugUtil;

import android.database.Cursor;

public class DatabaseCursorUtil<T> extends GenericBase<T> {

	private String className;

	public DatabaseCursorUtil(String className) {
		super();
		this.className = className;
	}

	public T getValueByCursor(Cursor c) {
		try {
			// Create class
			Class<? extends T> cls = (Class<? extends T>) Class
					.forName(this.className);
			// Create instance
			T d = cls.newInstance();

			// loop all variable in table class
			for (Field f : d.getClass().getDeclaredFields()) {
				// set accessible
				f.setAccessible(true);

				// set first character is UpperCase
				char[] nameCharArray = f.getName().toCharArray();
				nameCharArray[0] = Character.toUpperCase(nameCharArray[0]);
				String methodName = new String(nameCharArray);

				// create getter/setter method name
				String setMethodName = "set" + methodName;

				// create field type class for casting object after invoke
				String typeClassName = f.getType().toString();

				// create method
				Method setterMethod = d.getClass().getMethod(setMethodName,
						f.getType());

				// put field value into ContentValues
				if (int.class == f.getType()) {
					setterMethod.invoke(d, getInt(c, f.getName()));
				} else if (float.class == f.getType()) {
					setterMethod.invoke(d, getFloat(c, f.getName()));
				} else if (double.class == f.getType()) {
					setterMethod.invoke(d, getDouble(c, f.getName()));
				} else if (String.class == f.getType()) {
					setterMethod.invoke(d, getString(c, f.getName()));
				}
			}

			return d;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getInt(Cursor c, String columnName) {
		return c.getInt(c.getColumnIndex(columnName));
	}

	public String getString(Cursor c, String columnName) {
		return c.getString(c.getColumnIndex(columnName));
	}

	public Double getDouble(Cursor c, String columnName) {
		return c.getDouble(c.getColumnIndex(columnName));
	}

	public Float getFloat(Cursor c, String columnName) {
		return c.getFloat(c.getColumnIndex(columnName));
	}
}
