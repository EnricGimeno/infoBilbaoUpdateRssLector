package com.gimeno.enric.infobilbao;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.gimeno.enric.infobilbao.RSSParser.RssDownloadHelper;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class DownloadFeedTask extends AsyncTask<String, Void, String> {

    DownloadCompleteListener mDownloadCompleteListener;
    Context context;
    // Ruta de la URI BD
    private static Uri uri = Uri.parse("content://es.infobilbao.alerts");

    public DownloadFeedTask(DownloadCompleteListener mDownloadCompleteListener,Context context) {
        this.mDownloadCompleteListener = mDownloadCompleteListener;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            downloadData(params[0]);
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

            URL url = new URL(urlLink);
            InputStream inputStream = url.openConnection().getInputStream();
            RssDownloadHelper.parseFeed(inputStream); // Parse the feed

        } catch (IOException e) {
            Log.e(TAG, "Error", e);
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Error", e);
        }
    }
}
