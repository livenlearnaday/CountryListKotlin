package io.github.livenlearnaday.countrylistkotlin.data.remote



import io.github.livenlearnaday.countrylistkotlin.data.entity.Country
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CountryService {
    @GET("all?fields=name,flag,capital,region,subregion,callingCodes,languages")
    suspend  fun getAllCountries(): List<Country>
}