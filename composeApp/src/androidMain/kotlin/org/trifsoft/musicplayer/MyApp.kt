package org.trifsoft.musicplayer

import android.app.Application

class MyApp : Application() {
    companion object {
        lateinit var application: Application
            private set
    }
    override fun onCreate() {
        super.onCreate()
        application = this
    }
}
