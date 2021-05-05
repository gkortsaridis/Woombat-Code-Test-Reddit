package co.uk.gkortsaridis.woombatcodetest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import co.uk.gkortsaridis.woombatcodetest.models.RedditHotItem
import co.uk.gkortsaridis.woombatcodetest.models.Resource
import co.uk.gkortsaridis.woombatcodetest.models.ResponseRedditHot
import co.uk.gkortsaridis.woombatcodetest.repositories.SubredditListRepository
import co.uk.gkortsaridis.woombatcodetest.viewmodels.SubredditListViewModel
import com.google.common.truth.Truth.assertThat
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SubredditListViewModelTest {

    private val CORRECT_AFTER_ID = "12345"
    private val INCORRECT_AFTER_ID = "----"

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: SubredditListRepository

    @Mock
    private lateinit var viewModel: SubredditListViewModel

    private val subredditsObservable = Observable.just(ResponseRedditHot())
    private val subredditsSuccess = MutableLiveData<Resource<ArrayList<RedditHotItem>>>()
    private val subredditsError = MutableLiveData<Resource<ArrayList<RedditHotItem>>>()

    private val customError = RuntimeException("Custom Error")

    @Before
    fun setUp() {
        repository = Mockito.spy(SubredditListRepository())
        viewModel = Mockito.spy(SubredditListViewModel(repository))

        subredditsSuccess.postValue(Resource.success(arrayListOf()))
        subredditsError.postValue(Resource.error(msg = "Custom Error", data = null))

        //Setup Mock responses
        Mockito.doReturn(subredditsObservable).`when`(repository).getAndroidSubreddits(null)
        Mockito.doReturn(subredditsObservable).`when`(repository).getAndroidSubreddits(CORRECT_AFTER_ID)
        Mockito.doThrow(customError).`when`(repository).getAndroidSubreddits(INCORRECT_AFTER_ID)

        Mockito.doReturn(subredditsSuccess).`when`(viewModel).getAndroidSubreddits(null)
        Mockito.doReturn(subredditsSuccess).`when`(viewModel).getAndroidSubreddits(CORRECT_AFTER_ID)
        Mockito.doReturn(subredditsError).`when`(viewModel).getAndroidSubreddits(INCORRECT_AFTER_ID)
    }

    @Test
    fun repositorySubredditsSuccess() {
        assertThat(repository.getAndroidSubreddits(null)).isEqualTo(subredditsObservable)
        assertThat(repository.getAndroidSubreddits(CORRECT_AFTER_ID)).isEqualTo(subredditsObservable)
    }

    @Test
    fun viewModelSubredditsSuccess() {
        assertThat(viewModel.getAndroidSubreddits(null)).isEqualTo(subredditsSuccess)
        assertThat(viewModel.getAndroidSubreddits(CORRECT_AFTER_ID)).isEqualTo(subredditsSuccess)
    }

    @Test(expected= Exception::class)
    fun repositorySubredditsError() { repository.getAndroidSubreddits(INCORRECT_AFTER_ID) }

    @Test
    fun viewModelSubredditsError() {
        assertThat(viewModel.getAndroidSubreddits(INCORRECT_AFTER_ID).value?.message).isEqualTo(subredditsError.value?.message)
    }

}