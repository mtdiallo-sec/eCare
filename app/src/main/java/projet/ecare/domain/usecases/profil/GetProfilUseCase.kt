package projet.ecare.domain.usecases.profil

import projet.ecare.data.source.ProfilDao

class GetProfilUseCase(private val profilDao: ProfilDao) {
    operator fun invoke() =
        profilDao.getProfil()
}