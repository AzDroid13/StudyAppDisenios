package alejandro.ibague.studyapp.scenes.commons

import androidx.room.TypeConverter
import com.google.gson.Gson

class RoomDBStringConverters {
    @TypeConverter
    fun listToJson(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<String>? {
        val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
        return objects.toList()
    }
}

fun String.isValidEmail() =
    this.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
