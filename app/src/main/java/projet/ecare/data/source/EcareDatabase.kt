package projet.ecare.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import projet.ecare.domain.model.RoutineEntity
import projet.ecare.domain.model.ProfilEntity
import projet.ecare.domain.model.NotificationEntity

@Database(
    entities = [
        RoutineEntity::class,
        ProfilEntity::class,
        NotificationEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class EcareDatabase : RoomDatabase() {

    abstract fun routineDao(): RoutineDao

    abstract fun profilDao(): ProfilDao

    abstract fun notificationDao(): NotificationDao

    companion object {
        const val DATABASE_NAME = "eCare.db"

        @Volatile
        private var INSTANCE: EcareDatabase? = null

        fun getDatabase(context: Context): EcareDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EcareDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}