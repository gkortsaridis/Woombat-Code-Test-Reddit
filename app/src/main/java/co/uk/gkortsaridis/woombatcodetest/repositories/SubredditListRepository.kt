package co.uk.gkortsaridis.woombatcodetest.repositories

import co.uk.gkortsaridis.woombatcodetest.models.RedditHotData
import co.uk.gkortsaridis.woombatcodetest.models.RedditHotItem
import co.uk.gkortsaridis.woombatcodetest.models.ResponseRedditHot
import co.uk.gkortsaridis.woombatcodetest.utils.ApiClient
import co.uk.gkortsaridis.woombatcodetest.utils.RedditItemDataDAO
import io.reactivex.Observable

class SubredditListRepository() {


    /*
    * Either Use a cached version of any posts that have been downloaded via Room DB
    * or make an API Call using Retrofit
    * */
    fun getAndroidSubreddits(after: String? = null, dao: RedditItemDataDAO? = null, forceRefresh: Boolean = false): Observable<ResponseRedditHot> {
        return if(dao != null && !forceRefresh) {
            val saved = dao.getAll()
            val children = ArrayList<RedditHotItem>()
            for(item in saved) { children.add(RedditHotItem(data= item)) }
            val data = RedditHotData(children = children)
            val response = ResponseRedditHot(data = data)
            Observable.just(response)
        } else {
            ApiClient.api.getHotAndroidSubreddits(after)
        }
    }
}