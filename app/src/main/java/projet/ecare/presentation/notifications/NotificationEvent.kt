package projet.ecare.presentation.notifications

sealed class NotificationEvent {
    data class OnMarquerCommeLu(val id: Int) : NotificationEvent()

    data class OnSupprimerParRoutine(val routineId: Int) : NotificationEvent()

    object OnToutEffacer : NotificationEvent()
}