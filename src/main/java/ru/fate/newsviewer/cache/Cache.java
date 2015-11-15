package ru.fate.newsviewer.cache;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.nostra13.universalimageloader.utils.L;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.fate.newsviewer.Article;
import ru.fate.newsviewer.db.DbDao;

/**
 * Created by i4484 on 15.11.2015.
 */
public class Cache {

    private Cache() {
    }

    private static volatile Cache instance;
    private static Context context;
    private static DbDao dbDao;

    public static Cache getInstance() {
        if (instance == null) {
            Class var0 = Cache.class;
            synchronized (Cache.class) {
                if (instance == null) {
                    instance = new Cache();
                }
            }
        }

        return instance;
    }

    public synchronized void init(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Cache configuration can not be initialized with null");
        } else {
            if (this.context == null) {
                L.d("Initialize Cache");
                this.context = context.getApplicationContext();
                dbDao = new DbDao(this.context);
                dbDao.open();
            } else {
                L.w("Try to initialize Cache which had already been initialized before.");
            }

        }
    }

    public static Article getArticleByURL(String url) {
        Cursor cursor = dbDao.getArticleByArticleURL(url);
        Article article = null;
        while (cursor.moveToNext()) {
            String description = cursor.getString(cursor.getColumnIndex(DbDao.TABLE_ARTICLE_COLUMN_DESCRIPTION));
            String title = cursor.getString(cursor.getColumnIndex(DbDao.TABLE_ARTICLE_COLUMN_TITLE));

            Date publicationDate = null;

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = cursor.getString(cursor.getColumnIndex(DbDao.TABLE_ARTICLE_COLUMN_PUB_DATE));

            try {
                publicationDate = format.parse(dateTime);
            } catch (ParseException e) {
                Log.d("1", "Parsing ISO8601 datetime failed");
            }
            String content = cursor.getString(cursor.getColumnIndex(DbDao.TABLE_ARTICLE_COLUMN_CONTENT));
            String imageURL = cursor.getString(cursor.getColumnIndex(DbDao.TABLE_ARTICLE_COLUMN_IMAGE_URL));
            String articleURL = cursor.getString(cursor.getColumnIndex(DbDao.TABLE_ARTICLE_COLUMN_ARTICLE_URL));

            article = new Article(description, title, publicationDate, content, imageURL, articleURL);
            break;
        }
        cursor.close();

        return article;
    }

    public static void removeArticleByURL(String url) {
    }

    public static void addArticle(Article article) {
        dbDao.addArticle(article.getDescription(), article.getTitle(), article.getPubDate(),
                article.getContent(), article.getImageURL(), article.getArticleURL());
    }
}
