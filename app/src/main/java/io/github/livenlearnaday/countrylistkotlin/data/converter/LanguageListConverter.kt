package io.github.livenlearnaday.countrylistkotlin.data.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.livenlearnaday.countrylistkotlin.data.entity.Language
import java.util.*

class LanguageListConverter {


    @TypeConverter
    fun stringToLanguageObjectList(data: String?): MutableList<Language?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<Language?>?>() {}.type
        val gson = Gson()
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun languageObjectListToString(someObjects: List<Language?>?): String? {
        val gson = Gson()
        return gson.toJson(someObjects)
    }

}