package projet.ecare.presentation.utilitaires

import projet.ecare.presentation.RoutineVM
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")

fun formatDateHeure(r: RoutineVM, inclureHeure: Boolean = true): String {
    val today = LocalDate.now(ZoneId.systemDefault())
    val heurePrefix = if (inclureHeure) "${r.heure} • " else ""

    val dateDebut = r.dateDebut
    val dateFin = r.dateFin

    return when {
        //Cas 1 : Période complète
        dateFin != null ->
            "$heurePrefix${dateDebut.format(outputFormatter)} - ${dateFin.format(outputFormatter)}"

        //Cas 2 : Routine répétitive
        r.periodicite != Periodicite.PONCTUELLE -> {
            val prefix = when {
                dateDebut.isBefore(today) -> "Depuis le "
                dateDebut.isEqual(today) -> "Dès "
                else -> "À partir du "
            }
            "$heurePrefix$prefix${dateDebut.format(outputFormatter)}"
        }

        // Cas 3 : Ponctuelle
        r.periodicite == Periodicite.PONCTUELLE -> {
            "$heurePrefix${if (inclureHeure) "Le " else ""}${dateDebut.format(outputFormatter)}"
        }

        else -> r.heure
    }
}