package projet.ecare.domain.usecases.notification

data class NotificationUseCases(
    val getNotifications: GetNotificationsUseCase,
    val markAsRead: MarkAsReadUseCase,
    val getUnreadCount: GetUnreadCountUseCase,
    val addNotification: AddNotificationUseCase,
    val deleteByRoutine: DeleteByRoutineUseCase,
    val deleteAllNotifications: DeleteAllNotificationsUseCase
)