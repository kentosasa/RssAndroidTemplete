package com.nichannel.kento.rss.data;

import android.app.VoiceInteractor;
import android.content.Context;
import android.test.InstrumentationTestCase;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Kento on 15/11/22.
 */
public class Entry extends InstrumentationTestCase implements Serializable{
    int id = 0;
    String site = null;
    String title = null;
    String url = null;
    String description = null;
    String text = null;
    String html = null;
    String image = null;
    boolean fav = false;
    String web = null;

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

    public boolean isFav() {
        return fav;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
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

    public void setFav(boolean fav) {
        this.fav = fav;
    }
}
