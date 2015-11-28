package com.nichannel.kento.rss;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nichannel.kento.rss.data.Entry;
import com.nichannel.kento.rss.function.EndlessScrollListener;

import java.util.ArrayList;

public class ExchangeActivity extends AppCompatActivity {
    String url = getString(R.string.new_entry_url);
    String hoge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<Entry> entries = new ArrayList<Entry>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.addOnScrollListener(new EndlessScrollListener((LinearLayoutManager)
                recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {

            }
        });
    }

}
