package gank.kotlinforgank

import android.app.Application

/**
 */
class MyApplication : Application() {

    companion object {
        private var instance: Application? = null
        fun context() = instance!!
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}
