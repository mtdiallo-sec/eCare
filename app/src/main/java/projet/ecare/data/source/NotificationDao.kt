package projet.ecare.data.source

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import projet.ecare.domain.model.NotificationEntity

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notifications_routines ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity): Long

    @Query("UPDATE notifications_routines SET estLu = 1 WHERE id = :notificationId")
    suspend fun marquerCommeLu(notificationId: Int): Int

    @Query("SELECT COUNT(*) FROM notifications_routines WHERE estLu = 0")
    fun getUnreadCount(): Flow<Int>

    @Query("DELETE FROM notifications_routines")
    suspend fun deleteAllNotifications(): Int

    @Query("DELETE FROM notifications_routines WHERE routineId = :routineId")
    suspend fun deleteByRoutineId(routineId: Int): Int
}

