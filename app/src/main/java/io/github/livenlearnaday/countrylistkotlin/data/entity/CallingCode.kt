package io.github.livenlearnaday.countrylistkotlin.data.entity

import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import java.io.Serializable

data class CallingCode(
   /* @PrimaryKey(autoGenerate = true)
    @NonNull*/
    val id: Int = 0,
    var callingCode: String? = "",

    ) : Serializable {

}
