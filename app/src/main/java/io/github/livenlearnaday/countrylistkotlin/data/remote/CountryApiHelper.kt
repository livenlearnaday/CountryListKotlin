package io.github.livenlearnaday.countrylistkotlin.data.remote

import javax.inject.Inject


class CountryApiHelper @Inject constructor(private val countryService: CountryService) {

   suspend fun getAllCountries() = countryService.getAllCountries()

}