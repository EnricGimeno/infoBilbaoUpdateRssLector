package com.gimeno.enric.infobilbao;



import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.View;
import android.widget.Toast;

import com.gimeno.enric.infobilbao.RSSParser.RSSFeedListAdapter;
import com.gimeno.enric.infobilbao.RSSParser.RSSFeedModel;
import com.gimeno.enric.infobilbao.db.BilbaoFeedsDB;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DownloadCompleteListener, ItemClickListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private static final int RESULT_SETTINGS = 1;
    Cursor cursor;
    private List<RSSFeedModel> mFeedModelList;

    private static Uri uri = Uri.parse("content://es.infobilbao.alerts/alerts/*");


    // Proceso de dialogo para chequear conexion a internet
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        // Vemos si la base de datos tiene datos
        cursor = getContentResolver().query(uri, null, null, null,null);

        // Si ya hay datos no hagas nada
        if (cursor.getCount()>0){

            cursor.close();

        }else{
            cursor.close();
            // Comprueba la conexion a internet y muestra la una alerta
            if (isNetworkConnected()) {
                // Progress dialogo para la descarga
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage(getString(R.string.messageProgressDialog));
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                // Comienza la descarga
                startDownload();


            } else {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.titleAlert))
                        .setMessage(getString(R.string.messageAlert))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert).show();
            }

        }

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

    private void startDownload() {
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
    }

    // Funcion que combrueba la conexion a internet
    private boolean isNetworkConnected() {
        // Recupera una instancia de la clase ConnectivityManager del contexto de aplicación actual.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Recupera una instancia de la clase NetworkInfo que representa la conexión de red actual. Esto será nulo si no hay red disponible.
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // Comprueba si hay una conexión de red disponible y el dispositivo está conectado.
        return networkInfo != null && networkInfo.isConnected();
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


    @Override
    public void onClick(View view, int position) {
        final RSSFeedModel alert = mFeedModelList.get(position);
        Toast.makeText(this, alert.title, Toast.LENGTH_LONG);
    }
}
