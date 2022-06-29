package io.github.livenlearnaday.countrylistkotlin.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Language(
//    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String? = "",
    var nativeName: String? = "",
) : Serializable {

    @SerializedName("iso639_1")
    @Expose
    var iso6391: String? = ""

    @SerializedName("iso639_2")
    @Expose
    var iso6392: String? = ""


}