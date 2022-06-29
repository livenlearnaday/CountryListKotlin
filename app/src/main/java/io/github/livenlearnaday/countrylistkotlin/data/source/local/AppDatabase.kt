package io.github.livenlearnaday.countrylistkotlin.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.livenlearnaday.countrylistkotlin.data.converter.*
import io.github.livenlearnaday.countrylistkotlin.data.entity.*


@Database(
    entities = [Country::class
    ], version = 1, exportSchema = false
)
@TypeConverters(DateConverter::class, ListConverter::class, LanguageListConverter::class, CallingCodeListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun countryDao(): CountryDao



}
