package projet.ecare.domain.usecases.profil

import projet.ecare.data.source.ProfilDao
import projet.ecare.domain.model.ProfilEntity
class DeleteProfilUseCase(
    private val profilDao: ProfilDao
) {
    suspend operator fun invoke(profil: ProfilEntity) {
        profilDao.deleteProfil(profil)
    }
}