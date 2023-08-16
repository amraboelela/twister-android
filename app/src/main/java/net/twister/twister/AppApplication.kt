package net.twister.twister

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

class AppApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        //        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "myriad.ttf");
    }
}