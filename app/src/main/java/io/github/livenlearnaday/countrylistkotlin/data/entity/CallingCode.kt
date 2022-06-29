package io.github.livenlearnaday.countrylistkotlin.data.entity

data class CallingCode(
   /* @PrimaryKey(autoGenerate = true)
    @NonNull*/
    val id: Int = 0,
    var callingCode: String? = ""
)
