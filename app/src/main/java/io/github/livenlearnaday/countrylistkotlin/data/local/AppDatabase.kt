package io.github.livenlearnaday.countrylistkotlin.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null



        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildRoomDB(context).also { INSTANCE = it }

            }


        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "countrylistkotlin_db"
            )
                .fallbackToDestructiveMigration()  // no migrations and clear database when upgrade the version
                .build()
    }





}


