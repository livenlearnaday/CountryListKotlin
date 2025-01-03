package io.github.livenlearnaday.countrylistkotlin.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import io.github.livenlearnaday.countrylistkotlin.data.Result
import io.github.livenlearnaday.countrylistkotlin.data.Result.Success
import io.github.livenlearnaday.countrylistkotlin.data.entity.Country
import io.github.livenlearnaday.countrylistkotlin.data.repository.CountryDataSource

class CountryLocalDataSource internal constructor(
    private val countryDao: CountryDao
) : CountryDataSource {


    override suspend fun getCountriesFromDb(): List<Country> =
        countryDao.getAllCountries()

    override suspend fun getAllCountries(forceUpdate: Boolean): Result<List<Country>> {
        TODO("Not yet implemented")
    }

    override fun observeCountries(): LiveData<Result<List<Country>>> {
       return countryDao.observeCountries().map{
           Success(it)
       }
    }


    override suspend fun getCountriesFromApi(): List<Country> {
        TODO("Not yet implemented")
    }


    override suspend fun getCountryFromDb(name: String): Country =
        countryDao.getCountry(name)


    override suspend fun saveAllCountries(countries: List<Country>) =
        countryDao.insertCountries(countries)


    override suspend fun updateCountries(countries: List<Country>) =
        countryDao.updateCountries(countries)


    override suspend fun updateCountry(country: Country) =
        countryDao.updateCountry(country)


    override suspend fun updateCountryFav(fav: Boolean, countryName: String) =
        countryDao.updateCountryFav(fav, countryName)



    override suspend fun getFavCountries(): List<Country> = countryDao.getFavCountries()

    override suspend fun getSearchedCountries(name: String, capital: String): List<Country> = countryDao.getCountriesSearchResults(name, capital)

    override suspend fun clearAllFavCountries() =
        countryDao.clearAllFavCountries()


    override suspend fun deleteAllCountries() =
        countryDao.clearCountryTable()


    override fun observeCountriesFav(): LiveData<Result<List<Country>>> {
        return countryDao.observeCountriesFav().map{
            Success(it)
        }
    }



}