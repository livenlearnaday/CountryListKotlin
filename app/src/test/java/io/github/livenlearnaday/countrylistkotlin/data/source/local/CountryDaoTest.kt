/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.livenlearnaday.countrylistkotlin.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import io.github.livenlearnaday.countrylistkotlin.MainCoroutineRule
import io.github.livenlearnaday.countrylistkotlin.data.entity.Country
import io.github.livenlearnaday.countrylistkotlin.data.entity.Language
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class CountryDaoTest {

    private lateinit var database: AppDatabase

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()




    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertCountriesAndGetAllCountries() = runBlockingTest {
        // GIVEN - insert data

        val country1 = Country("Country001","Capital001","Region001","Subregion001","https://picsum.photos/200", listOf(Language("LanguageName001","LanguageNativeName001")),listOf("0001"))
        val country2= Country("Country002","Capital002","Region002","Subregion002","https://picsum.photos/200", listOf(Language("LanguageName002","LanguageNativeName002")),listOf("0002"))
        val country3= Country("Country003","Capital003","Region003","Subregion003","https://picsum.photos/200", listOf(Language("LanguageName003","LanguageNativeName003")),listOf("0003"))
        val countries:List<Country> = listOf(country1,country2,country3)
        database.countryDao().insertCountries(countries)

        // WHEN - Get the countries from the database
        val loaded = database.countryDao().getAllCountries()

        // THEN - The loaded data contains the expected values
        assertThat(loaded.size, equalTo(3))
        assertThat(loaded[0].name, equalTo(countries[0].name))
        assertThat(loaded[1].name, equalTo(countries[1].name))
        assertThat(loaded[2].name, equalTo(countries[2].name))
    }




    @Test
    fun updateSomeCountriesFavToTrueAndClearFav() = runBlockingTest {
        val country1 = Country("Country001","Capital001","Region001","Subregion001","https://picsum.photos/200", listOf(Language("LanguageName001","LanguageNativeName001")),listOf("0001"))
        val country2= Country("Country002","Capital002","Region002","Subregion002","https://picsum.photos/200", listOf(Language("LanguageName002","LanguageNativeName002")),listOf("0002"))
        val country3= Country("Country003","Capital003","Region003","Subregion003","https://picsum.photos/200", listOf(Language("LanguageName003","LanguageNativeName003")),listOf("0003"))

        database.countryDao().insertCountries(listOf(country1,country2,country3))

        // When the country favorite
        database.countryDao().updateCountryFav(true, "Country001")
        database.countryDao().updateCountryFav(true, "Country003")
        database.countryDao().clearAllFavCountries()

        // THEN - The loaded data contains the expected values
        val countriesFavReset = database.countryDao().getAllCountries()
        assertThat(countriesFavReset[0].favorite, equalTo(false))
        assertThat(countriesFavReset[2].favorite, equalTo(false))

    }


    @Test
    fun deleteCountriesAndGettingCountries() = runBlockingTest {
        val country1 = Country("Country001","Capital001","Region001","Subregion001","https://picsum.photos/200", listOf(Language("LanguageName001","LanguageNativeName001")),listOf("0001"))
        val country2= Country("Country002","Capital002","Region002","Subregion002","https://picsum.photos/200", listOf(Language("LanguageName002","LanguageNativeName002")),listOf("0002"))
        val country3= Country("Country003","Capital003","Region003","Subregion003","https://picsum.photos/200", listOf(Language("LanguageName003","LanguageNativeName003")),listOf("0003" ))
        val countries:List<Country> = listOf(country1,country2,country3)
        database.countryDao().insertCountries(countries)

        // When deleting a task by id
        database.countryDao().clearCountryTable()

        // THEN - The list is empty
        val getAllCountriesAfterClearingTable = database.countryDao().getAllCountries()
        assertThat(getAllCountriesAfterClearingTable.isEmpty(), equalTo(true))
    }


}
