package mcgill.sharoz.belltest

import android.app.Application
import mcgill.sharoz.belltest.util.PreferencesHelper

class MyApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        PreferencesHelper.init(applicationContext)
    }
}