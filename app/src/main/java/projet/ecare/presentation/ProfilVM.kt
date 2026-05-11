package projet.ecare.presentation

import projet.ecare.domain.model.ProfilEntity
import projet.ecare.presentation.utilitaires.Genre

data class ProfilVM(
    val id: Int = 1,
    val nom: String = "",
    val prenom: String = "",
    val age: String = "",
    val genre: Genre = Genre.NON_REPONSE
){
    companion object {
        fun fromEntity(entity: ProfilEntity): ProfilVM {
            return ProfilVM(
                id = entity.id,
                nom = entity.nom,
                prenom = entity.prenom,
                age = entity.age?.toString() ?: "",
                genre = entity.genre
            )
        }
    }
}


fun ProfilVM.toEntity(): ProfilEntity {
    return ProfilEntity(
        id = this.id,
        nom = this.nom,
        prenom = this.prenom,
        age = this.age.toIntOrNull(),
        genre = this.genre
    )
}