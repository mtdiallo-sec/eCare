package projet.ecare.domain.usecases.routine

import kotlinx.coroutines.flow.Flow
import projet.ecare.data.source.RoutineDao
import projet.ecare.domain.model.RoutineEntity

class GetRoutinesUseCase(private val routinesDao : RoutineDao) {
    operator fun invoke() : Flow<List<RoutineEntity>> {
        return routinesDao.getAllRoutines()
    }
}