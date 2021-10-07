package io.github.livenlearnaday.countrylistkotlin.ui.countries

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.livenlearnaday.countrylistkotlin.data.entity.Country
import io.github.livenlearnaday.countrylistkotlin.data.repository.CountryRepository
import io.github.livenlearnaday.countrylistkotlin.data.repository.DataStoreHelperRepository
import io.github.livenlearnaday.countrylistkotlin.utils.Resource
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val repository: CountryRepository,
    private val dataStoreHelperRepository: DataStoreHelperRepository
) : ViewModel() {


    fun getAllCountries() =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = repository.getAllCountriesFromApi()))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred."))
            }
        }


    fun getAllCountriesFromDb() =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = repository.getAllCountriesFromDb()))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred."))
            }
        }



    fun insertAllCountries(countries: List<Country>) {
        viewModelScope.launch {
            repository.insertAllCountries(countries)
        }
    }

    fun searchForItems(name: String, capital: String) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = repository.search(name, capital)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred."))
            }
        }


    fun getAllFavCountries() =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = repository.getAllFavCountries()))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred."))
            }
        }



    fun clearAllFavCountries(){
        viewModelScope.launch {
            repository.clearAllFavCountries()
        }
    }

    fun setLoaded(loaded: Boolean){
        viewModelScope.launch {
            dataStoreHelperRepository.setLoadedDataPref(loaded)
        }
    }

    fun getLoadedFlow() = dataStoreHelperRepository.getLoadedDataPrefFlow()

    fun clearCountryTable(){
        viewModelScope.launch {
            repository.clearCountryTable()
        }
    }

}



