package mcgill.sharoz.belltest.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mcgill.sharoz.belltest.R
import mcgill.sharoz.belltest.api.TwitterAPIService
import mcgill.sharoz.belltest.models.tweetModel.TweetsItem
import mcgill.sharoz.belltest.ui.dialog.TweetFragment
import mcgill.sharoz.belltest.ui.search.SearchActivity
import mcgill.sharoz.belltest.util.Constants.ENGLISH
import mcgill.sharoz.belltest.util.Constants.FRENCH
import mcgill.sharoz.belltest.util.PreferencesHelper
import mcgill.sharoz.belltest.util.Utils.applyLanguageLocale

class MainActivity : AppCompatActivity(), OnMapReadyCallback, MainContract.View {

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLastLocation: Location

    private lateinit var mPresenter: MainPresenter

    private var mFetched = false
    private var mMapReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setTitle(R.string.app_name)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mPresenter = MainPresenter(this, TwitterAPIService.create())
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMapReady = true
        fetchLocationData()
    }

    private fun fetchLocationData() {

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            Log.d(TAG, "Requesting Location Permission")

        } else {
            setUpLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            LOCATION_REQUEST_CODE -> {

                if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    Log.d(TAG, "Permission Denied")
                    generateMessage(getString(R.string.permission_denied))

                } else {
                    Log.d(TAG, "Permission Granted")
                    setUpLocation()
                }
            }
        }
    }

    // Calling this method only when permission was granted
    @SuppressLint("MissingPermission")
    fun setUpLocation() {

        Log.d(TAG, "Getting Last known Location")

        mMap.isMyLocationEnabled = true
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->

            if (location != null) {

                mLastLocation = location
                animateMapCamera(location)
                mPresenter.fetchTweets(mLastLocation, RADIUS)
            }
        }
    }

    override fun animateMapCamera(location: Location) {

        val currentLatLng = LatLng(location.latitude, location.longitude)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13f))
    }

    override fun generateMessage(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun setTweetMarkers(tweets: List<TweetsItem?>) {

        for (tweet in tweets) {

            if (tweet?.geo?.type ?: "" == "Point") {

                val long = tweet?.geo?.coordinates?.get(0) ?: 0.0
                val lat = tweet?.geo?.coordinates?.get(1) ?: 0.0

                if (long != 0.0 && lat != 0.0) {

                    val marker = mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(long, lat))
                            .title(tweet?.user?.name ?: "-")
                    )

                    marker.tag = tweet
                }
            }
        }

        setMarkerListener()
        mFetched = true
    }

    private fun setMarkerListener() {

        mMap.setOnInfoWindowClickListener { marker ->
            val tweet = marker.tag as TweetsItem
            openTweetDialog(tweet)
        }
    }

    override fun openTweetDialog(tweet: TweetsItem) {

        val fragment = TweetFragment.newInstance(tweet)
        fragment.show(supportFragmentManager, "TweetFragment")
    }

    override fun onResume() {

        super.onResume()
        if (!mFetched && mMapReady) {
            fetchLocationData()
        }
    }

    override fun onDestroy() {

        super.onDestroy()
        mPresenter.unsubscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }

            R.id.language -> {
                toggleLanguage()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun toggleLanguage() {

        val langCode = PreferencesHelper.getInstance().langCode
        val newLang: String

        newLang = if (langCode == ENGLISH) {
            FRENCH
        } else {
            ENGLISH
        }

        applyLanguageLocale(baseContext, newLang)
        generateMessage(getString(R.string.changed))
        restartActivity()
    }

    private fun restartActivity() {
        val refresh = Intent(this, MainActivity::class.java)
        startActivity(refresh)
        finish()
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val LOCATION_REQUEST_CODE = 1
        private const val RADIUS = "5km"
    }
}

