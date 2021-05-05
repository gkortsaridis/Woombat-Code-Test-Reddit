package co.uk.gkortsaridis.woombatcodetest.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.uk.gkortsaridis.woombatcodetest.models.RedditHotItem
import co.uk.gkortsaridis.woombatcodetest.models.RedditHotItemData
import co.uk.gkortsaridis.woombatcodetest.models.Resource
import co.uk.gkortsaridis.woombatcodetest.repositories.SubredditListRepository
import co.uk.gkortsaridis.woombatcodetest.utils.RedditItemDataDAO
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SubredditListViewModel(private val repository: SubredditListRepository): ViewModel() {

    private val subredditList = MutableLiveData<Resource<ArrayList<RedditHotItem>>>()
    var currentAfterValue: String? = null

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getAndroidSubreddits(after: String? = null, dao: RedditItemDataDAO? = null, forceRefresh: Boolean = false): MutableLiveData<Resource<ArrayList<RedditHotItem>>> {
        subredditList.postValue(Resource.loading(null))

        try{
            compositeDisposable.add(
                repository.getAndroidSubreddits(after, dao, forceRefresh)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.newThread())
                    .subscribe(
                        {
                            //Every time posts are loaded, update the Room DB
                            val dataToBeSaved = ArrayList<RedditHotItemData>()
                            for (item in it.data?.children ?: arrayListOf()) { if(item.data != null){ dataToBeSaved.add(item.data) } }
                            dao?.nukeTable()
                            dao?.insertAll(dataToBeSaved)

                            subredditList.postValue(Resource.success(data = it.data?.children));
                            currentAfterValue = it.data?.after
                        },
                        { subredditList.postValue(Resource.error(msg = it.localizedMessage, data = null)) }
                    )
            )
        }catch (e: Exception) {
            subredditList.postValue(Resource.error(e.localizedMessage, data = null))
        }

        return subredditList
    }

}