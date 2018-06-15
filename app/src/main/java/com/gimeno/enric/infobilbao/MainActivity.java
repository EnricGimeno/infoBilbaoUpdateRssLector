package com.gimeno.enric.infobilbao;



import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gimeno.enric.infobilbao.RSSParser.RSSFeedListAdapter;
import com.gimeno.enric.infobilbao.RSSParser.RSSFeedModel;
import com.gimeno.enric.infobilbao.db.BilbaoFeedsDB;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DownloadCompleteListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private static final int RESULT_SETTINGS = 1;
    Cursor cursor;
    private List<RSSFeedModel> mFeedModelList;

    private static Uri uri = Uri.parse("content://es.infobilbao.alerts/alerts/*");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        // Store all the url in a list and execute for each one the
        int stringRes[] = {R.string.avisos_de_agua_y_suministros,   R.string.avisos_de_trafico_y_transportes, R.string.avisos_de_temas_varios,};
        URL url1 = null;
        URL url2 = null;
        try {
            url1 = new URL(getResources().getString(R.string.avisos_del_distrito_1_deusto));
            url2 = new URL(getResources().getString(R.string.avisos_del_distrito_2_uribarri));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new DownloadFeedTask(this,this, mRecyclerView).execute(url1, url2 );

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_list_view));
        mRecyclerView.addItemDecoration(dividerItemDecoration);


//        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new DownloadFeedTask().execute((Void) null);
//            }
//        });
    }

    //MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_preferences:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void downloadComplete() {
        String sortOrder = BilbaoFeedsDB.Alerts.CAMPO_PUB_DATE;
        cursor = getContentResolver().query(uri, null, null, null,  sortOrder + " DESC");
        mFeedModelList = getListItemData(cursor);
        mRecyclerView.setAdapter(new RSSFeedListAdapter(mFeedModelList));
    }

    private List<RSSFeedModel> getListItemData(Cursor cursor) {
        List<RSSFeedModel> listViewItems = new ArrayList<RSSFeedModel>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                listViewItems.add(new RSSFeedModel(
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
        return listViewItems;

    }


}
