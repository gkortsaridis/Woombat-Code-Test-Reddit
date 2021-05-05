package co.uk.gkortsaridis.woombatcodetest.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.uk.gkortsaridis.woombatcodetest.models.RedditHotItem
import co.uk.gkortsaridis.woombatcodetest.models.Resource
import co.uk.gkortsaridis.woombatcodetest.repositories.SubredditListRepository
import io.reactivex.android.schedulers.AndroidSchedulers
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

    fun getAndroidSubreddits(after: String? = null): MutableLiveData<Resource<ArrayList<RedditHotItem>>> {
        subredditList.postValue(Resource.loading(null))
        compositeDisposable.add(
            repository.getAndroidSubreddits(after)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { subredditList.postValue(Resource.success(data = it.data.children)); currentAfterValue = it.data.after },
                    { subredditList.postValue(Resource.error(msg = it.localizedMessage, data = null)) }
                )
        )

        return subredditList
    }

}