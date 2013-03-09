package com.piscessera.pdsql;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.ContentValues;

public class DatabaseContentValuesUtil<T> extends GenericBase<T> {

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

				// create field type class for casting object after invoke
				String typeClassName = field.getType().toString();

				// create method
				Method getterMethod = d.getClass().getMethod(getMethodName);

				// put field value into ContentValues
				if (int.class == field.getType()) {
					int intVal = (Integer) getterMethod.invoke(d, null);

					// if _id is <= 0, pass to put this value into ContentValues
					if (_ID.equalsIgnoreCase(field.getName()) && intVal <= 0) {
						continue;
					}

					cv.put(field.getName(), intVal);
				} else if (float.class == field.getType()) {
					cv.put(field.getName(),
							(Float) getterMethod.invoke(d, null));
				} else if (double.class == field.getType()) {
					cv.put(field.getName(),
							(Double) getterMethod.invoke(d, null));
				} else if (String.class == field.getType()) {
					cv.put(field.getName(),
							(String) getterMethod.invoke(d, null));
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
}
