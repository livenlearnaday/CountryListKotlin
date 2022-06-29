package io.github.livenlearnaday.countrylistkotlin.data.repository

import androidx.lifecycle.LiveData
import io.github.livenlearnaday.countrylistkotlin.data.Result
import io.github.livenlearnaday.countrylistkotlin.data.entity.Country


interface CountryRepository {

    fun observeCountries():LiveData<Result<List<Country>>>

    fun observeCountriesFav():LiveData<Result<List<Country>>>

    suspend fun getCountries(forceUpdate: Boolean):List<Country>

    suspend fun getCountry(name: String): Country

    suspend fun insertCountries(countries: List<Country>)

    suspend fun updateCountryLocalDataSource(country: Country)

    suspend fun updateCountryFavLocalDataSource(fav:Boolean, countryName:String)

    suspend fun getFavCountries(): List<Country>

    suspend fun getSearchedCountries(name:String, capital:String): List<Country>

    suspend fun clearAllFavCountries()

    suspend fun clearCountryTable()


}
