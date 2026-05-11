package projet.ecare.domain.usecases.profil

data class ProfilUseCases (
    val getProfil : GetProfilUseCase,
    val upsertProfil : UpsertProfilUseCase,
    val deleteProfil : DeleteProfilUseCase
)