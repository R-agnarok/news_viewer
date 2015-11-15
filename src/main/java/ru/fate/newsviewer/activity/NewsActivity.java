package ru.fate.newsviewer.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.fate.newsviewer.Article;
import ru.fate.newsviewer.R;
import ru.fate.newsviewer.cache.Cache;
import ru.fate.newsviewer.html.HtmlParser;
import ru.fate.newsviewer.rss.RssItem;

public class NewsActivity extends AppCompatActivity {

    private static RssItem selectedRssItem;

    private ParseHtmlTask parseHtmlTask = null;

    TextView mTitle = null;
    TextView mDescription = null;
    ImageView mImage = null;
    TextView mDate = null;
    WebView mArticle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        selectedRssItem = MainActivity.selectedRssItem;

        mTitle = (TextView) findViewById(R.id.title);
        mDescription = (TextView) findViewById(R.id.description);
        mImage = (ImageView) findViewById(R.id.image);
        mDate = (TextView) findViewById(R.id.datePub);
        mArticle = (WebView) findViewById(R.id.article);

        mTitle.setText(selectedRssItem.getTitle());
        mDescription.setText(selectedRssItem.getDescription());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        mDate.setText(dateFormat.format(selectedRssItem.getPublicationDate()));

        ImageLoader.getInstance().displayImage(selectedRssItem.getImageUrl(), mImage);

        parseHtmlTask = new ParseHtmlTask();
        parseHtmlTask.execute(selectedRssItem.getArticleUrl());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void refreshArticle(String article) {
        if (article == null) return;
        mArticle.loadDataWithBaseURL(null, article, "text/html", "UTF-8", null);
    }

    public class ParseHtmlTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            try {
                if (urls[0].equals("")) return null;

                Article article = Cache.getArticleByURL(urls[0]);
                if (article != null)
                    return article.getContent();

                String content = HtmlParser.parse(urls[0]);

                String description = selectedRssItem.getDescription();
                String title = selectedRssItem.getTitle();
                Date publicationDate = selectedRssItem.getPublicationDate();
                String imageURL = selectedRssItem.getImageUrl();
                String articleURL = selectedRssItem.getArticleUrl();

                Cache.addArticle(new Article(description, title, publicationDate, content, imageURL, articleURL));

                return content;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            refreshArticle(result);
        }
    }

}
