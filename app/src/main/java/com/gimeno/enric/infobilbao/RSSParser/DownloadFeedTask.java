package com.gimeno.enric.infobilbao.RSSParser;


import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.gimeno.enric.infobilbao.MainActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class DownloadFeedTask  extends AsyncTask<Void, Void, Boolean> {

    private String urlLink;

    @Override
    protected void onPreExecute() {
//        mSwipeLayout.setRefreshing(true);
//        mFeedTitle = null;
//        mFeedLink = null;
//        mFeedDescription = null;
//        mFeedTitleTextView.setText("Feed Title: " + mFeedTitle);
//        mFeedDescriptionTextView.setText("Feed Description: " + mFeedDescription);
//        mFeedLinkTextView.setText("Feed Link: " + mFeedLink);
//        urlLink = mEditText.getText().toString();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
//        if (TextUtils.isEmpty(urlLink))
//            return false;
//
//        try {
//            if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
//                urlLink = "http://" + urlLink;
//
//            URL url = new URL(urlLink);
//            InputStream inputStream = url.openConnection().getInputStream();
//            mFeedModelList = parseFeed(inputStream);
//            return true;
//        } catch (IOException e) {
//            Log.e(TAG, "Error", e);
//        } catch (XmlPullParserException e) {
//            Log.e(TAG, "Error", e);
//        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean success) {
//        mSwipeLayout.setRefreshing(false);
//
//        if (success) {
//            mFeedTitleTextView.setText("Feed Title: " + mFeedTitle);
//            mFeedDescriptionTextView.setText("Feed Description: " + mFeedDescription);
//            mFeedLinkTextView.setText("Feed Link: " + mFeedLink);
//            // Fill RecyclerView
//            mRecyclerView.setAdapter(new RssFeedListAdapter(mFeedModelList));
//        } else {
//            Toast.makeText(MainActivity.this,
//                    "Enter a valid Rss feed url",
//                    Toast.LENGTH_LONG).show();
//        }
    }

}
