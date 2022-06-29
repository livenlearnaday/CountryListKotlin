package io.github.livenlearnaday.countrylistkotlin.data

import androidx.lifecycle.LiveData
import io.github.livenlearnaday.countrylistkotlin.data.entity.Country
import io.github.livenlearnaday.countrylistkotlin.data.repository.CountryDataSource
import io.github.livenlearnaday.countrylistkotlin.data.repository.CountryRepository
import io.github.livenlearnaday.countrylistkotlin.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultCountryRepository(
    private val countryRemoteDataSource: CountryDataSource,
    private val countryLocalDataSource: CountryDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CountryRepository {

    override fun observeCountries(): LiveData<Result<List<Country>>> {
        return countryLocalDataSource.observeCountries()
    }

    override fun observeCountriesFav(): LiveData<Result<List<Country>>> {
        return countryLocalDataSource.observeCountriesFav()
    }


    override suspend fun getCountries(forceUpdate: Boolean): List<Country> {
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                updateCountriesDatabase()
            }
            return countryLocalDataSource.getCountriesFromDb()
        }
    }

    private suspend fun updateCountriesDatabase() {
        val remoteCountries = countryRemoteDataSource.getCountriesFromApi()
        if (remoteCountries.isNotEmpty()) {
            countryLocalDataSource.deleteAllCountries()
            countryLocalDataSource.saveAllCountries(remoteCountries)
        }
    }


    override suspend fun getCountry(name: String): Country {
        wrapEspressoIdlingResource {
            return countryLocalDataSource.getCountryFromDb(name)
        }
    }


    override suspend fun insertCountries(countries: List<Country>) {
        countryLocalDataSource.saveAllCountries(countries)
    }


    override suspend fun updateCountryLocalDataSource(country: Country) {
        countryLocalDataSource.updateCountry(country)
    }

    override suspend fun updateCountryFavLocalDataSource(fav: Boolean, countryName: String) {
        countryLocalDataSource.updateCountryFav(fav, countryName)
    }


    override suspend fun getFavCountries(): List<Country> {
        wrapEspressoIdlingResource {
            return countryLocalDataSource.getFavCountries()
        }
    }

    override suspend fun getSearchedCountries(name: String, capital: String): List<Country> {
        wrapEspressoIdlingResource {
            return countryLocalDataSource.getSearchedCountries(name, capital)
        }
    }

    override suspend fun clearAllFavCountries(){
        wrapEspressoIdlingResource {
            countryLocalDataSource.clearAllFavCountries()
        }
    }


    override suspend fun clearCountryTable() {
        countryLocalDataSource.deleteAllCountries()
    }


}







