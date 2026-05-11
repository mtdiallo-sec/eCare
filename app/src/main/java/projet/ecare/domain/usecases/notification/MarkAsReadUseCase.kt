package projet.ecare.domain.usecases.notification

import projet.ecare.data.source.NotificationDao

class MarkAsReadUseCase(private val dao: NotificationDao) {
    suspend operator fun invoke(id: Int) {
        dao.marquerCommeLu(id)
    }
}