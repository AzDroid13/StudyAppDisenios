package alejandro.ibague.studyapp.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "user_entity")
@Parcelize
data class UserEntity(
    @PrimaryKey var identifier: String,
    var name: String?,
    var email: String?,
    var password: String?,
 //   val interest: List<String>?,
    var username: String?,
    var rol: String?,
    var country: String?,
    var thumbnailImage: String?): Parcelable {

    companion object {
        fun mapToObject(identifier: String, map: Map<String, Any>): UserEntity {
            val dbEmail = map["email"] as String
            val dbPass = map["password"] as String
            val dbCountry = map["country"] as String
            val dbName = map["name"] as String
            val image = map["thumbnailImage"] as? String
            val username = map["username"] as? String
            val rol = map["rol"] as? String
          //  val dbInterest = map["insterest"] as List<String>
            return UserEntity(identifier, dbName, dbEmail.toLowerCase(), dbPass.toLowerCase(), dbCountry, image, username, rol)
        }
    }
}