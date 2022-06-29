package io.github.livenlearnaday.countrylistkotlin.data.source.remote

import androidx.lifecycle.LiveData
import io.github.livenlearnaday.countrylistkotlin.data.Result
import io.github.livenlearnaday.countrylistkotlin.data.entity.Country
import io.github.livenlearnaday.countrylistkotlin.data.repository.CountryDataSource


class CountryRemoteDataSource internal constructor(
    private val countryApi: CountryApi
) : CountryDataSource {
    override fun observeCountries(): LiveData<Result<List<Country>>> {
        TODO("Not yet implemented")
    }


    override suspend fun getCountriesFromApi(): List<Country> =
        countryApi.getCountries()


    override suspend fun getCountriesFromDb(): List<Country> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCountries(forceUpdate: Boolean): Result<List<Country>> {
        TODO("Not yet implemented")
    }


    override suspend fun getCountryFromDb(name: String): Country {
        TODO("Not yet implemented")
    }


    override fun observeCountriesFav(): LiveData<Result<List<Country>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getFavCountries(): List<Country> {
        TODO("Not yet implemented")
    }

    override suspend fun getSearchedCountries(name: String, capital: String): List<Country> {
        TODO("Not yet implemented")
    }


    override suspend fun saveAllCountries(countries: List<Country>) {
        TODO("Not yet implemented")
    }


    override suspend fun updateCountries(countries: List<Country>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCountry(country: Country) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCountryFav(fav: Boolean, countryName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearAllFavCountries() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllCountries() {
        TODO("Not yet implemented")
    }



}