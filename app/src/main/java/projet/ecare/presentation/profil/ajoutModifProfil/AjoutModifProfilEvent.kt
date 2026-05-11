package projet.ecare.presentation.profil.ajoutModifProfil

import projet.ecare.presentation.utilitaires.Genre

sealed class AjoutModifProfilEvent {
    data class OnNomChange(val value: String) : AjoutModifProfilEvent()
    data class OnPrenomChange(val value: String) : AjoutModifProfilEvent()
    data class OnAgeChange(val value: String) : AjoutModifProfilEvent()
    data class OnGenreChange(val value: Genre) : AjoutModifProfilEvent()
    object OnSave : AjoutModifProfilEvent()
}