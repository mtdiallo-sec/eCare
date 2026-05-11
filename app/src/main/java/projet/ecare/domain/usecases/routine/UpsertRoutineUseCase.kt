package projet.ecare.domain.usecases.routine

import projet.ecare.data.source.RoutineDao
import projet.ecare.domain.exception.EcareException
import projet.ecare.domain.model.RoutineEntity
import projet.ecare.presentation.utilitaires.Categorie
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.time.ZoneId
import java.time.LocalDateTime
import java.time.LocalTime

class UpsertRoutineUseCase(private val dao: RoutineDao) {

    @Throws(EcareException::class)
    suspend operator fun invoke(routine: RoutineEntity): Long {

        val aujourdhui = LocalDate.now(ZoneId.systemDefault())
        val maintenant = LocalDateTime.now(ZoneId.systemDefault())
        val heureActuelle = maintenant.toLocalTime()

        val heureRoutine = try {
            LocalTime.parse(routine.heure)
        } catch (e: DateTimeParseException) {
            throw EcareException("Le format de l'heure est incorrect.")
        }

        val debut = parseDate(routine.dateDebut)
            ?: throw EcareException("Le format de la date de début est incorrect.")

        if (routine.nom.trim().isBlank()) {
            throw EcareException("Le nom de la routine est obligatoire.")
        }

        if (routine.categorie == Categorie.CHOISIR_CATEGORIE) {
            throw EcareException("Veuillez sélectionner une catégorie pour cette routine.")
        }

        if (routine.dateDebut.isBlank()) {
            throw EcareException("La date de début est obligatoire.")
        }

        if (routine.id == 0 && debut.isBefore(aujourdhui)) {
            throw EcareException("La date de début ne peut pas être dans le passé.")
        }

        if (!routine.dateFin.isNullOrBlank()) {
            val fin = parseDate(routine.dateFin)
            if (fin == null) {
                throw EcareException("Le format de la date de fin est incorrect.")
            }
            if (fin.isBefore(debut)) {
                throw EcareException("La date de fin ne peut pas être avant la date de début.")
            }

        }

        if (debut.isEqual(aujourdhui) && heureRoutine.isBefore(heureActuelle)) {
            throw EcareException("L'heure choisie est déjà passée pour aujourd'hui.")
        }

        if (routine.repetition.isBlank()) {
            throw EcareException("La configuration de la répétition est incomplète.")
        }

        if (routine.heure.isBlank()) {
            throw EcareException("L'heure de début est obligatoire.")
        }


        return try {
            dao.upsertRoutine(routine)
        } catch (e: Exception) {
            throw EcareException("Erreur lors de l'enregistrement en base de données.")
        }
    }
    private fun parseDate(dateStr: String): LocalDate? {
        return try {
            LocalDate.parse(dateStr)
        } catch (e: DateTimeParseException) {
            null
        }
    }

}