package io.github.livenlearnaday.countrylistkotlin.data.repository

import io.github.livenlearnaday.countrylistkotlin.data.local.key_value_pairs.SharedPrefHelper
import io.github.livenlearnaday.countrylistkotlin.utils.COUNTRIES_LOADED_KEY
import javax.inject.Inject

class SharedPrefHelperRepository @Inject constructor(
    private val sharedPrefHelper: SharedPrefHelper
) {

     fun setloaded(loaded: Boolean) = sharedPrefHelper.save(COUNTRIES_LOADED_KEY,loaded)

    fun loaded() = sharedPrefHelper.getValueBoolean(COUNTRIES_LOADED_KEY,false)





}