package co.uk.gkortsaridis.woombatcodetest.models

data class RedditHotData(
    val modhash: String,
    val dist: Int,
    val after: String?,
    val before: String?,
    val children: ArrayList<RedditHotItem>
)

data class RedditHotItem(
    val kind: String,
    val data: RedditHotItemData
)

data class RedditHotItemData(
    val selftext: String,
    val author_fullname: String,
    val title: String,
    val url: String,
)