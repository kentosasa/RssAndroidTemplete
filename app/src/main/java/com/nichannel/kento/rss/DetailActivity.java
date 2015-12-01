package com.nichannel.kento.rss;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nichannel.kento.rss.data.Entry;
import com.nichannel.kento.rss.ui.FastModeFragment;
import com.nichannel.kento.rss.ui.WebViewFragment;

import org.json.JSONObject;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class DetailActivity extends AppCompatActivity implements MaterialTabListener {
    private ViewPager pager;
    private MaterialTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Entry entry = (Entry)getIntent().getSerializableExtra("entry");
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = getString(R.string.access);
        url += "?entry_id=" + entry.getId();
        queue.add(new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(entry.getTitle());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String articleURL = entry.getUrl();
                String articleTitle = entry.getTitle();
                String sharedText = articleTitle + " " + articleURL;

                // builderの生成　ShareCompat.IntentBuilder.from(Context context);
                ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(DetailActivity.this);

                // アプリ一覧が表示されるDialogのタイトルの設定
                builder.setChooserTitle(R.string.app_name);

                // シェアするタイトル
                builder.setSubject(articleTitle);

                // シェアするテキスト
                builder.setText(sharedText);

                // シェアするタイプ（他にもいっぱいあるよ）
                builder.setType("text/plain");

                // Shareアプリ一覧のDialogの表示
                builder.startChooser();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        tabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        pager = (ViewPager) this.findViewById(R.id.pager);

        // init view pager
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        FastModeFragment fastModeFragment = new FastModeFragment();
                        return fastModeFragment;
                    case 1:
                        WebViewFragment webViewFragment = new WebViewFragment();
                        return webViewFragment;
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabHost.setSelectedNavigationItem(position);
            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            switch (i){
                case 0:
                    tabHost.addTab(
                            tabHost.newTab()
                                    .setText("FastMode")
                                    .setTabListener(this)
                    );
                    break;
                case 1:
                    tabHost.addTab(
                            tabHost.newTab()
                                    .setText("Original")
                                    .setTabListener(this)
                    );
                    break;

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }
}
