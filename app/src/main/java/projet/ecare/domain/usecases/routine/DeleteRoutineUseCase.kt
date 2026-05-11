package projet.ecare.domain.usecases.routine

import projet.ecare.data.source.RoutineDao
import projet.ecare.domain.exception.EcareException
import projet.ecare.domain.model.RoutineEntity

class DeleteRoutineUseCase (private val routinesDao : RoutineDao) {
    @Throws(EcareException::class)
    suspend operator fun invoke(story: RoutineEntity) {
        routinesDao.deleteRoutine(story)

    }
}