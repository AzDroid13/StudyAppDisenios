package alejandro.ibague.studyapp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subject_entity")
data class SubjectEntity(@PrimaryKey var identifier: String,
                         val name: String,
                         val color: Int?,
                         var guess: Int?,
                         var packages: Int?,
                         var qAndA: Int?,
                         var active: Boolean,
                         @ColumnInfo(name = "user_id") val userId: String) {
    companion object {
        fun mapToObject(identifier: String, map: Map<String, Any>): SubjectEntity {
            val name = map["name"] as String
            val color = map["color"] as Int
            val guess = map["guess"] as Int
            val packages = map["packages"] as Int
            val qAndA = map["qAndA"] as Int
            val active = map["active"] as Boolean
            val userId = map["userId"] as String

            return SubjectEntity(
                name = name,
                identifier = identifier,
                color = color,
                guess = guess,
                packages = packages,
                qAndA = qAndA,
                active = active,
                userId = userId
            )
        }
    }
}