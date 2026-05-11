package projet.ecare.presentation.ajoutModification

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import projet.ecare.domain.usecases.routine.RoutinesUseCases
import projet.ecare.presentation.utilitaires.Periodicite
import projet.ecare.presentation.RoutineVM
import projet.ecare.presentation.toEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import android.content.Context
import android.location.Geocoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import projet.ecare.presentation.notifications.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AjoutModificationViewModel @Inject constructor(
    private val routinesUseCases: RoutinesUseCases,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    var state by mutableStateOf(RoutineVM())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var suggestions by mutableStateOf<List<android.location.Address>>(emptyList())
        private set

    fun loadRoutine(id: Int) {
        viewModelScope.launch {
            routinesUseCases.getRoutine(id)?.let { entity ->
                state = RoutineVM.fromEntity(entity)
            }
        }
    }

    fun onEvent(event: AjoutModificationEvent) {
        when (event) {
            is AjoutModificationEvent.OnNomChange -> state = state.copy(nom = event.value)
            is AjoutModificationEvent.OnDescriptionChange -> state = state.copy(description = event.value)
            is AjoutModificationEvent.OnCategorieChange -> state = state.copy(categorie = event.value)
            is AjoutModificationEvent.OnPrioriteChange -> state = state.copy(priorite = event.value)
            is AjoutModificationEvent.OnPeriodiciteChange -> {
                val newDateFin = if (event.value == Periodicite.PONCTUELLE) null else state.dateFin
                state = state.copy(periodicite = event.value, dateFin = newDateFin)
            }
            is AjoutModificationEvent.OnDateDebutChange -> {
                state = state.copy(dateDebut = event.millis?.toLocalDate() ?: LocalDate.now())
            }
            is AjoutModificationEvent.OnDateFinChange -> {
                state = state.copy(dateFin = event.millis?.toLocalDate())
            }
            is AjoutModificationEvent.OnHeureChange -> state = state.copy(heure = event.value)
            is AjoutModificationEvent.OnRepetitionChange -> state = state.copy(repetition = event.value)

            is AjoutModificationEvent.OnLieuSelectionne -> {
                state = state.copy(
                    latitude = event.lat,
                    longitude = event.lon,
                    adresse = event.adresse
                )
                suggestions = emptyList()
            }

            is AjoutModificationEvent.OnEffacerLieu -> {
                state = state.copy(
                    latitude = null,
                    longitude = null,
                    adresse = null
                )
                suggestions = emptyList()
            }

            is AjoutModificationEvent.OnRayonChange -> {
                state = state.copy(rayonMetres = event.value)
            }

            is AjoutModificationEvent.OnSave -> { }

        }
    }

    fun resetState() {
        state = RoutineVM()
        errorMessage = null
    }

    fun onSave(context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val entity = state.toEntity()

                val generatedId = routinesUseCases.upsertRoutine(entity)

                val routineFinal = if (entity.id == 0) { 
                    entity.copy(id = generatedId.toInt())
                } else {
                    entity
                }

                alarmScheduler.schedule(routineFinal)

                errorMessage = null
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    private var searchJob: kotlinx.coroutines.Job? = null

    fun searchAddress(query: String, context: Context) {
        searchJob?.cancel()

        if (query.isBlank() || query.length < 3) {
            suggestions = emptyList()
            return
        }

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                kotlinx.coroutines.delay(300)

                val geocoder = Geocoder(context, Locale.getDefault())
                val results = geocoder.getFromLocationName(query, 5)

                withContext(Dispatchers.Main) {
                    suggestions = results ?: emptyList()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    suggestions = emptyList()
                }
            }
        }
    }

    private fun Long.toLocalDate(): LocalDate {
        return Instant.ofEpochMilli(this)
            .atZone(ZoneId.of("UTC"))
            .toLocalDate()
    }
}