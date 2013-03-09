package com.piscessera.pdsql;


public class GenericBase<T> {
	private String className;

	public GenericBase(String className) {
		this.className = className;
	}

	@SuppressWarnings("unchecked")
	protected T newInstance() {
		try {
			// Create class
			Class<? extends T> cls = (Class<? extends T>) Class
					.forName(this.className);
			// Create instance
			T d = cls.newInstance();
			return d;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
