package projet.ecare.domain.usecases.notification

import projet.ecare.data.source.NotificationDao
import projet.ecare.domain.model.NotificationEntity

class AddNotificationUseCase(private val dao: NotificationDao) {
    suspend operator fun invoke(notification: NotificationEntity) {
        dao.insertNotification(notification)
    }
}