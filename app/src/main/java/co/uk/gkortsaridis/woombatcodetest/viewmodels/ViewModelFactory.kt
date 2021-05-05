package co.uk.gkortsaridis.woombatcodetest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.uk.gkortsaridis.woombatcodetest.repositories.SubredditListRepository

class ViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubredditListViewModel::class.java)) {
            val repo = SubredditListRepository()
            return SubredditListViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}