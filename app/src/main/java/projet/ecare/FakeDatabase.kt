package projet.ecare

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import projet.ecare.data.source.RoutineDao
import projet.ecare.data.source.ProfilDao
import projet.ecare.domain.model.RoutineEntity
import projet.ecare.domain.model.ProfilEntity

class FakeDatabase : RoutineDao, ProfilDao {

    private val routines = mutableListOf<RoutineEntity>()

    private var profil: ProfilEntity? = null

    //Partie Routine
    override fun getAllRoutines(): Flow<List<RoutineEntity>> = flow {
        emit(routines.toList())
    }

    override suspend fun upsertRoutine(routine: RoutineEntity): Long {
        val index = routines.indexOfFirst { it.id == routine.id }

        return if (index != -1) {
            routines[index] = routine
            routine.id.toLong()
        } else {
            val newId = if (routine.id == 0) (routines.maxOfOrNull { it.id } ?: 0) + 1 else routine.id
            val entityWithId = routine.copy(id = newId)
            routines.add(entityWithId)
            newId.toLong()
        }
    }

    override suspend fun deleteRoutine(routine: RoutineEntity): Int {
        return if (routines.removeIf { it.id == routine.id }) 1 else 0
    }

    override suspend fun getRoutineById(id: Int): RoutineEntity? {
        return routines.find { it.id == id }
    }

    //Partie Profil
    override suspend fun upsertProfil(profilEntity: ProfilEntity): Long {
        profil = profilEntity
        return 1
    }


    override fun getProfil(): Flow<ProfilEntity?> = flow {
        emit(profil)
    }

    override suspend fun deleteProfil(profil: ProfilEntity): Int {
        return if (this.profil?.id == profil.id) {
            this.profil = null
            1
        } else {
            0
        }
    }
}