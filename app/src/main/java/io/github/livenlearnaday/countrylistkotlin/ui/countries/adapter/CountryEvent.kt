package io.github.livenlearnaday.countrylistkotlin.ui.countries.adapter

import io.github.livenlearnaday.countrylistkotlin.data.entity.Country

sealed class CountryEvent{
    data class countryClicked(val country: Country): CountryEvent()
    data class countryFavClicked(val country: Country): CountryEvent()
}
