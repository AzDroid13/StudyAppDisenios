package alejandro.ibague.studyapp.database

import alejandro.ibague.studyapp.database.daos.PackageDao
import alejandro.ibague.studyapp.database.daos.QuestionDao
import alejandro.ibague.studyapp.database.daos.SubjectDAO
import alejandro.ibague.studyapp.database.daos.UserDao
import alejandro.ibague.studyapp.database.entities.PackageEntity
import alejandro.ibague.studyapp.database.entities.QuestionEntity
import alejandro.ibague.studyapp.database.entities.SubjectEntity
import alejandro.ibague.studyapp.database.entities.UserEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PackageEntity::class, QuestionEntity::class, UserEntity::class, SubjectEntity::class],
          version = 2)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun questionDao(): QuestionDao
    abstract fun packageDao(): PackageDao
    abstract fun subjectDAO(): SubjectDAO

    companion object {
        private lateinit var appDatabase: AppDatabase

        fun getDatabase(context: Context): AppDatabase {
            synchronized(AppDatabase::class) {
                appDatabase = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "study-app-database")
                    .fallbackToDestructiveMigration() //Temporal code just for develop.
                    .build()
            }

            return appDatabase
        }
    }
}