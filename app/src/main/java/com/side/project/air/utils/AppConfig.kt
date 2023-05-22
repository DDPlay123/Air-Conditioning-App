package com.side.project.air.utils

import android.annotation.SuppressLint
import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.side.project.air.R
import com.side.project.air.di.other
import com.side.project.air.di.repoModule
import com.side.project.air.di.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AppConfig: Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@AppConfig)
            modules(listOf(viewModel, repoModule, other))
        }
    }

    @SuppressLint("ResourceType")
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(applicationContext)
            .crossfade(true)
            .placeholder(R.drawable.baseline_image_search_24)
            .error(R.drawable.baseline_image_search_24)
            .build()
    }
}