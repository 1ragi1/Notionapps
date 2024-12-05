package com.example.notioapps;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "memos.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE memos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "content TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS memos");
        onCreate(db);
    }

    // メモを全て取得するメソッド
    public List<ListItem> getAllMemos() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ListItem> memoList = new ArrayList<>();
        Cursor cursor = db.query("memos", new String[]{"id", "title", "content"}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String title = cursor.getString(1);
                memoList.add(new ListItem(id, title));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return memoList;
    }

    // メモを保存するメソッド
    public long saveMemo(String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // タイトルが空の場合、"untitled" をセット
        if (title == null || title.isEmpty()) {
            title = "untitled";
        }

        values.put("title", title);
        values.put("content", content);
        long id = db.insert("memos", null, values);
        db.close();
        return id;
    }

    public void deleteMemo(long memoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("memos", "id = ?", new String[]{String.valueOf(memoId)});
        db.close();
    }
}

