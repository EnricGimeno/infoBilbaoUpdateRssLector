package com.gimeno.enric.infobilbao.RSSParser;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gimeno.enric.infobilbao.R;

import java.util.List;

public class RSSFeedListAdapter extends RecyclerView.Adapter<RSSFeedListAdapter.FeedModelViewHolder> {

    private List<RSSFeedModel> mRssFeedModels;

    public RSSFeedListAdapter(List<RSSFeedModel>[] alertList) {
    }

    public static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private View rssFeedView;

        public FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;
        }
    }

    public RSSFeedListAdapter(List<RSSFeedModel> rssFeedModels) {
        mRssFeedModels = rssFeedModels;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rss_feed, parent, false);
        FeedModelViewHolder holder = new FeedModelViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        final RSSFeedModel rssFeedModel = mRssFeedModels.get(position);
        ((TextView) holder.rssFeedView.findViewById(R.id.titleText)).setText(rssFeedModel.title);
        ((TextView) holder.rssFeedView.findViewById(R.id.descriptionText)).setText(rssFeedModel.description);
        ((TextView) holder.rssFeedView.findViewById(R.id.pubDateText)).setText(rssFeedModel.pubDate);

    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }

}