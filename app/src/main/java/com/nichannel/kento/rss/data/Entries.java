package com.nichannel.kento.rss.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kento on 15/11/22.
 */
public class Entries {

    public ArrayList<Entry> get_from_json(JSONArray raw){
        ArrayList<Entry> data = new ArrayList<>();
        for (int i = 0; i < raw.length(); i++){
            Entry entry = new Entry();
            try {
                JSONObject val = raw.getJSONObject(i);
                entry.setId(val.getInt("id"));
                entry.setSite(val.getString("site"));
                entry.setTitle(val.getString("title"));
                entry.setUrl(val.getString("url"));
                entry.setDescription(val.getString("description"));
                entry.setText(val.getString("text"));
                entry.setHtml(val.getString("html"));
                entry.setImage(val.getString("image"));
                data.add(entry);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
