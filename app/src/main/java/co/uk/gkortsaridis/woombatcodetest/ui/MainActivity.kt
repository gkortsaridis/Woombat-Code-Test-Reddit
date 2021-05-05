package co.uk.gkortsaridis.woombatcodetest.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.uk.gkortsaridis.woombatcodetest.R
import co.uk.gkortsaridis.woombatcodetest.models.RedditHotItem
import co.uk.gkortsaridis.woombatcodetest.models.Status
import co.uk.gkortsaridis.woombatcodetest.utils.RedditItemClickLIstener
import co.uk.gkortsaridis.woombatcodetest.viewmodels.SubredditListViewModel
import co.uk.gkortsaridis.woombatcodetest.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), RedditItemClickLIstener {

    private lateinit var viewModel: SubredditListViewModel
    private lateinit var adapter: SubredditsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = SubredditsAdapter(context = this, listener = this)
        reddits_rv.layoutManager = LinearLayoutManager(this)
        reddits_rv.adapter = adapter

        reddits_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!reddits_rv.canScrollVertically(RecyclerView.FOCUS_DOWN) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    viewModel.getAndroidSubreddits(
                        viewModel.currentAfterValue
                    )
                }
            }
        })

        viewModel = ViewModelProvider(this, ViewModelFactory()).get(SubredditListViewModel::class.java)
        viewModel.getAndroidSubreddits().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    adapter.appendNewItems(it.data ?: arrayListOf())
                }
                Status.ERROR -> {
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
        intent.data = Uri.parse(reddit.data.url)
        startActivity(intent)
    }
}