package io.github.livenlearnaday.countrylistkotlin.data.repository

import androidx.lifecycle.LiveData
import io.github.livenlearnaday.countrylistkotlin.data.Result
import io.github.livenlearnaday.countrylistkotlin.data.entity.Country

interface CountryDataSource {

    fun observeCountries():LiveData<Result<List<Country>>>
    suspend fun getCountriesFromApi(): List<Country>
    suspend fun getCountriesFromDb(): List<Country>
    suspend fun getAllCountries(forceUpdate: Boolean = false): Result<List<Country>>
    suspend fun getCountryFromDb(name: String):Country
    fun observeCountriesFav():LiveData<Result<List<Country>>>
    suspend fun getFavCountries(): List<Country>
    suspend fun getSearchedCountries(name:String, capital:String): List<Country>
    suspend fun saveAllCountries(countries: List<Country>)
    suspend fun updateCountries(countries: List<Country>)
    suspend fun updateCountry(country: Country)
    suspend fun updateCountryFav(fav:Boolean, countryName:String)
    suspend fun clearAllFavCountries()
    suspend fun deleteAllCountries()

}