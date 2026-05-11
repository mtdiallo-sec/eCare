package projet.ecare.presentation.ajoutModification

import projet.ecare.presentation.utilitaires.Priorite
import projet.ecare.presentation.utilitaires.Periodicite
import projet.ecare.presentation.utilitaires.Categorie

sealed class AjoutModificationEvent {
    data class OnNomChange(val value: String) : AjoutModificationEvent()
    data class OnDescriptionChange(val value: String) : AjoutModificationEvent()
    data class OnRepetitionChange(val value: String) : AjoutModificationEvent()
    data class OnHeureChange(val value: String) : AjoutModificationEvent()
    data class OnPrioriteChange(val value: Priorite) : AjoutModificationEvent()
    data class OnPeriodiciteChange(val value: Periodicite) : AjoutModificationEvent()
    data class OnCategorieChange(val value: Categorie) : AjoutModificationEvent()
    data class OnDateDebutChange(val millis: Long?) : AjoutModificationEvent()
    data class OnDateFinChange(val millis: Long?) : AjoutModificationEvent()

    data class OnLieuSelectionne(
        val lat: Double,
        val lon: Double,
        val adresse: String
    ) : AjoutModificationEvent()

    object OnEffacerLieu : AjoutModificationEvent()

    data class OnRayonChange(val value: Int) : AjoutModificationEvent()
    object OnSave : AjoutModificationEvent()
}