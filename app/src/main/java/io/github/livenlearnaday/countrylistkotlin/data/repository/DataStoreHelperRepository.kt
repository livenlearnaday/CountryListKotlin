package io.github.livenlearnaday.countrylistkotlin.data.repository

import io.github.livenlearnaday.countrylistkotlin.data.local.key_value_pairs.DataStoreHelper
import javax.inject.Inject

class DataStoreHelperRepository @Inject constructor(
    private val dataStoreHelper: DataStoreHelper
) {

    suspend fun setLoadedDataPref(loaded: Boolean) = dataStoreHelper.setCountryLoaded(loaded)


    fun getLoadedDataPrefFlow() =  dataStoreHelper.countryLoadedFlow




}