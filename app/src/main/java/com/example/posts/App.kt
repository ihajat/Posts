package com.example.posts

import android.app.Application
import com.example.posts.data.cache.CacheLibrary
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        CacheLibrary.init(this)
        startKoin { androidContext(this@App) }
    }
}