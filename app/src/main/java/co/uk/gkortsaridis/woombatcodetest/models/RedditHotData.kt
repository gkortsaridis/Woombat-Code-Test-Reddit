package co.uk.gkortsaridis.woombatcodetest.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class RedditHotData(
    val modhash: String = "",
    val dist: Int = -1,
    val after: String? = null,
    val before: String? = null,
    val children: ArrayList<RedditHotItem> = arrayListOf()
)

data class RedditHotItem(
    val kind: String = "",
    val data: RedditHotItemData? = null
)


@Entity
data class RedditHotItemData(
    @PrimaryKey(autoGenerate = true) val uid: Int,

    @ColumnInfo(name = "self_text") val selftext: String = "",
    @ColumnInfo(name = "author_full_name") val author_fullname: String = "",
    @ColumnInfo(name = "title") val title: String = "",
    @ColumnInfo(name = "url") val url: String = "",
)