package com.gimeno.enric.infobilbao;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.gimeno.enric.infobilbao.RSSParser.DownloadFeedTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private TextView mFeedTitleTextView;
    private TextView mFeedLinkTextView;
    private TextView mFeedDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mFeedTitleTextView = (TextView) findViewById(R.id.feedTitle);
        mFeedDescriptionTextView = (TextView) findViewById(R.id.feedDescription);
        mFeedLinkTextView = (TextView) findViewById(R.id.feedLink);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new DownloadFeedTask().execute((Void) null);
            }
        });
    }

}
