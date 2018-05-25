package com.gimeno.enric.infobilbao.RSSParser;
import java.util.Calendar;

public class RSSFeedModel {

    public long guid;
    public String title;
    public String pubDate;
    public String link;
    public String description;

    public RSSFeedModel(Long guid, String title, String pubDate, String link, String description) {
        this.guid = guid;
        this.title = title;
        this.pubDate = pubDate;
        this.link = link;
        this.description = description;
    }
}
