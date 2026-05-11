package projet.ecare.presentation

import projet.ecare.domain.model.NotificationEntity

data class NotificationVM(
    val id: Int = 0,
    val routineId: Int = 0,
    val titre: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val estLu: Boolean = false,
    val priorite: String
) {
    companion object {
        fun fromEntity(entity: NotificationEntity): NotificationVM {
            return NotificationVM(
                id = entity.id,
                routineId = entity.routineId,
                titre = entity.titre,
                message = entity.message,
                timestamp = entity.timestamp,
                estLu = entity.estLu,
                priorite = entity.priorite
            )
        }
    }
}

fun NotificationVM.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = this.id,
        routineId = this.routineId,
        titre = this.titre,
        message = this.message,
        timestamp = this.timestamp,
        estLu = this.estLu,
        priorite = this.priorite
    )
}