package projet.ecare.presentation.profil.ajoutModifProfil

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import projet.ecare.domain.usecases.profil.ProfilUseCases
import projet.ecare.presentation.ProfilVM
import projet.ecare.presentation.toEntity
import javax.inject.Inject

@HiltViewModel
class AjoutModifProfilViewModel @Inject constructor
    (
    private val profilUseCases: ProfilUseCases
) : ViewModel() {

    var state by mutableStateOf(ProfilVM())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isEditMode by mutableStateOf(false)
        private set

    fun loadProfil() {
        viewModelScope.launch {
            profilUseCases.getProfil().collect { entity ->
                if (entity != null) {
                    state = ProfilVM.fromEntity(entity)
                    isEditMode = true
                } else {
                    state = ProfilVM()
                    isEditMode = false
                }
            }
        }
    }

    fun onEvent(event: AjoutModifProfilEvent) {
        when (event) {
            is AjoutModifProfilEvent.OnNomChange ->
                state = state.copy(nom = event.value)

            is AjoutModifProfilEvent.OnPrenomChange ->
                state = state.copy(prenom = event.value)

            is AjoutModifProfilEvent.OnAgeChange ->
                state = state.copy(age = event.value)

            is AjoutModifProfilEvent.OnGenreChange ->
                state = state.copy(genre = event.value)

            is AjoutModifProfilEvent.OnSave ->
                save()
        }
    }

    private fun save() {
        viewModelScope.launch {
            try {
                profilUseCases.upsertProfil(state.toEntity())
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

}