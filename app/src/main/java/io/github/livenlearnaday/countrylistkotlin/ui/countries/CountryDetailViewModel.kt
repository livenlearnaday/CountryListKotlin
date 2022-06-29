package io.github.livenlearnaday.countrylistkotlin.ui.countries

import android.widget.CheckBox
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.livenlearnaday.countrylistkotlin.data.entity.Country
import io.github.livenlearnaday.countrylistkotlin.data.repository.CountryRepository
import io.github.livenlearnaday.countrylistkotlin.utils.Resource
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class CountryDetailViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {


     fun getCountry(name: String) =
         liveData(Dispatchers.IO) {
             emit(Resource.loading(data = null))
             try {
                 emit(Resource.success(data = repository.getCountry(name)))
             } catch (exception: Exception) {
                 emit(Resource.error(data = null, message = exception.message ?: "Error Occurred."))
             }
         }



    fun setUpFavToggle(checkBox: CheckBox, country: Country) {
        checkBox.setOnCheckedChangeListener { _, b ->
            country.favorite = b
            updateFavCountry(b,country.name)
        }

        checkBox.isChecked = country.favorite
    }


    fun updateFavCountry(fav:Boolean, countryName:String){
        viewModelScope.launch {
            repository.updateCountryFavLocalDataSource(fav, countryName)
        }
    }


}
