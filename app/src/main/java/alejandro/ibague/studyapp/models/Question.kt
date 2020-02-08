package alejandro.ibague.studyapp.models

import alejandro.ibague.studyapp.database.entities.QuestionEntity
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Question(val identifier: String?, var statement:String,
                    var options:HashMap<String, Boolean>?, val type: Type) {

    enum class Type {
        ABC_Type,
        BINARY,
        MULTIPLE,
        OPEN_ANSWER
    }
}

@Parcelize
class QuestionEntityList: ArrayList<QuestionEntity>(), Parcelable