package alejandro.ibague.studyapp.database.daos

import alejandro.ibague.studyapp.database.entities.PackageEntity
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PackageDao {
    @Query("SELECT * FROM package_entity")
    fun getAll(): LiveData<List<PackageEntity>>

    @Query("SELECT * FROM package_entity WHERE package_entity.user_id=:userId or package_entity.isPublicAccess = 1")
    fun getAllPublicId(userId: String?): LiveData<List<PackageEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg packageEntity: PackageEntity): LongArray

    @Query("""
           SELECT * FROM package_entity 
           WHERE package_entity.user_id=:userId
           """)
    fun findPackageByUser(userId: String): LiveData<List<PackageEntity>>

    @Query("SELECT * FROM package_entity WHERE package_entity.identifier=:packageId")
    fun findInformationOf(packageId: String): LiveData<PackageEntity>

    @Query("DELETE FROM package_entity")
    fun removeAll()

    @Query("SELECT * FROM package_entity WHERE package_entity.name=:namePack AND package_entity.user_id=:userId")
    fun verifyPackageName(namePack: String, userId: String?): LiveData<List<PackageEntity>>
}