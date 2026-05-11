package projet.ecare.presentation

import projet.ecare.domain.model.RoutineEntity
import projet.ecare.presentation.utilitaires.Categorie
import projet.ecare.presentation.utilitaires.Periodicite
import projet.ecare.presentation.utilitaires.Priorite
import java.time.LocalDate
import java.time.ZoneId

data class RoutineVM(
    val id: Int = 0,
    val nom: String = "",
    val description: String = "",
    val categorie: Categorie = Categorie.CHOISIR_CATEGORIE,
    val periodicite: Periodicite = Periodicite.QUOTIDIENNE,
    val priorite: Priorite = Priorite.MOYENNE,
    val repetition: String = "",
    val dateDebut: LocalDate = LocalDate.now(ZoneId.systemDefault()),
    val dateFin: LocalDate? = null,
    val heure: String = "00:00",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val adresse: String? = null,
    val rayonMetres: Int = 100

) {
    companion object {
        fun fromEntity(entity: RoutineEntity): RoutineVM {
            return RoutineVM(
                id = entity.id,
                nom = entity.nom,
                description = if (entity.description.isNullOrBlank()) "Aucune description" else entity.description,
                categorie = entity.categorie,
                periodicite = entity.periodicite,
                priorite = entity.priorite,
                repetition = entity.repetition,
                dateDebut = try {
                    LocalDate.parse(entity.dateDebut)
                } catch (e: Exception) {
                    LocalDate.now(ZoneId.systemDefault())
                },
                dateFin = entity.dateFin?.let { LocalDate.parse(it) },
                heure = entity.heure,
                latitude = entity.latitude,
                longitude = entity.longitude,
                adresse = entity.adresse,
                rayonMetres = entity.rayonMetres
            )
        }
    }
}

fun RoutineVM.toEntity(): RoutineEntity {
    return RoutineEntity(
        id = this.id,
        nom = this.nom,
        description = this.description,
        categorie = this.categorie,
        periodicite = this.periodicite,
        repetition = if (this.periodicite == Periodicite.HEBDOMADAIRE) this.repetition else this.periodicite.label,
        dateDebut = this.dateDebut.toString(),
        dateFin = this.dateFin?.toString(),
        heure = this.heure,
        priorite = this.priorite,
        latitude = this.latitude,
        longitude = this.longitude,
        adresse = this.adresse,
        rayonMetres = this.rayonMetres
    )
}