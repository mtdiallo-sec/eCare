package projet.ecare.data.source

import androidx.room.TypeConverter
import projet.ecare.presentation.utilitaires.Categorie
import projet.ecare.presentation.utilitaires.Periodicite
import projet.ecare.presentation.utilitaires.Priorite
import projet.ecare.presentation.utilitaires.Genre

class Converters {
    @TypeConverter
    fun fromCategorie(value: Categorie): String = value.name

    @TypeConverter
    fun toCategorie(value: String): Categorie = try {
        Categorie.valueOf(value)
    } catch (e: Exception) {
        Categorie.CHOISIR_CATEGORIE
    }

    @TypeConverter
    fun fromPeriodicite(value: Periodicite): String = value.name

    @TypeConverter
    fun toPeriodicite(value: String): Periodicite = try {
        Periodicite.valueOf(value)
    } catch (e: Exception) {
        Periodicite.QUOTIDIENNE
    }

    @TypeConverter
    fun fromPriorite(value: Priorite): String = value.name

    @TypeConverter
    fun toPriorite(value: String): Priorite = try {
        Priorite.valueOf(value)
    } catch (e: Exception) {
        Priorite.MOYENNE
    }

    @TypeConverter
    fun fromGenre(value: Genre): String = value.name

    @TypeConverter
    fun toGenre(value: String): Genre = try {
        Genre.valueOf(value)
    } catch (e: Exception) {
        Genre.NON_REPONSE
    }
}