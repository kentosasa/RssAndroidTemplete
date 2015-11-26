package com.nichannel.kento.rss.data;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kento on 15/11/22.
 */
public class Entries {

    public ArrayList<Entry> get_from_json(JSONArray raw, Context context){
        ArrayList<Entry> data = new ArrayList<>();

        SharedPreferences favs = context.getSharedPreferences("Favs", Context.MODE_PRIVATE);

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
                entry.setFav(favs.getBoolean(""+val.getInt("id"),false));
                data.add(entry);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
