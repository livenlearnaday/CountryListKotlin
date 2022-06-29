package io.github.livenlearnaday.countrylistkotlin.ui.countries.adapter


sealed class CallingCodeEvent{
    data class callingCodeClicked(val callingCode: String): CallingCodeEvent()
}
