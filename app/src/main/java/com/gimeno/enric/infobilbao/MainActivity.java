package com.gimeno.enric.infobilbao;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.gimeno.enric.infobilbao.RSSParser.RSSFeedListAdapter;
import com.gimeno.enric.infobilbao.RSSParser.RSSFeedModel;
import com.gimeno.enric.infobilbao.RSSParser.RssDownloadHelper;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private TextView mFeedTitleTextView;
    private TextView mFeedLinkTextView;
    private TextView mFeedDescriptionTextView;

    private List<RSSFeedModel> mFeedModelList;
    private String mFeedTitle;
    private String mFeedLink;
    private String mFeedDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mFeedTitleTextView = (TextView) findViewById(R.id.feedTitle);
        mFeedDescriptionTextView = (TextView) findViewById(R.id.feedDescription);
        mFeedLinkTextView = (TextView) findViewById(R.id.feedLink);

        new DownloadFeedTask().execute();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new DownloadFeedTask().execute((Void) null);
            }
        });
    }

    public class DownloadFeedTask  extends AsyncTask<Void, Void, Boolean> {

        private String urlLink;

        @Override
        protected void onPreExecute() {
        mSwipeLayout.setRefreshing(true);
        mFeedTitle = null;
        mFeedLink = null;
        mFeedDescription = null;
        mFeedTitleTextView.setText("Feed Title: " + mFeedTitle);
        mFeedDescriptionTextView.setText("Feed Description: " + mFeedDescription);
        mFeedLinkTextView.setText("Feed Link: " + mFeedLink);
        urlLink = "http://www.bilbao.eus/cs/Satellite?language=es&pageid=3000075248&pagename=Bilbaonet/Page/BIO_suscripcionRSS&tipoSus=Avisos&idSec=3000014008";
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
        if (TextUtils.isEmpty(urlLink))
            return false;

        try {
            Log.e(TAG, "Do in Background" );
            if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                urlLink = "http://" + urlLink;

            URL url = new URL(urlLink);
            InputStream inputStream = url.openConnection().getInputStream();
            mFeedModelList = RssDownloadHelper.parseFeed(inputStream); // Parse the feed
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error", e);
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Error", e);
        }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
        mSwipeLayout.setRefreshing(false);

            if (success) {
                mFeedTitleTextView.setText("Feed Title: " + mFeedTitle);
                mFeedDescriptionTextView.setText("Feed Description: " + mFeedDescription);
                mFeedLinkTextView.setText("Feed Link: " + mFeedLink);
                // Fill RecyclerView
                mRecyclerView.setAdapter(new RSSFeedListAdapter(mFeedModelList));
            } else {
                Toast.makeText(MainActivity.this,
                        "Enter a valid Rss feed url",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
