package projet.ecare.presentation.detailsRoutine

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import projet.ecare.presentation.RoutineVM
import projet.ecare.presentation.toEntity
import projet.ecare.domain.usecases.routine.RoutinesUseCases
import projet.ecare.presentation.notifications.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsRoutineViewModel @Inject constructor(
    private val routinesUseCases: RoutinesUseCases,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    private val _routine = mutableStateOf<RoutineVM?>(null)
    val routine: State<RoutineVM?> = _routine

    fun loadRoutineById(id: Int) {
        if (id <= 0) return

        viewModelScope.launch {
            val entity = routinesUseCases.getRoutine(id)
            _routine.value = entity?.let { RoutineVM.fromEntity(it) }
        }
    }

    fun deleteRoutine(context: Context, routineVM: RoutineVM) {
        viewModelScope.launch {
            val entityToDelete = routineVM.toEntity()
            alarmScheduler.cancel(routineVM.id)
            routinesUseCases.deleteRoutine(entityToDelete)
        }
    }
}