package com.nichannel.kento.rss.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.nichannel.kento.rss.R;
import com.nichannel.kento.rss.data.Entry;
import com.nichannel.kento.rss.function.ImageGetterImpl;

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
        webview.loadData("<body>" + entry.getHtml()+css + "</body>", "text/html; charset=utf-8", "utf-8");

//        View v = inflater.inflate(R.layout.fragment_fast_mode, null);
//        TextView textView = (TextView)v.findViewById(R.id.html_view);
//        Log.d("HTML", entry.getHtml());
//        ImageGetterImpl imageGetter = new ImageGetterImpl(getActivity().getApplicationContext());
//        textView.setText(Html.fromHtml(entry.getHtml()));
//        textView.setMovementMethod(LinkMovementMethod.getInstance());

        return v;
    }
}
