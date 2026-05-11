package projet.ecare.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import projet.ecare.presentation.utilitaires.Genre

@Entity(tableName = "profil")
data class ProfilEntity(
    @PrimaryKey
    val id: Int = 1,
    val nom: String,
    val prenom: String,
    val age: Int?,
    val genre: Genre
)
