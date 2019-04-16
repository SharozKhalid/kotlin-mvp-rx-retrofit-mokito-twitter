package mcgill.sharoz.belltest

import android.location.Location
import io.reactivex.Observable
import mcgill.sharoz.belltest.api.TwitterAPIService
import mcgill.sharoz.belltest.models.SearchResponse
import mcgill.sharoz.belltest.ui.main.MainContract
import mcgill.sharoz.belltest.ui.main.MainPresenter
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as whenever

class MainPresenterTest {

    @Mock
    private lateinit var view: MainContract.View

    @Mock
    private lateinit var apiService: TwitterAPIService

    private lateinit var presenter: MainContract.Presenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MainPresenter(view, apiService)
    }

    @Test
    fun rx_successResponse_setTweetsMarker() {

        val response = SearchResponse()
        response.tweets = emptyList()
        val observable = Observable.just(response)

        whenever(
            apiService.searchTweets(
                anyString(), anyString(), anyString(), anyString(),
                anyInt(), anyBoolean(), anyLong()
            )
        ).thenReturn(observable)

        val mLocation = mock(Location::class.java)

        whenever(mLocation.latitude).thenReturn(-73.5715)
        whenever(mLocation.longitude).thenReturn(45.4591)

        presenter.loadMapTweets("ABC", mLocation, "5km")

        verify(view, times(1)).setTweetMarkers(anyList())
        verify(view, never()).generateMessage(anyString())
    }

    @Test
    fun rx_errorResponse_generateMessage() {

        val observable: Observable<SearchResponse> = Observable.create { subscriber ->
            subscriber.onError(IllegalArgumentException("Invalid Response"))
        }

        whenever(
            apiService.searchTweets(
                anyString(), anyString(), anyString(), anyString(),
                anyInt(), anyBoolean(), anyLong()
            )
        ).thenReturn(observable)

        val location = mock(Location::class.java)

        whenever(location.latitude).thenReturn(-73.5715)
        whenever(location.longitude).thenReturn(45.4591)

        presenter.loadMapTweets("ABC", location, "5km")

        verify(view, never()).setTweetMarkers(anyList())
        verify(view, times(1)).generateMessage(anyString())
    }

    companion object {

        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }
}