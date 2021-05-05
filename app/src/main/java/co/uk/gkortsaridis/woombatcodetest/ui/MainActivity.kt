package co.uk.gkortsaridis.woombatcodetest.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import co.uk.gkortsaridis.woombatcodetest.R
import co.uk.gkortsaridis.woombatcodetest.models.RedditHotItem
import co.uk.gkortsaridis.woombatcodetest.models.Status
import co.uk.gkortsaridis.woombatcodetest.utils.AppDatabase
import co.uk.gkortsaridis.woombatcodetest.utils.RedditItemClickLIstener
import co.uk.gkortsaridis.woombatcodetest.viewmodels.SubredditListViewModel
import co.uk.gkortsaridis.woombatcodetest.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), RedditItemClickLIstener {

    private lateinit var viewModel: SubredditListViewModel
    private lateinit var adapter: SubredditsAdapter

    private val USE_CACHED_DATA = false //Should be somewhere in a settings page. Will leave it here for simplicity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, ViewModelFactory()).get(SubredditListViewModel::class.java)

        //I am allowing Main Thread Queries, mainly for simplicity and to quickly proceed with my time
        //On a production app, all DB call will of course be handles by a background thread
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "offline-reddits").allowMainThreadQueries().build()
        val dao = db.itemsDao()

        adapter = SubredditsAdapter(context = this, listener = this)
        reddits_rv.layoutManager = LinearLayoutManager(this)
        reddits_rv.adapter = adapter

        //Load next posts page once we reach the bottom of the recyclerview
        reddits_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!reddits_rv.canScrollVertically(RecyclerView.FOCUS_DOWN) && newState == RecyclerView.SCROLL_STATE_IDLE && !USE_CACHED_DATA) {
                    viewModel.getAndroidSubreddits(viewModel.currentAfterValue)
                }
            }
        })

        //Observe the UI data from viewmodel
        viewModel.getAndroidSubreddits(after = null, dao= dao, forceRefresh = !USE_CACHED_DATA).observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    adapter.appendNewItems(it.data ?: arrayListOf())
                }
                Status.ERROR -> {
                    Log.i("ERROR", it.message ?: "")
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    /*Should be a meaningfull loading ainmation*/
                    Toast.makeText(this, "Loading Posts", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    override fun onRedditClicked(reddit: RedditHotItem) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(reddit.data?.url)
        startActivity(intent)
    }
}