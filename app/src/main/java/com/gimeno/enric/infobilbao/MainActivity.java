package com.gimeno.enric.infobilbao;


import android.database.Cursor;
import android.net.Uri;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DownloadCompleteListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;

    private static Uri uri = Uri.parse("content://es.infobilbao.alerts/alerts/*");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        new DownloadFeedTask(this,this, mRecyclerView).execute(getResources().getString(R.string.avisos_de_agua_y_suministros));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new DownloadFeedTask().execute((Void) null);
//            }
//        });
    }

    @Override
    public void downloadComplete() {
    }
    private List<RSSFeedModel> getListItemData(Cursor cursor) {
        List<RSSFeedModel> listViewItems = new ArrayList<RSSFeedModel>();

        if (cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                listViewItems.add( new RSSFeedModel(
                        cursor.getLong(cursor.getColumnIndex("guid")),
                        cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("pubdate")),
                        cursor.getString(cursor.getColumnIndex("link")),
                        cursor.getString(cursor.getColumnIndex("description"))
                        ));
                cursor.moveToNext();
            }
        cursor.close();
        }
        return  listViewItems;
    }


}
