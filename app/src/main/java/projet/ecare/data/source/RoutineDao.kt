package projet.ecare.data.source

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import projet.ecare.domain.model.RoutineEntity

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routines ORDER BY id DESC")
    fun getAllRoutines(): Flow<List<RoutineEntity>>

    @Upsert
    suspend fun upsertRoutine(routine: RoutineEntity): Long

    @Delete
    suspend fun deleteRoutine(routine: RoutineEntity): Int

    @Query("SELECT * FROM routines WHERE id = :id")
    suspend fun getRoutineById(id: Int): RoutineEntity?
}