package com.nichannel.kento.rss

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import com.nichannel.kento.rss.data.Entries
import com.nichannel.kento.rss.data.Entry
import com.nichannel.kento.rss.function.EndlessScrollListener
import com.nichannel.kento.rss.ui.HomeAdapter
import hotchemi.android.rate.AppRate
import java.io.ObjectInputStream
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private  var url = "https://nichannel.herokuapp.com/api/entries/daily_ranking"
    private var loading = false
    private  var splash: FrameLayout? = null
    private  var queue: RequestQueue? = null
    private var refreshLayout: SwipeRefreshLayout? = null
    private var list: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar.setTitle(R.string.app_name)

        val app = application as MyApplication
        val t = app.tracker
        // Classインスタンスから名前取得する場合は難読化に注意.
        t.setScreenName("MainActivity")
        t.send(HitBuilders.AppViewBuilder().build())


        AppRate.with(this).setInstallDays(3)
                .setRemindInterval(10) // default 1
                .setShowLaterButton(true) // default true
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);

//        val fab = findViewById(R.id.reload) as FloatingActionButton
//        fab.setOnClickListener {
////            view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
////            KotolinでIntentを書く
////            val intent = Intent(getApplicationContext(), javaClass<DetailActivity>())
////            startActivity(intent)
//        }


        //最初に初期化処理をしないとNullPointerを起こす AndroidのError
        list = findViewById(R.id.recycle_view) as RecyclerView
        layoutManager = LinearLayoutManager(this);
        (list as RecyclerView).setLayoutManager(layoutManager)

        splash = findViewById(R.id.splash) as FrameLayout
        refreshLayout = findViewById(R.id.refresh) as SwipeRefreshLayout
        (refreshLayout as SwipeRefreshLayout).setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimaryDark, R.color.colorPrimaryDark, R.color.colorPrimaryDark)
        refreshLayout?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            createHomeView()
        })


        queue = Volley.newRequestQueue(applicationContext)

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        url = getString(R.string.new_entry_url)
        createHomeView()
    }

    //HomeViewの生成
    fun createHomeView(){
        refreshLayout?.setRefreshing(true)
        //ことりんでvolley使ってみた。
        var request = JsonArrayRequest(
                url,
                { response ->
                    var entriesClass: Entries = Entries();
                    var entries: ArrayList<Entry>  = entriesClass.get_from_json(response, this);
                    setMyAdapter(entries)
                    splash?.setVisibility(android.view.View.GONE)
                },
                { volleyError ->
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                    finish()
                }
        ).setRetryPolicy(DefaultRetryPolicy(5000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        queue?.add(request)
    }

    fun createFavView() {
        val favs = getSharedPreferences("Favs", Context.MODE_PRIVATE)
        var entries: ArrayList<Entry> = ArrayList();
        val keys = favs.all
        //Userオブジェクト復元
        keys.forEach {
            url += "&ids[]=" + it.key
            if(favs.getBoolean(it.key, false)){
                try {
                val fis = openFileInput(it.key)
                val ois = ObjectInputStream(fis)
                val entry = ois.readObject() as Entry
                ois.close()
                entries.add(entry)
                } catch (e: Exception) {
                    Log.d("InputStream", "Error")
                }
            }
        }
        Log.d("FavURL: ", url);
        setMyAdapter(entries)
        splash?.setVisibility(android.view.View.GONE)
    }

    //Adapterを登録する
    fun setMyAdapter(entries: ArrayList<Entry>){
        var list: RecyclerView = findViewById(R.id.recycle_view) as RecyclerView
        var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this);
        list.setLayoutManager(layoutManager)
        var adapter: HomeAdapter = HomeAdapter(entries, this);
        list.setAdapter(adapter)
        setEntryContent(entries, adapter, url.replace("entries","contents"))


        list.addOnScrollListener(object : EndlessScrollListener(list.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(current_page: Int) {
                if (loading) return
                if (supportActionBar.title.equals(getString(R.string.stock))) return
                loading = true
                var request = JsonArrayRequest(
                        url+"?page="+current_page,
                        { response ->
                            loading = false
                            var entriesClass: Entries = Entries();
                            entries.addAll(entriesClass.get_from_json(response, this@MainActivity))
                            adapter.notifyDataSetChanged()
                            refreshLayout?.setRefreshing(false)
                        },
                        { volleyError ->
                            Log.d("VolleryError", "悲しいことにエラーが発生しました。")
                        }
                ).setRetryPolicy(DefaultRetryPolicy(5000,
                        3,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
                queue?.add(request)
                setEntryContent(entries, adapter, (url+"?page="+current_page).replace("entries","contents"))

            }
        })
    }

    fun setEntryContent(entries: ArrayList<Entry>, adapter: HomeAdapter, content_url: String) {
        var request = JsonArrayRequest(
                content_url,
                { response ->
                    loading = false
                    var entriesClass: Entries = Entries();
                    val tmp = ArrayList<Entry>()
                    tmp.addAll(entriesClass.set_content_from_json(response, this@MainActivity, entries))
                    entries.clear()
                    entries.addAll(tmp)
                    adapter.notifyDataSetChanged()
                },
                { volleyError ->
                    Log.d("VolleryError", "悲しいことにエラーが発生しました。")
                }
        ).setRetryPolicy(DefaultRetryPolicy(5000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        queue?.add(request)
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
            supportActionBar.setTitle(getString(R.string.popular_daily))
            createHomeView()
        } else if (id == R.id.popular_weekly) {
            url = getString(R.string.weekly_ranking_url)
            supportActionBar.setTitle(getString(R.string.popular_weekly))
            createHomeView()
        } else if (id == R.id.popular_monthly) {
            url = getString(R.string.montyly_ranking_url)
            supportActionBar.setTitle(getString(R.string.popular_monthly))
            createHomeView()
        } else if (id == R.id.nav_fav) {
            supportActionBar.setTitle(getString(R.string.stock))
            url = getString(R.string.entries_url)
            url += "?"
            createFavView()
        } else if (id == R.id.new_entry) {
            supportActionBar.setTitle(getString(R.string.new_entry))
            url = getString(R.string.new_entry_url)
            createHomeView()
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
