package projet.ecare.domain.usecases.notification

import kotlinx.coroutines.flow.Flow
import projet.ecare.data.source.NotificationDao
import projet.ecare.domain.model.NotificationEntity

class GetNotificationsUseCase(private val dao: NotificationDao) {
    operator fun invoke(): Flow<List<NotificationEntity>> {
        return dao.getAllNotifications()
    }
}