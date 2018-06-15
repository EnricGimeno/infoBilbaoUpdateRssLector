package com.gimeno.enric.infobilbao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.gimeno.enric.infobilbao.RSSParser.RSSFeedListAdapter;
import com.gimeno.enric.infobilbao.RSSParser.RSSFeedModel;
import com.gimeno.enric.infobilbao.RSSParser.RssDownloadHelper;
import com.gimeno.enric.infobilbao.db.BilbaoFeedsDB;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class DownloadFeedTask extends AsyncTask<URL, Void, String> {

    DownloadCompleteListener mDownloadCompleteListener;
    Context context;
    private RecyclerView recyclerView;

    // Ruta de la URI BD
    private static Uri uri = Uri.parse("content://es.infobilbao.alerts/alerts");

    public DownloadFeedTask(DownloadCompleteListener mDownloadCompleteListener, Context context, RecyclerView mRecyclerView) {
        this.mDownloadCompleteListener = mDownloadCompleteListener;
        this.context = context;
        this.recyclerView = mRecyclerView;
    }
    // https://developer.android.com/reference/android/os/AsyncTask
    @Override
    protected String doInBackground(URL... urls) {
        try {
            for(URL url: urls){
                downloadData(url.toString());
            }
            return "OK";
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void downloadData(String urlLink) throws IOException {
        if (TextUtils.isEmpty(urlLink))
            Log.e(TAG, "The URL is empty");

        try {
            if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                urlLink = "http://" + urlLink;
            Log.i("URL DOWNLOAD", urlLink + "");
            URL url = new URL(urlLink);
            InputStream inputStream = url.openConnection().getInputStream();
            List<RSSFeedModel> parseList =  RssDownloadHelper.parseFeed(inputStream);// Parse the feed
            for (RSSFeedModel item: parseList) {
                ContentValues values = new ContentValues();
                Log.i("TITLE VALUES", item.title);
                values.put("guid", item.guid + "");
                values.put("title", item.title);
                values.put("pubdate", item.pubDate);
                values.put("link", item.link);
                values.put("description", item.description);
                context.getContentResolver().insert(uri, values);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error", e);
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Error", e);
        }
    }

    @Override
    protected void onPostExecute(String s) {
        mDownloadCompleteListener.downloadComplete();

    }
}
