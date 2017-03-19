package com.example.mrphonglinh.ad65_reminder.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mrphonglinh.ad65_reminder.model.Reminder;

import java.util.ArrayList;

/**
 * Created by MyPC on 19/03/2017.
 */

public class ReminderDBAdapter {
    //Tên cột
    public static final String KEY_ID = "id";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_IMPORTANT = "important";

    //Các chỉ số tương ứng
    public static final int INDEX_ID = 0;
    public static final int INDEX_CONTENT = INDEX_ID + 1;
    public static final int INDEX_IMPORTANT = INDEX_ID + 2;

    //Tên database
    public static final String DATABASE_NAME = "db_reminder";
    public static final String TABLE_REMINDER = "tb_reminder";
    public static final int DATABASE_VERSION = 1;

    //Đối tượng DatabaseHelper
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Context mContext;
    //Câu truy vấn tạo database
    private static final String CREATE_REMINDER = "CREATE TABLE " + TABLE_REMINDER + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_CONTENT + " TEXT," +
            KEY_IMPORTANT + " INTEGER);";

    //Contructor
    public ReminderDBAdapter(Context mContext) {
        this.mContext = mContext;
    }

    //open
    public void open() throws SQLiteException{
        databaseHelper = new DatabaseHelper(mContext);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
    }

    //close
    public void close(){
        if (databaseHelper != null){
            databaseHelper.close();
        }
    }

    //CRUD
    //Hàm tạo 1 reminder
    public void createReminder(String name, boolean important){
        ContentValues values = new ContentValues();
        values.put(KEY_CONTENT,name);
        values.put(KEY_IMPORTANT,important?1:0);
        sqLiteDatabase.insert(TABLE_REMINDER,null,values);
    }

    //Hàm thêm 1 reminder
    public long createReminder(Reminder reminder){
        ContentValues values = new ContentValues();
        values.put(KEY_CONTENT,reminder.getContent());
        values.put(KEY_IMPORTANT,reminder.getImportant());

        return sqLiteDatabase.insert(TABLE_REMINDER,null,values);
    }

    //READ
    //Đọc 1 remind theo id
    public Reminder fetchRemindById(int id){
        Reminder reminder = new Reminder();
        String sql = "SELECT * FROM " + TABLE_REMINDER + " WHERE " + KEY_ID + " = " + id;
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                String content = cursor.getString(cursor.getColumnIndex(KEY_CONTENT));
                int important = cursor.getInt(cursor.getColumnIndex(KEY_IMPORTANT));
                reminder.setId(id);
                reminder.setContent(content);
                reminder.setImportant(important);

                cursor.moveToNext();
            }
        }
        return reminder;
    }

    //Đọc toàn bộ remind
    public ArrayList<Reminder> fetchAllReminders(){
        ArrayList<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_REMINDER;
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String content = cursor.getString(cursor.getColumnIndex(KEY_CONTENT));
                int important = cursor.getInt(cursor.getColumnIndex(KEY_IMPORTANT));

                reminders.add(new Reminder(id,content,important));

                cursor.moveToNext();
            }
        }
        return reminders;
    }

    //UPDATE
    //Update 1 remind
    public void updateReminder(Reminder reminder){
        ContentValues values = new ContentValues();
        values.put(KEY_CONTENT,reminder.getContent());
        values.put(KEY_IMPORTANT,reminder.getImportant());

        sqLiteDatabase.update(TABLE_REMINDER,values,KEY_ID + " =?",
                new String[]{String.valueOf(reminder.getId())});
    }

    //DELETE
    //Delete 1 reminder
    public void deleteReminderById(int id){
        sqLiteDatabase.delete(TABLE_REMINDER,KEY_ID + " =? ",
                new String[]{String.valueOf(id)});
    }

    //Delete all reminder
    public void deleteAllReminder(){
        sqLiteDatabase.delete(TABLE_REMINDER,null,null);
    }

    /*
            Class DatabaseHelper là 1 class SQLite API cung cấp chuyên để đóng và
            mở cơ sở dữ liệu.
            Nó sử dụng 1 context là lớp trừu tượng cung cấp quyền truy cập vào hệ điều hành android
            . DatabaseHelper là lớp tùy chỉnh và phải được developer xác định.
            - DatabaseHelper kế thừa SQLiteOpenHelper giúp duy trì cơ sở dữ liệu bằng phương thức đặc
            biệt gọi là phương thức callback.
            - Hàm callback là hàm sẽ được gọi trong suốt vòng đơi của ứng dụng, và chúng sử dụng biến
            SQLiteDatabase db để thực thi câu lện truy vấn SQL. Constructor là nơi cơ sở dữ liệu đc
            khởi tạo
            - Constructor truyền tên database và phiên bản của nó sang class cha của nó, sau đó lớp cha
             sẽ thực thi cài đặt database.

        */
    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /*Hàm này đc gọi 1 cách tự động bởi hệ thống khi nó cần tạo cơ sở dữ liệu
        * - Hàm onCreate chỉ chạy 1 lần khi ứng dụng chạy lần đầu tiên và databas chưa
        * được khởi tạo
        * - Hàm onUpgrade được gọi bất cứ khi nào mà database được cập nhật. Khi db cập nhật
        * thì DATABASE_VERSION phải tăng lên 1 */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_REMINDER);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
            onCreate(db);
        }
    }

}
