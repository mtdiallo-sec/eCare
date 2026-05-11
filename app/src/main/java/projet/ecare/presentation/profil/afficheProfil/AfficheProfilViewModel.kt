package projet.ecare.presentation.afficheprofil

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import projet.ecare.domain.usecases.profil.ProfilUseCases
import projet.ecare.presentation.ProfilVM
import projet.ecare.presentation.toEntity
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AfficheProfilViewModel @Inject constructor
    (
    private val profilUseCases: ProfilUseCases
) : ViewModel() {

    private val _profil = mutableStateOf<ProfilVM?>(null)
    val profil: State<ProfilVM?> = _profil

    val nomComplet = derivedStateOf {
        val p = _profil.value
        if (p == null) "" else "${p.nom} ${p.prenom}"
    }

    val ageAffiche = derivedStateOf {
        _profil.value?.age?.takeIf { it.isNotBlank() } ?: "Non renseigné"
    }

    val genreAffiche = derivedStateOf {
        _profil.value?.genre?.label ?: ""
    }

    init {
        loadProfile()
    }

    private fun loadProfile() {
        profilUseCases.getProfil()
            .onEach { entity ->
                _profil.value = entity?.let {
                    ProfilVM.fromEntity(it)
                }
            }
            .launchIn(viewModelScope)
    }

    fun deleteProfil() {
        viewModelScope.launch {
            profil.value?.let {
                profilUseCases.deleteProfil(it.toEntity())
                _profil.value = null
            }
        }
    }
}