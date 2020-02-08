package alejandro.ibague.studyapp.database.daos

import alejandro.ibague.studyapp.database.entities.UserEntity
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user_entity")
    fun getAll(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user_entity WHERE identifier =:userId")
    fun findUserInformationById(userId: String): LiveData<UserEntity>

    @Query("SELECT * FROM user_entity WHERE email =:name AND password =:password")
    fun findUser(name: String, password: String): LiveData<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg questionEntity: UserEntity): LongArray

    @Query("DELETE FROM user_entity")
    fun removeAll()
}