package com.piscessera.pdsql;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.ContentValues;

public class DatabaseContentValuesUtil<T> extends GenericBase<T> {

	public DatabaseContentValuesUtil(String className) {
		super(className);
	}

	// TABLE ID FIELD
	private static final String _ID = "_id";

	public ContentValues getContentValues(T d) {
		try {
			ContentValues cv = new ContentValues();

			// loop all variable in table class
			for (Field field : d.getClass().getDeclaredFields()) {
				// set accessible
				field.setAccessible(true);

				// set first character is UpperCase
				char[] nameCharArray = field.getName().toCharArray();
				nameCharArray[0] = Character.toUpperCase(nameCharArray[0]);
				String methodName = new String(nameCharArray);

				// create getter/setter method name
				String getMethodName = "get" + methodName;

				// create method
				Method getterMethod = d.getClass().getMethod(getMethodName);

				// put field value into ContentValues
				if (int.class == field.getType()) {
					int intVal = (Integer) getterMethod.invoke(d,
							new Object[] {});

					// if _id is <= 0, pass to put this value into ContentValues
					if (_ID.equalsIgnoreCase(field.getName()) && intVal <= 0) {
						continue;
					}

					cv.put(field.getName(), intVal);
				} else if (float.class == field.getType()) {
					cv.put(field.getName(),
							(Float) getterMethod.invoke(d, new Object[] {}));
				} else if (double.class == field.getType()) {
					cv.put(field.getName(),
							(Double) getterMethod.invoke(d, new Object[] {}));
				} else if (String.class == field.getType()) {
					cv.put(field.getName(),
							(String) getterMethod.invoke(d, new Object[] {}));
				}
			}

			return cv;
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

	public ContentValues getContentValues(String[] row) {
		T d = newInstance();
		try {
			ContentValues cv = new ContentValues();

			// loop all variable in table class
			int index = 0;
			for (Field f : d.getClass().getDeclaredFields()) {
				// set accessible
				f.setAccessible(true);

				// put field value into ContentValues
				if (int.class == f.getType()) {
					cv.put(f.getName(), Integer.parseInt(row[index]));
				} else if (float.class == f.getType()) {
					cv.put(f.getName(), Float.parseFloat(row[index]));
				} else if (double.class == f.getType()) {
					cv.put(f.getName(), Double.parseDouble(row[index]));
				} else if (String.class == f.getType()) {
					cv.put(f.getName(), (String) row[index]);
				}
				index++;
			}

			return cv;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}
}
