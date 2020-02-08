package alejandro.ibague.studyapp.database.entities

import alejandro.ibague.studyapp.scenes.commons.RoomDBStringConverters
import androidx.room.*

@Entity(tableName = "question_entity")
@TypeConverters(RoomDBStringConverters::class)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val type: String,
    val interrogativeSentence: String,
    val answers: List<String>?,
    var packageId: String?,
    var subjectionSentence: String?) {

    companion object {
        fun mapToObject(map: Map<String, Any>): QuestionEntity {
            val uid = map["uid"] as Long
            val type = map["type"] as String
            val interrogativeSentence = map["interrogativeSentence"] as String
            val packageId = map["packageId"] as String
            val answers = map["answers"] as List<String>
            var subjectionSentence  = map["subjectionSentence"] as String

            return QuestionEntity(
                uid = uid.toInt(),
                type = type,
                interrogativeSentence = interrogativeSentence,
                packageId = packageId,
                answers = answers,
                subjectionSentence = subjectionSentence
            )
        }
    }
}