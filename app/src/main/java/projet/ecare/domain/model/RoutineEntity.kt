package projet.ecare.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import projet.ecare.presentation.utilitaires.Categorie
import projet.ecare.presentation.utilitaires.Periodicite
import projet.ecare.presentation.utilitaires.Priorite

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nom: String,
    val description: String?,
    val categorie: Categorie,
    val heure: String,
    val dateDebut: String,
    val dateFin: String?,
    val periodicite: Periodicite,
    val repetition: String,
    val priorite: Priorite,

    val latitude: Double? = null,
    val longitude: Double? = null,
    val adresse: String? = null,
    val rayonMetres: Int = 100
)