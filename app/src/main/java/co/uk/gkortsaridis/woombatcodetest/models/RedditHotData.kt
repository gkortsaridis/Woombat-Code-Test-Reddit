package co.uk.gkortsaridis.woombatcodetest.models

data class RedditHotData(
    val modhash: String = "",
    val dist: Int = -1,
    val after: String? = null,
    val before: String? = null,
    val children: ArrayList<RedditHotItem> = arrayListOf()
)

data class RedditHotItem(
    val kind: String = "",
    val data: RedditHotItemData = RedditHotItemData()
)

data class RedditHotItemData(
    val selftext: String = "",
    val author_fullname: String = "",
    val title: String = "",
    val url: String = "",
)