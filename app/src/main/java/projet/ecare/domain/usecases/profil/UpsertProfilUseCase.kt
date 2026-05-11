package projet.ecare.domain.usecases.profil

import projet.ecare.data.source.ProfilDao
import projet.ecare.domain.exception.EcareException
import projet.ecare.domain.model.ProfilEntity

class UpsertProfilUseCase(private val profilDao: ProfilDao) {

    @Throws(EcareException::class)
    suspend operator fun invoke(profil: ProfilEntity) {

        if (profil.nom.trim().isBlank()) {
            throw EcareException("Le nom du profil est obligatoire.")
        }

        if (profil.prenom.trim().isBlank()) {
            throw EcareException("Le prenom du profil est obligatoire.")
        }

        val age = profil.age
        if (age != null && age < 0) {
            throw EcareException("L'age ne peut pas etre negatif.")
        }

        try {
            profilDao.upsertProfil(profil)
        } catch (e: Exception) {
            throw EcareException("Erreur lors de l'enregistrement dans la base de données.")
        }

    }
}