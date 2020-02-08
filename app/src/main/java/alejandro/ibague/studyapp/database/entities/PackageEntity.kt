package alejandro.ibague.studyapp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "package_entity")
data class PackageEntity(@PrimaryKey var identifier: String,
                         val name: String,
                         val description: String?,
                         val category: String,
                         val isPublicAccess: Boolean,
                         val price: Float,
                         var thumbnailImage: String,
                         @ColumnInfo(name = "user_id") val userId: String) {
    companion object {
        fun mapToObject(identifier: String, map: Map<String, Any>): PackageEntity {
            val name = map["name"] as String
            val image = map["thumbnailImage"] as String
            val cost = map["price"] as Long
            val description = map["description"] as String
            val accessor = map["publicAccess"] as Boolean
            val category = map["category"] as String
            val userId = map["userId"] as String

            return PackageEntity(
                name = name,
                description = description,
                category = category,
                isPublicAccess = accessor,
                price = cost.toFloat(),
                thumbnailImage = image,
                identifier = identifier,
                userId = userId
            )
        }
    }
}