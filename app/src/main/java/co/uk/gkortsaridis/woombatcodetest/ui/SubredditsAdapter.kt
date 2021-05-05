package co.uk.gkortsaridis.woombatcodetest.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.uk.gkortsaridis.woombatcodetest.R
import co.uk.gkortsaridis.woombatcodetest.models.RedditHotItem
import co.uk.gkortsaridis.woombatcodetest.utils.RedditItemClickLIstener


class SubredditsAdapter(
    private var items: ArrayList<RedditHotItem> = arrayListOf(),
    private val context: Context,
    private var listener: RedditItemClickLIstener? = null
): RecyclerView.Adapter<SubredditsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(context).inflate(
            R.layout.reddit_item_card,
            parent,
            false
        )
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position], listener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var title: TextView? = null
        private var description: TextView? = null
        private var author: TextView? = null
        private var container: LinearLayout? = null

        init {
            container = itemView.findViewById(R.id.container)
            title = itemView.findViewById(R.id.title)
            description = itemView.findViewById(R.id.description)
            author = itemView.findViewById(R.id.author)
        }

        fun bindItems(item: RedditHotItem, listener: RedditItemClickLIstener?) {
            title?.text = item.data.title
            description?.text = item.data.selftext
            author?.text = item.data.author_fullname

            container?.setOnClickListener { listener?.onRedditClicked(item) }
        }
    }

    fun updateItems(items: ArrayList<RedditHotItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun appendNewItems(items: ArrayList<RedditHotItem>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}
