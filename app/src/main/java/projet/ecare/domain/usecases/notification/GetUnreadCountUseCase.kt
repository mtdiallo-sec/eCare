package projet.ecare.domain.usecases.notification

import kotlinx.coroutines.flow.Flow
import projet.ecare.data.source.NotificationDao

class GetUnreadCountUseCase(private val dao: NotificationDao) {
    operator fun invoke(): Flow<Int> {
        return dao.getUnreadCount()
    }
}