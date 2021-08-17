package io.github.livenlearnaday.countrylistkotlin.data.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import io.github.livenlearnaday.countrylistkotlin.data.entity.Country
import io.github.livenlearnaday.countrylistkotlin.data.entity.CountryDao
import io.github.livenlearnaday.countrylistkotlin.data.remote.CountryApiHelper
import javax.inject.Inject


class CountryRepository @Inject constructor (
    private val countryApiHelper: CountryApiHelper,
    private val countryDao: CountryDao
) {

    suspend  fun getAllCountriesFromApi(): List<Country> = countryApiHelper.getAllCountries()

    fun getCountryFromDb(name: String) = countryDao.getCountry(name)

    fun getAllCountriesFromDb(): List<Country> = countryDao.getAllCountriesInAscOrder()

    suspend fun insertAllCountries(countries: List<Country>) = countryDao.insertAll(countries)

    suspend fun updateCountryFav(fav:Boolean, countryName:String) = countryDao.updateCountryFav(fav,countryName)

    fun getAllFavCountries(): List<Country>  = countryDao.getAllFavCountries()

    suspend fun clearAllFavCountries() = countryDao.clearAllFavCountries()

    fun search(name : String, capital: String) : List<Country> {
        return countryDao.getSearchResults(name, capital)
    }




    /*@WorkerThread
    fun search(name : String, capital: String) : List<Country> {
        return countryDao.getSearchResults(name, capital)
    }
*/


}
