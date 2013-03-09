PDSQL
=====

This is an android database library. It's include basic functional for CRUD. You can create database helper class to insert, update, delete, and select easier.

Getting Start
=============

1. Create sqlite database file and added into an android project assets folder
2. Create Domain Class which depend on table in database file

```java
public class Foo{
  private int _id;
	private String name;

	public void set_id(int _id){
		this._id = _id;
	}

	public void setName(String name){
		this.name = name;
	}

	public int get_id(){
		return this._id;
	}

	public String getName(){
		return this.name;
	}
}
```

Foo is table name
_id is primary key **PRIMARY KEY FIELD NAME MUST IS _id**
name is column in foo table
Getter and Setter is not follow the syntax rule but must to follow a column name

3. Create Database Helper class

```java
public class FooDBHelper extends Dsql<Foo> {
  private static String DB_PATH = "/data/data/<your package name>/databases/";

	private static String DB_NAME = "<your database file name>.sqlite";
	private static String DB_VERSION = "<your database file name>_version";

	public CategoryDBHelper(Context context) {
		super(context, DB_PATH, DB_NAME, DB_VERSION, ApplicationUtil
				.getAppVersionCode(context));
	}

}
```

ApplicationUtil.getAppVersionCode(context) is method to get an application code version (In androidmanifest.xml). You can change it to your fixed database version :)

4. Enjoy with your code XD

How to use
==========

```java
FooDBHelper fooDBHelper = new FooDBHelper(this);
for(Foo f:fooDBHelper.getAll()){
	Log.i("FooDBHelper", "ID: "+f.get_id()+", Name: "+f.getName());
}
```

PDSQL in Thai Language
======================

PDSQL คือ Android Database Library ซึ่งประกอบไปด้วย function พื้นฐานของ Database คือ Create, Read, Update, และ Delete เพื่อช่วยให้ผู้พัฒนาสามารถสร้าง Database Helper Class สำหรับใช้ในการ Insert, Update, Delete, และ Select ได้ง่ายขึ้น

เริ่มต้น
====

1. สร้างไฟล์ฐานข้อมูล sqlite จากโปรแกรมต่างๆ และนำมาใส่ไว้ใน assets folder ของ android project ที่กำลังพัฒนา
2. สร้าง Class โดยอิงข้อมูลจากตารางในฐานข้อมูลที่สร้างขึ้น

```java
public class Foo{
  private int _id;
  private String name;

	public void set_id(int _id){
		this._id = _id;
	}

	public void setName(String name){
		this.name = name;
	}

	public int get_id(){
		return this._id;
	}

	public String getName(){
		return this.name;
	}
}
```

Foo คือ ชื่อตาราง
_id คือ primary key **PRIMARY KEY ต้องใช้ชื่อว่า  _id เท่านั้น**
name คือชื่อของ column ในตาราง foo
Getter and Setter ไม่จำเป็นต้องอิงตามกฎของตัวแปร แต่ต้องอิงตามชื่อ column ที่ใช้ (สามารถใช้ Eclipse เพื่อ Generate Getter/Setter ได้เลย)

3. สร้างไฟล์ Database Helper class

```java
public class FooDBHelper extends Dsql<Foo> {
  private static String DB_PATH = "/data/data/<your package name>/databases/";

	private static String DB_NAME = "<your database file name>.sqlite";
	private static String DB_VERSION = "<your database file name>_version";

	public CategoryDBHelper(Context context) {
		super(context, DB_PATH, DB_NAME, DB_VERSION, ApplicationUtil
				.getAppVersionCode(context));
	}

}
```

ApplicationUtil.getAppVersionCode(context) คือ method เพื่อใช้ในการดึงข้อมูล application code version (สามารถดูได้ในไฟล์ androidmanifest.xml) โดยที่ผู้พัฒนาสามารถกำหนดเลข version ได้เองตามต้องการ

4. Enjoy with your code XD

วิธีใช้
====

```java
FooDBHelper fooDBHelper = new FooDBHelper(this);
for(Foo f:fooDBHelper.getAll()){
	Log.i("FooDBHelper", "ID: "+f.get_id()+", Name: "+f.getName());
}
```

Tasks
=====

- [x] Database core functional - Copy File for copying database file from assets folder into device
- [x] Database core functional - CursorUtil for converting a cursor to specific class
- [x] Database core functional - ContentValues Util for convert a specific class to ContentValues
- [x] Convert record to String array
- [x] Read all value (SELECT * FROM table_name)
- [x] Read value by primary key
- [x] Insert
- [x] Update
- [x] Delete
- [x] Delete All
- [ ] Read value by criteria
- [ ] Update value by criteria
