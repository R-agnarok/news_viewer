package ru.fate.newsviewer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import ru.fate.newsviewer.R;
import ru.fate.newsviewer.rss.RssFetcher;
import ru.fate.newsviewer.rss.RssItem;

public class MainActivity extends AppCompatActivity {


    public static RssItem selectedRssItem = null;
    private ArrayList<RssItem> rssItems = new ArrayList<>();
    private RssListAdapter listAdapter = null;
    private RetrieveFeedTask task = null;
    private ListView mRssListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mRssListView = (ListView) findViewById(R.id.rssListView);

        mRssListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //@Override
            public void onItemClick(AdapterView<?> av, View view, int index,
                                    long arg3) {
                selectedRssItem = rssItems.get(index);
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });

        listAdapter = new RssListAdapter(this, rssItems, null);
        mRssListView.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String url;

        switch (item.getItemId()) {
            case R.id.category_culture:
                url = getString(R.string.url_culture);
                break;
            case R.id.category_defense_safety:
                url = getString(R.string.url_defense_safety);
                break;
            case R.id.category_eco:
                url = getString(R.string.url_eco);
                break;
            case R.id.category_economy:
                url = getString(R.string.url_economy);
                break;
            case R.id.category_education:
                url = getString(R.string.url_education);
                break;
            case R.id.category_incidents:
                url = getString(R.string.url_incidents);
                break;
            case R.id.category_moscow:
                url = getString(R.string.url_moscow);
                break;
            case R.id.category_world:
                url = getString(R.string.url_world);
                break;
            case R.id.category_tourism:
                url = getString(R.string.url_tourism);
                break;
            case R.id.category_sport:
                url = getString(R.string.url_sport);
                break;
            case R.id.category_science:
                url = getString(R.string.url_science);
                break;
            case R.id.category_society:
                url = getString(R.string.url_society);
                break;
            case R.id.category_politics:
                url = getString(R.string.url_politics);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        if (task != null) task.cancel(true);

        task = new RetrieveFeedTask();
        task.execute(url);

        return true;
    }

    private void refreshRssList(ArrayList<RssItem> newItems) {
        if (newItems == null) return;

        rssItems.clear();
        rssItems.addAll(newItems);

        listAdapter.notifyDataSetChanged();
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, ArrayList<RssItem>> {

        @Override
        protected ArrayList<RssItem> doInBackground(String... urls) {
            try {
                if (urls[0].equals("")) return null;
                return RssFetcher.fetch(urls[0]);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<RssItem> feed) {
            refreshRssList(feed);
        }
    }

    public class RssListAdapter extends BaseAdapter {

        private ArrayList<RssItem> items = new ArrayList<>();
        private Context context;
        private ImageLoader imageLoader;
        private ImageLoadingListener animateFirstDisplayListener;


        public RssListAdapter(Context context, ArrayList<RssItem> items, ImageLoadingListener animateFirstDisplayListener) {
            super();
            this.context = context;
            this.items = items;
            this.animateFirstDisplayListener = animateFirstDisplayListener;
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            final RssItem rssItem = items.get(position);


            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_rss_item, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.description = (TextView) convertView.findViewById(R.id.description);
                holder.image = (ImageView) convertView.findViewById(R.id.image);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.title.setText(rssItem.getTitle());
            holder.description.setText(rssItem.getDescription());

            String imageUrl = rssItem.getImageUrl();
            imageLoader.displayImage(imageUrl != null && !imageUrl.equals("") ? imageUrl : "drawable://" + R.drawable.noimage,
                    holder.image, animateFirstDisplayListener);

            return convertView;
        }

        class ViewHolder {
            TextView title;
            TextView description;
            ImageView image;
        }

    }
}
