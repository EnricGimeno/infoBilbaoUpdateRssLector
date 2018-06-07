package com.gimeno.enric.infobilbao;



import android.net.Uri;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;

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
        //mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        // Store all the url in a list
        int stringRes[] = {R.string.avisos_de_agua_y_suministros,  R.string.avisos_de_temas_varios, R.string.avisos_de_trafico_y_transportes};
        List<String> myUrls = new ArrayList<String>();
        for (int id : stringRes){
            String url = getResources().getString(id);
            myUrls.add(url);
        }
        for (String url:myUrls) {
            //new DownloadFeedTask(this,this, mRecyclerView).execute(url);
        }

        new DownloadFeedTask(this,this, mRecyclerView).execute(getResources().getString(R.string.avisos_de_temas_varios));

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

    @Override
    public void downloadComplete() {
    }


}
