package com.nichannel.kento.rss.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.nichannel.kento.rss.R;
import com.nichannel.kento.rss.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FastModeFragment extends Fragment {
    public FastModeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Entry entry = (Entry)getActivity().getIntent().getSerializableExtra("entry");
        View v = inflater.inflate(R.layout.fragment_fast_mode, null);
        WebView webview = new WebView(getActivity());
        webview = (WebView)v.findViewById(R.id.webView);
        String css = "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css\">\n";
        css += "<style type=\"text/css\">\n" +
                "  img {\n" +
                "    width: 100%;\n" +
                "  }\n" +
                "  body {\n" +
                "    padding: 16px;\n" +
                "  }\n" +
                "</style>\n";
        if (entry.getHtml() == null) {
            Log.d("HTML追加読み込み", "追加読み込み");
            RequestQueue mQueue = Volley.newRequestQueue(getActivity());
            String url = getActivity().getString(R.string.entries_url).replace("entries", "contents");
            url += "?ids[]=" + entry.getId();
            //同期処理サンプル
            final WebView finalWebview = webview;
            final String finalCss = css;
            JsonArrayRequest r = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        finalWebview.loadData("<body>" + response.getJSONObject(0).getString("html") + finalCss + "</body>", "text/html; charset=utf-8", "utf-8");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            mQueue.add(r);
        } else {
            webview.loadData("<body>" + entry.getHtml() + css + "</body>", "text/html; charset=utf-8", "utf-8");
        }


        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        return v;
    }
}
