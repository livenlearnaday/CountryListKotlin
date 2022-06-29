package io.github.livenlearnaday.countrylistkotlin.data.source.remote



import io.github.livenlearnaday.countrylistkotlin.data.entity.Country
import retrofit2.http.GET

interface CountryService {
    @GET("all_countries_7_fields.json")
    suspend  fun getAllCountries(): List<Country>


}