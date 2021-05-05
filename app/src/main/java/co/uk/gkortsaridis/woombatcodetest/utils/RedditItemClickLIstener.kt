package co.uk.gkortsaridis.woombatcodetest.utils

import co.uk.gkortsaridis.woombatcodetest.models.RedditHotItem

interface RedditItemClickLIstener {
    fun onRedditClicked(reddit: RedditHotItem)
}