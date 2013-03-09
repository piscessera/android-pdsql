package com.piscessera.pdsql;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.database.Cursor;

public class DatabaseCursorUtil<T> extends GenericBase<T> {

	public DatabaseCursorUtil(String className) {
		super(className);
	}

	public T getValueByCursor(Cursor c) {
		try {
			// create instance
			T d = newInstance();

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
		}
		return null;
	}

	public String[] getArrayByCursor(Cursor c) {
		T d = newInstance();
		Field[] fields = d.getClass().getFields();
		String[] result = new String[fields.length];

		int index = 0;
		for (Field f : fields) {
			// set result array value
			if (int.class == f.getType()) {
				result[index] = String.valueOf(getInt(c, f.getName()));
			} else if (float.class == f.getType()) {
				result[index] = String.valueOf(getFloat(c, f.getName()));
			} else if (double.class == f.getType()) {
				result[index] = String.valueOf(getDouble(c, f.getName()));
			} else if (String.class == f.getType()) {
				result[index] = String.valueOf(getString(c, f.getName()));
			}
			index++;
		}
		return result;
	}

	public String[] getColumnByCursor(Cursor c) {
		T d = newInstance();
		Field[] fields = d.getClass().getFields();
		String[] result = new String[fields.length];

		int index = 0;
		for (Field f : fields) {
			// set result array value
			result[index] = f.getName();
			index++;
		}

		return result;
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
