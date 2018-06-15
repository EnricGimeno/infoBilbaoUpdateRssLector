package com.gimeno.enric.infobilbao.RSSParser;

import android.util.Log;
import android.util.Xml;
import org.jsoup.Jsoup;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RssDownloadHelper {
    // We don't use namespaces
    private static final String ns = null;

    public static List<RSSFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            parser.nextTag();
            return readChannel(parser);
        } finally {
            inputStream.close();
        }
    }

    private static List readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<RSSFeedModel> items = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "channel");
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("item")) {
                items.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }
    private static RSSFeedModel readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        long guid = 0;
        String title = null;
        String pubDate = null;
        String link = null;
        String description = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("guid")) {
                guid = readGuid(parser);
            } else if (name.equals("pubDate")) {
                pubDate = readPubDate(parser);
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else {
                skip(parser);
            }
        }
        return new RSSFeedModel(guid, title, pubDate, link, description);
    }

    // Processes title tags in the feed.
    private static String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }
    // Processes guid tags in the feed.
    private static long readGuid(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "guid");
        long guid = readLong(parser);
        parser.require(XmlPullParser.END_TAG, ns, "guid");
        return guid;
    }
    // Processes pubDate tags in the feed.
    private static String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");
        String pubDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
        return pubDate;
    }
    // Processes description tags in the feed.
    private static String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        // Parse without HTML STRINGS
        String description = Jsoup.parse(readText(parser)).text();
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }
    // Processes link tags in the feed.
    private static String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    // For the tags texts, extracts their text values.
    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    // For the tags long extracts their long values.
    private static long readLong(XmlPullParser parser) throws IOException, XmlPullParserException {
        long result = 0;
        if (parser.next() == XmlPullParser.TEXT) {
            try{
                result = Long.valueOf(parser.getText());
            }catch (NumberFormatException e) {
                Log.d("Conversion error", e.getMessage().toString());
            }
            parser.nextTag();
        }
        return result;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}



