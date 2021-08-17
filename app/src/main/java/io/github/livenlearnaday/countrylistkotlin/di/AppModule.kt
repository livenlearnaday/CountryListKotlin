package io.github.livenlearnaday.countrylistkotlin.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.livenlearnaday.countrylistkotlin.data.entity.CountryDao
import io.github.livenlearnaday.countrylistkotlin.data.local.AppDatabase
import io.github.livenlearnaday.countrylistkotlin.data.local.key_value_pairs.DataStoreHelper
import io.github.livenlearnaday.countrylistkotlin.data.remote.CountryApiHelper
import io.github.livenlearnaday.countrylistkotlin.data.remote.CountryService
import io.github.livenlearnaday.countrylistkotlin.data.repository.CountryRepository
import io.github.livenlearnaday.countrylistkotlin.data.repository.DataStoreHelperRepository
import io.github.livenlearnaday.countrylistkotlin.utils.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()


    @Provides
    fun provideCountryService(retrofit: Retrofit): CountryService = retrofit.create(CountryService::class.java)


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)


    @Provides
    fun provideCountryDao(db: AppDatabase) = db.countryDao()


    @Singleton
    @Provides
    fun provideRepositoryCountry(
        countryApiHelper: CountryApiHelper,
        countryDao: CountryDao
    ) = CountryRepository(countryApiHelper, countryDao)


    @Singleton
    @Provides
    fun provideRepositoryDataStoreHelper(
        dataStoreHelper: DataStoreHelper
    ) = DataStoreHelperRepository(dataStoreHelper)


}
