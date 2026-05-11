package projet.ecare.presentation.utilitaires

import androidx.compose.ui.graphics.Color

enum class TypeEcran {
    ACCUEIL,
    HISTORIQUE,
    PROFIL
}

enum class Categorie(val label: String, val color: Color) {
    SANTE("Santé", Color(0xFFE91E63)),
    SPORT("Sport", Color(0xFFFF9800)),
    TRAVAIL("Travail", Color(0xFF2196F3)),
    SOMMEIL("Sommeil", Color(0xFF673AB7)),
    NUTRITION("Nutrition", Color(0xFF4CAF50)),
    PRODUCTIVITE("Productivité", Color(0xFF00BCD4)),
    LOISIR("Loisir", Color(0xFFFFEB3B)),
    AUTRE("Autre", Color(0xFF9E9E9E)),
    CHOISIR_CATEGORIE("Aucune Selection", Color(0x000))
}

enum class Periodicite(val label: String) {
    QUOTIDIENNE("Quotidienne"),
    PONCTUELLE("Ponctuelle"),
    HEBDOMADAIRE("Hebdomadaire");

    companion object {
        val joursSemaine = listOf("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche")
    }
    fun getAffichageDetail(repetitionValue: String): String {
        return when (this) {
            QUOTIDIENNE -> "Chaque jour"
            PONCTUELLE -> "Une fois"
            HEBDOMADAIRE -> "Chaque $repetitionValue"
        }
    }
}

enum class Priorite(val label: String, val color: Color) {
    FAIBLE("Faible", Color(0xFF4CAF50)),
    MOYENNE("Moyenne", Color(0xFFFFC107)),
    ELEVEE("Élevée", Color(0xFFF44336))
}

enum class Genre(val label: String) {
    HOMME("Homme"),
    FEMME("Femme"),
    NON_BINAIRE("Non binaire"),
    NON_REPONSE("Je préfère ne pas répondre")
}
