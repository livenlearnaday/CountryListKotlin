package io.github.livenlearnaday.countrylistkotlin.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.livenlearnaday.countrylistkotlin.data.repository.CountryDataSource
import io.github.livenlearnaday.countrylistkotlin.data.source.local.AppDatabase
import io.github.livenlearnaday.countrylistkotlin.data.source.remote.CountryService
import io.github.livenlearnaday.countrylistkotlin.data.repository.CountryRepository
import io.github.livenlearnaday.countrylistkotlin.data.DefaultCountryRepository
import io.github.livenlearnaday.countrylistkotlin.data.source.local.CountryLocalDataSource
import io.github.livenlearnaday.countrylistkotlin.data.source.remote.CountryApi
import io.github.livenlearnaday.countrylistkotlin.data.source.remote.CountryRemoteDataSource
import io.github.livenlearnaday.countrylistkotlin.utils.BASE_URL
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RemoteCountryDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalCountryDataSource

    @Singleton
    @RemoteCountryDataSource
    @Provides
    fun provideCountryRemoteDataSource(
        countryApi: CountryApi
    ): CountryDataSource {
        return CountryRemoteDataSource(
            countryApi
        )
    }



    @Singleton
    @LocalCountryDataSource
    @Provides
    fun provideCountryLocalDataSource(
        database: AppDatabase
    ): CountryDataSource {
        return CountryLocalDataSource(
            database.countryDao()
        )
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext.applicationContext,
            AppDatabase::class.java,
            "io.github.livenlearnaday.countrylistkotlin.db"
        )
            .fallbackToDestructiveMigration()
            // no migrations and clear database when upgrade the database version
            .fallbackToDestructiveMigrationOnDowngrade()
            // no migrations and clear database when downgrade the database version
            .build()
    }


    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO


    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()


    @Provides
    fun provideCountryService(retrofit: Retrofit): CountryService =
        retrofit.create(CountryService::class.java)


}


/**
 * The binding for TasksRepository is on its own module so that we can replace it easily in tests.
 */
@Module
@InstallIn(SingletonComponent::class)
object CountryRepositoryModule {

    @Singleton
    @Provides
    fun provideCountryRepository(
        @AppModule.RemoteCountryDataSource remoteCountryDataSource: CountryDataSource,
        @AppModule.LocalCountryDataSource localCountryDataSource: CountryDataSource,
        ioDispatcher: CoroutineDispatcher
    ): CountryRepository {
        return DefaultCountryRepository(
            remoteCountryDataSource, localCountryDataSource, ioDispatcher
        )
    }
}

