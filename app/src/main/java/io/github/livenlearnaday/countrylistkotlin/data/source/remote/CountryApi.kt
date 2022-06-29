package io.github.livenlearnaday.countrylistkotlin.data.source.remote

import javax.inject.Inject


class CountryApi @Inject constructor(private val countryService: CountryService) {

   suspend fun getCountries() = countryService.getAllCountries()



}