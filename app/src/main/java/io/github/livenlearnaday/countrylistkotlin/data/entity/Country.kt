package io.github.livenlearnaday.countrylistkotlin.data.entity

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "country_table")
data class Country(
    @PrimaryKey
    @SerializedName("name")
    val name: String,
    @SerializedName("capital")
    var capital: String?,
    @SerializedName("region")
    @Nullable
    var region: String?,
    @SerializedName("subregion")
    @Nullable
    var subregion: String?,
    @SerializedName("flag")
    var flag: String?,
    @SerializedName("languages")
    var languages: List<Language>?,
    @SerializedName("callingCodes")
    var callingCodes: ArrayList<String>?,
    var favorite: Boolean = false,

    )




@Dao
interface CountryDao {

    @Query("SELECT * FROM country_table")
    fun getAllCountriesInAscOrder(): List<Country>

    @Query("SELECT * FROM country_table WHERE name = :name")
    fun getCountry(name: String): Country

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<Country>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(country: Country)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCountry(country: Country)

    @Query("Select * from country_table c where name like  :name or capital like  :capital")
    fun getSearchResults(name : String, capital:String) : List<Country>



    @Query("UPDATE country_table SET favorite = :isFav WHERE name = :name")
    suspend fun updateCountryFav(isFav: Boolean,name:String)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCountries(countries: List<Country>)

    @Query("SELECT * FROM country_table WHERE favorite = 1")
    fun getAllFavCountries(): List<Country>

    @Query("UPDATE country_table SET favorite = 0")
    suspend fun clearAllFavCountries()



}








