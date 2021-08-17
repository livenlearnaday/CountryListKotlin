package io.github.livenlearnaday.countrylistkotlin.data.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.livenlearnaday.countrylistkotlin.data.entity.CallingCode
import java.util.*

class CallingCodeListConverter {


    @TypeConverter
     fun stringToCallingCodeObjectList(data: String?): MutableList<CallingCode?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<CallingCode?>?>() {}.type
        val gson = Gson()
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun CallingCodeObjectListToString(someObjects: List<CallingCode?>?): String? {
        val gson = Gson()
        return gson.toJson(someObjects)
    }


}