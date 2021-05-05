package co.uk.gkortsaridis.woombatcodetest.repositories

import co.uk.gkortsaridis.woombatcodetest.models.ResponseRedditHot
import co.uk.gkortsaridis.woombatcodetest.utils.ApiClient
import io.reactivex.Observable

class SubredditListRepository() {

    fun getAndroidSubreddits(after: String? = null): Observable<ResponseRedditHot> {
        return ApiClient.api.getHotAndroidSubreddits(after)
    }
}