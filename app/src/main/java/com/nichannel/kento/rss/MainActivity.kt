package com.nichannel.kento.rss

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.nichannel.kento.rss.data.Entries
import com.nichannel.kento.rss.data.Entry
import com.nichannel.kento.rss.function.EndlessScrollListener
import com.nichannel.kento.rss.ui.HomeAdapter
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    internal var url = "https://nichannel.herokuapp.com/api/entries/daily_ranking"
    var loading = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar.setTitle(R.string.app_name)

//        val fab = findViewById(R.id.fab) as FloatingActionButton
//        fab.setOnClickListener {
//            view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
//            KotolinでIntentを書く
//            val intent = Intent(getApplicationContext(), javaClass<DetailActivity>())
//            intent.putExtra("test_result",);
//            startActivity(intent)
//        }

        //最初に初期化処理をしないとNullPointerを起こす AndroidのError
        var list: RecyclerView = findViewById(R.id.recycle_view) as RecyclerView
        var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this);
        list.setLayoutManager(layoutManager)

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        createHomeView()
    }

    //HomeViewの生成
    fun createHomeView(){
        //ことりんでvolley使ってみた。
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext);
        var request = JsonArrayRequest(
                url,
                { response ->
                    var entriesClass: Entries = Entries();
                    setMyAdapter(entriesClass.get_from_json(response, this))
                },
                { volleyError ->
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
        ).setRetryPolicy(DefaultRetryPolicy(5000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        queue.add(request)
    }

    //Adapterを登録する
    fun setMyAdapter(entries: ArrayList<Entry>){
        var list: RecyclerView = findViewById(R.id.recycle_view) as RecyclerView
        var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this);
        list.setLayoutManager(layoutManager)
        var adapter: HomeAdapter = HomeAdapter(entries, this);
        list.setAdapter(adapter)
        list.addOnScrollListener(object : EndlessScrollListener(list.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(current_page: Int) {
                Log.d("LoadMore: ", "Page" + current_page)
                if (loading) return
                Log.d("LoadMore: ", "Page" + current_page + " Loading")
                loading = true
                val queue: RequestQueue = Volley.newRequestQueue(applicationContext);
                var request = JsonArrayRequest(
                        url,
                        { response ->
                            loading = false
                            var entriesClass: Entries = Entries();
                            entries.addAll(entriesClass.get_from_json(response, this@MainActivity))
                            adapter.notifyDataSetChanged()
                        },
                        { volleyError ->
                            Log.d("VolleryError", "悲しいことにエラーが発生しました。")
                        }
                ).setRetryPolicy(DefaultRetryPolicy(5000,
                        3,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
                queue.add(request)

            }
        })
    }

    override fun onBackPressed() {
        val drawer= findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        val id = item.itemId
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.popular_daily) {
            url = getString(R.string.daily_ranking_url)
        } else if (id == R.id.popular_weekly) {
            url = getString(R.string.weekly_ranking_url)
        } else if (id == R.id.popular_monthly) {
            url = getString(R.string.montyly_ranking_url)
        } else if (id == R.id.nav_fav) {
            url = getString(R.string.entries_url)
        } else if (id == R.id.new_entry) {
            url = getString(R.string.new_entry_url)
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
