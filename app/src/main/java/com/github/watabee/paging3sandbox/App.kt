package com.github.watabee.paging3sandbox

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.Lazy
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), ImageLoaderFactory {

    @Inject lateinit var imageLoaderProvider: Lazy<ImageLoader>

    override fun newImageLoader(): ImageLoader {
        return imageLoaderProvider.get()
    }
}
