package com.github.watabee.paging3sandbox.image

import android.content.Context
import coil.ImageLoader
import coil.util.DebugLogger
import com.github.watabee.paging3sandbox.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.io.File

@InstallIn(SingletonComponent::class)
@Module
object ImageModule {

    @Provides
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .availableMemoryPercentage(0.25)
            .crossfade(true)
            .okHttpClient {
                val cacheDirectory = File(context.cacheDir, "image_cache").apply { mkdirs() }
                val cache = Cache(cacheDirectory, 50 * 1024 * 1024)

                // Don't limit concurrent network requests by host.
                val dispatcher = Dispatcher().apply { maxRequestsPerHost = maxRequests }

                // Lazily create the OkHttpClient that is used for network operations.
                OkHttpClient.Builder()
                    .cache(cache)
                    .dispatcher(dispatcher)
                    .build()
            }
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }
            .build()
    }
}
