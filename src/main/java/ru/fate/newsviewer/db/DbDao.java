package ru.fate.newsviewer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by i4484 on 15.11.2015.
 */
public class DbDao {

    private static final String DATABASE_NAME = "news_viewer.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ARTICLE = "article";

    public static final String TABLE_ARTICLE_COLUMN_ID = "_id";
    public static final String TABLE_ARTICLE_COLUMN_DESCRIPTION = "description";
    public static final String TABLE_ARTICLE_COLUMN_TITLE = "title";
    public static final String TABLE_ARTICLE_COLUMN_PUB_DATE = "pub_date";
    public static final String TABLE_ARTICLE_COLUMN_CONTENT = "content";
    public static final String TABLE_ARTICLE_COLUMN_ARTICLE_URL = "article_url";
    public static final String TABLE_ARTICLE_COLUMN_IMAGE_URL = "image_url";


    private static final String ARTICLE_TABLE_CREATE = "create table "
            + TABLE_ARTICLE + " (" + TABLE_ARTICLE_COLUMN_ID
            + " integer primary key autoincrement, " + TABLE_ARTICLE_COLUMN_ARTICLE_URL + " text not null, "
            + TABLE_ARTICLE_COLUMN_DESCRIPTION + " text, "
            + TABLE_ARTICLE_COLUMN_TITLE + " text, "
            + TABLE_ARTICLE_COLUMN_CONTENT + " text, "
            + TABLE_ARTICLE_COLUMN_IMAGE_URL + " text, "
            + TABLE_ARTICLE_COLUMN_PUB_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

    private final Context mCtx;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DbDao(Context ctx) {
        mCtx = ctx;
    }

    public void open() {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    public void close() {
        if (mDBHelper != null) mDBHelper.close();
    }

    public Cursor getArticleByArticleURL(String articleURL) {
        String selectQuery = "SELECT * FROM " + TABLE_ARTICLE +
                " WHERE " + TABLE_ARTICLE_COLUMN_ARTICLE_URL + "=?";
        return mDB.rawQuery(selectQuery, new String[]{articleURL});
    }

    public long addArticle(String description, String title, Date publicationDate, String content, String imageURL, String articleURL) {
        ContentValues cv = new ContentValues();
        cv.put(TABLE_ARTICLE_COLUMN_ARTICLE_URL, articleURL);
        cv.put(TABLE_ARTICLE_COLUMN_DESCRIPTION, description);
        cv.put(TABLE_ARTICLE_COLUMN_TITLE, title);
        cv.put(TABLE_ARTICLE_COLUMN_CONTENT, content);
        cv.put(TABLE_ARTICLE_COLUMN_IMAGE_URL, imageURL);
        cv.put(TABLE_ARTICLE_COLUMN_PUB_DATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(publicationDate));

        return mDB.insert(TABLE_ARTICLE, null, cv);
    }

    public void clearDB() {
        mDB.execSQL("delete from " + TABLE_ARTICLE + ";");
    }

    public class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
            super(context, name, factory, version, errorHandler);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(ARTICLE_TABLE_CREATE);
            } catch (Exception e) {
                Log.d("1111", Log.getStackTraceString(e));
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF IT EXISTS " + TABLE_ARTICLE);
            onCreate(db);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }
    }
}
