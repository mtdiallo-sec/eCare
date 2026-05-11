package projet.ecare.domain.usecases.notification

import projet.ecare.data.source.NotificationDao

class DeleteByRoutineUseCase(private val dao: NotificationDao) {
    suspend operator fun invoke(routineId: Int) {
        dao.deleteByRoutineId(routineId)
    }
}