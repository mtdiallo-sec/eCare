package projet.ecare.domain.usecases.routine

data class RoutinesUseCases (
    val getRoutines : GetRoutinesUseCase,
    val getRoutine : GetRoutineUseCase,
    val upsertRoutine : UpsertRoutineUseCase,
    val deleteRoutine : DeleteRoutineUseCase
)