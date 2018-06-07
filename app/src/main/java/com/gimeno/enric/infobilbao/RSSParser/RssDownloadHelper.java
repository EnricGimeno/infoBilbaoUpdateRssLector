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

    public static List<RSSFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        long guid = 0;
        String title = null;
        String pubDate = null;
        String link = null;
        String description = null;
        boolean isItem = false;
        List<RSSFeedModel> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MainActivity", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                    Log.d("TITLE", "TITLE ==> " + title);
                }else if(name.equalsIgnoreCase("guid")){
                    try{
                        guid = Long.valueOf(result);
                    }catch (NumberFormatException e) {
                        Log.d("Conversion error", e.getMessage().toString());
                    }
                }else if(name.equalsIgnoreCase("pubDate")){
                    try{
                        pubDate = result;
                        //pubDate = getDataTime(result);
                        Log.d("PUBDATERSS HELPER", pubDate + "");
                    }catch (Exception e) {
                        Log.d("Conversion error time", e.getMessage().toString());
                    }
                }else if (name.equalsIgnoreCase("description")) {
                    description = Jsoup.parse(result).text();
                    //description = result;
                }else if (name.equalsIgnoreCase("link")) {
                    link = result;

                }

                if (title != null && link != null && description != null && pubDate != null) {
                    if(isItem) {
                        RSSFeedModel item = new RSSFeedModel(guid, title, pubDate, link, description);
                        items.add(item);
                    }
                    else{
                        title = null;
                        link = null;
                        description = null;
                        pubDate = null;
                        isItem = false;
                    }

                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }

}


