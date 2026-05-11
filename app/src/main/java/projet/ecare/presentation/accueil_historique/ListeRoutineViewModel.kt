package projet.ecare.presentation.accueil_historique

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import projet.ecare.domain.usecases.routine.RoutinesUseCases
import projet.ecare.presentation.utilitaires.Periodicite
import projet.ecare.presentation.utilitaires.calculerDistancePosition
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import projet.ecare.presentation.RoutineVM
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.LocalTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import projet.ecare.domain.usecases.profil.ProfilUseCases
import javax.inject.Inject

data class RoutineUiState(
    val item: RoutineVM,
    val isActive: Boolean
)

@HiltViewModel
class ListeRoutineViewModel @Inject constructor
    (
    val routinesUseCases: RoutinesUseCases,
    val profilUseCases: ProfilUseCases
) : ViewModel() {

    private val _tick = mutableStateOf(0L)
    private val _routines = mutableStateOf<List<RoutineVM>>(emptyList())

    private val _nomProfil = mutableStateOf("")

    private val _positionActuelle = mutableStateOf<Pair<Double, Double>?>(null)

    val messageBonjour = derivedStateOf {
        _tick.value
        val heure = LocalTime.now(ZoneId.systemDefault()).hour
        val base = if (heure in 5..17) "Bonjour" else "Bonsoir"

        if (_nomProfil.value.isBlank()) base else "$base ${_nomProfil.value}"
    }

    val routinesAccueil = derivedStateOf {

        _tick.value

        _routines.value
            .filter { !estTerminee(it) }
            .map { vm ->
                RoutineUiState(
                    item = vm,
                    isActive = calculerSiActive(vm)
                )
            }
    }
    val routinesHistorique = derivedStateOf {
        _routines.value.filter { estTerminee(it) }
    }
    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading
    val nombreActivesDuJour = derivedStateOf {
        routinesAccueil.value.count { it.isActive }
    }

    init {
        loadRoutines()
        observeProfil()
        startTimer()
    }

    fun updatePosition(lat: Double, lon: Double) {
        _positionActuelle.value = Pair(lat, lon)
    }

    private fun observeProfil() {
        profilUseCases.getProfil()
            .onEach { profil ->
                _nomProfil.value = profil?.nom ?: ""
            }
            .launchIn(viewModelScope)
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (true) {
                delay(1000L)
                _tick.value = System.currentTimeMillis()
            }
        }
    }

    private var job: Job? = null
    private fun loadRoutines() {
        job?.cancel()
        _isLoading.value = true

        job = routinesUseCases.getRoutines()
            .onEach { listeEntities ->
                val vms = listeEntities.map { RoutineVM.fromEntity(it) }

                _routines.value = vms
                _isLoading.value = false
            }
            .launchIn(viewModelScope)
    }

    private fun calculerSiActive(r: RoutineVM): Boolean {
        val maintenant = LocalDateTime.now(ZoneId.systemDefault())
        val aujourdhui = maintenant.toLocalDate()
        val heureActuelle = maintenant.toLocalTime()

        if (aujourdhui.isBefore(r.dateDebut)) {
            return false
        }

        val estLeBonJour = when (r.periodicite) {
            Periodicite.QUOTIDIENNE -> true
            Periodicite.HEBDOMADAIRE ->
                r.repetition.equals(
                    aujourdhui.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.FRENCH),
                    true
                )
            Periodicite.PONCTUELLE ->
                r.dateDebut == aujourdhui
        }

        if (!estLeBonJour) return false

        val heureRoutine = try {
            LocalTime.parse(r.heure)
        } catch (e: Exception) {
            LocalTime.MIN
        }

        val tempsEcoule = heureActuelle.isAfter(heureRoutine)

        val lat = r.latitude
        val lon = r.longitude

        val estAuBonEndroit = if (lat == null || lon == null) {
            true
        } else {
            val pos = _positionActuelle.value
            if (pos == null) {
                true
            } else {
                val (currentlat, currentlon) = pos
                calculerDistancePosition(
                    currentlat,
                    currentlon,
                    lat,
                    lon
                ) <= r.rayonMetres
            }
        }


        val estDejaPasseeAujourdhui = tempsEcoule && estAuBonEndroit

        return !estDejaPasseeAujourdhui
    }

    private fun estTerminee(r: RoutineVM): Boolean {
        val aujourdhui = LocalDate.now(ZoneId.systemDefault())

        if (r.dateFin != null && r.dateFin.isBefore(aujourdhui)) return true

        if (r.periodicite == Periodicite.PONCTUELLE && r.dateDebut.isBefore(aujourdhui))
        {
            return true
        }
        return false
    }
    val messageBienvenue = derivedStateOf {
        val nb = nombreActivesDuJour.value
        when {
            nb == 0 -> "Aujourd’hui, aucune routine prévue"
            nb == 1 -> "Aujourd’hui, 1 routine prévue"
            else -> "Aujourd’hui, $nb routines prévues"
        }
    }

}
