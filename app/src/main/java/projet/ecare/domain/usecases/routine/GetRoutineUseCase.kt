package projet.ecare.domain.usecases.routine

import projet.ecare.data.source.RoutineDao
import projet.ecare.domain.model.RoutineEntity

class GetRoutineUseCase(private val routinesDao : RoutineDao) {
    suspend operator fun invoke(routineId: Int) : RoutineEntity? {
        return routinesDao.getRoutineById(routineId)
    }
}