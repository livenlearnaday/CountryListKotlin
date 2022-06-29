package io.github.livenlearnaday.countrylistkotlin.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.livenlearnaday.countrylistkotlin.data.entity.Country

@Dao
interface CountryDao {

    /**
     * Observes list of countries.
     *
     * @return all countries.
     */


    @Query("SELECT * FROM country_table")
    fun getAllCountries(): List<Country>

    @Query("SELECT * FROM country_table WHERE name = :name")
    fun getCountry(name: String): Country

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountries(countries: List<Country>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCountry(country: Country)

    @Query("SELECT * FROM country_table WHERE favorite = 1")
    fun getFavCountries(): List<Country>

    @Query("Select * from country_table where name like  :name or capital like  :capital")
    suspend fun getCountriesSearchResults(name: String, capital: String): List<Country>

    @Query("UPDATE country_table SET favorite = 0")
    suspend fun clearAllFavCountries()

    @Query("DELETE FROM country_table")
    suspend fun clearCountryTable()

    @Query("UPDATE country_table SET favorite = :isFav WHERE name = :name")
    suspend fun updateCountryFav(isFav: Boolean, name: String)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCountries(countries: List<Country>)

    @Query("SELECT * FROM country_table")
    fun observeCountries(): LiveData<List<Country>>

    @Query("SELECT * FROM country_table WHERE name = :name ")
    fun observeCountry(name: String): LiveData<Country>

    @Query("SELECT * FROM country_table WHERE favorite = 1")
    fun observeCountriesFav(): LiveData<List<Country>>

    @Query("SELECT * FROM country_table where name like  :name or capital like  :capital")
    fun observeCountriesSearchResults(name: String, capital: String): LiveData<List<Country>>



}
