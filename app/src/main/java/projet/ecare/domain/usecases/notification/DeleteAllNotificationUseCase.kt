package projet.ecare.domain.usecases.notification

import projet.ecare.data.source.NotificationDao

class DeleteAllNotificationsUseCase(private val dao: NotificationDao) {
    suspend operator fun invoke() {
        dao.deleteAllNotifications()
    }
}