package com.nichannel.kento.rss;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nichannel.kento.rss.ui.WebViewFragment;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        tabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        pager = (ViewPager) this.findViewById(R.id.pager);

        // init view pager
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                WebViewFragment webViewFragment = new WebViewFragment();
                return webViewFragment;
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabHost.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                tabHost.setSelectedNavigationItem(state);
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
                case 1:
                    tabHost.addTab(
                            tabHost.newTab()
                                    .setText("Original")
                                    .setTabListener(this)
                    );

            }
        }
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
