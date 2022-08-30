package io.github.livenlearnaday.countrylistkotlin.data.entity

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "country_table")
data class Country(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
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
    var callingCodes: List<String>,
    var favorite: Boolean = false,

    ){
    constructor(
        name:String,
        capital:String?,
        region:String?,
        subregion:String?,
        flag:String?,
        languages:List<Language>?,
        callingCodes:List<String>,
    ) : this(0, name, capital,region,subregion,flag,languages,callingCodes, false)
}










