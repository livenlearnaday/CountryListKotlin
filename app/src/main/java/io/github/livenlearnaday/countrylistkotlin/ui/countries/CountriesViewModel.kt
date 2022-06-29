package io.github.livenlearnaday.countrylistkotlin.ui.countries

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.livenlearnaday.countrylistkotlin.data.Result
import io.github.livenlearnaday.countrylistkotlin.data.Result.Success
import io.github.livenlearnaday.countrylistkotlin.data.Result.Error
import io.github.livenlearnaday.countrylistkotlin.data.entity.Country
import io.github.livenlearnaday.countrylistkotlin.data.repository.CountryRepository
import io.github.livenlearnaday.countrylistkotlin.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

//background operation and state
//for better mocking of database
//repository pattern have to inject scope viewmodelscope
@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val repository: CountryRepository,
) : ViewModel() {


    private val _countries: LiveData<List<Country>> =
        repository.observeCountries().switchMap { getCountriesResult(it) }

    private val _countriesFav: LiveData<List<Country>> =
        repository.observeCountriesFav().switchMap { getCountriesFav(it) }


    val isDataLoadingError = MutableLiveData<Boolean>()


    val countries: LiveData<List<Country>> = _countries

    private fun getCountriesResult(countriesResult: Result<List<Country>>): LiveData<List<Country>> {
        val result = MutableLiveData<List<Country>>()

        when (countriesResult) {
            is Success -> {
                isDataLoadingError.value = false
                viewModelScope.launch {
                    result.value = countriesResult.data.toList()
                }
            }
            is Error -> {
                isDataLoadingError.value = true
                result.value = emptyList()

            }
            else -> {
                isDataLoadingError.value = false
            }
        }

        return result
    }

    val countriesFav: LiveData<List<Country>> = _countriesFav

    private fun getCountriesFav(countriesFavResult: Result<List<Country>>): LiveData<List<Country>> {
        val result = MutableLiveData<List<Country>>()

        when (countriesFavResult) {
            is Success -> {
                isDataLoadingError.value = false
                viewModelScope.launch {
                    result.value = countriesFavResult.data.toList()
                }
            }
            is Error -> {
                isDataLoadingError.value = true
                result.value = emptyList()

            }
            else -> {
                isDataLoadingError.value = false
            }

        }

        return result
    }


    val getCountriesFromApi =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = repository.getCountries(true)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred."))
            }
        }


    val refreshedCountries =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = repository.getCountries(true)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred."))
            }
        }


    val countriesFromDb =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = repository.getCountries(false)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred."))
            }
        }

    fun searchedCountryResult(name: String, capital: String) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = repository.getSearchedCountries(name, capital)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred."))
            }
        }


    fun favCountries() =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = repository.getFavCountries()))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred."))
            }
        }


    fun clearAllFavCountries() {
        viewModelScope.launch {
            repository.clearAllFavCountries()
        }
    }




}



