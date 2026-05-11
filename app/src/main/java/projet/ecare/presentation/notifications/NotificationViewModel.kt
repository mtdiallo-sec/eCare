package projet.ecare.presentation.notifications

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import projet.ecare.domain.usecases.notification.NotificationUseCases
import projet.ecare.presentation.NotificationVM
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationUseCases: NotificationUseCases
) : ViewModel() {

    private val _notifications = mutableStateOf<List<NotificationVM>>(emptyList())
    val notifications: State<List<NotificationVM>> = _notifications

    private val _unreadCount = mutableStateOf(0)
    val unreadCount: State<Int> = _unreadCount

    init {
        notificationUseCases.getNotifications()
            .onEach { entities ->
                _notifications.value = entities.map { NotificationVM.fromEntity(it) }
            }
            .launchIn(viewModelScope)

        notificationUseCases.getUnreadCount()
            .onEach { count ->
                _unreadCount.value = count
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: NotificationEvent) {
        when (event) {
            is NotificationEvent.OnMarquerCommeLu -> {
                viewModelScope.launch {
                    notificationUseCases.markAsRead(event.id)
                }
            }
            is NotificationEvent.OnSupprimerParRoutine -> {
                viewModelScope.launch {
                    notificationUseCases.deleteByRoutine(event.routineId)
                }
            }
            NotificationEvent.OnToutEffacer -> {
                viewModelScope.launch {
                    notificationUseCases.deleteAllNotifications()
                }
            }
        }
    }
}