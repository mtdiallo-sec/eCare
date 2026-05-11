package projet.ecare.data.source

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import projet.ecare.domain.model.ProfilEntity

@Dao
interface ProfilDao {

    @Query("SELECT * FROM profil LIMIT 1")
    fun getProfil(): Flow<ProfilEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfil(profil: ProfilEntity): Long

    @Delete
    suspend fun deleteProfil(profil: ProfilEntity): Int
}