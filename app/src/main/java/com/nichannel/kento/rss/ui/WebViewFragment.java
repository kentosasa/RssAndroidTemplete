package com.nichannel.kento.rss.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nichannel.kento.rss.R;
import com.nichannel.kento.rss.data.Entry;

public class WebViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Entry entry = (Entry)getActivity().getIntent().getSerializableExtra("entry");
        View v = inflater.inflate(R.layout.fragment_web_view, null);
        WebView webview = new WebView(getActivity());
        webview = (WebView)v.findViewById(R.id.webView);
        webview.loadUrl(entry.getUrl());
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}
