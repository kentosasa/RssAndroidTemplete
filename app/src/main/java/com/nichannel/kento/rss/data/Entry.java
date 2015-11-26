package com.nichannel.kento.rss.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Kento on 15/11/22.
 */
public class Entry implements Serializable{
    int id;
    String site;
    String title;
    String url;
    String description;
    String text;
    String html;
    String image;

    public int getId() {
        return id;
    }

    public String getSite() {
        return site;
    }

    public String getDescription() {
        return description;
    }

    public String getText() {
        return text;
    }

    public String getHtml() {
        return html;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
