package projet.ecare.domain.usecases.routine

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import projet.ecare.FakeDatabase
import projet.ecare.domain.exception.EcareException
import projet.ecare.domain.model.RoutineEntity
import projet.ecare.presentation.utilitaires.Categorie
import projet.ecare.presentation.utilitaires.Periodicite
import projet.ecare.presentation.utilitaires.Priorite
import java.time.LocalDate
import java.time.LocalTime

class UpsertRoutineUseCaseTest {

    lateinit var upsertRoutineUseCase: UpsertRoutineUseCase
    val database = FakeDatabase()

    @Before
    fun setUp() {
        upsertRoutineUseCase = UpsertRoutineUseCase(database)
    }

    @Test
    fun `La routine devrait etre ajoutee si les champs sont valides`() {
        runBlocking {
            val routine = RoutineEntity(
                id = 0,
                nom = "Méditation",
                description = "Méditer est bon pour l'esprit",
                categorie = Categorie.SANTE,
                periodicite = Periodicite.QUOTIDIENNE,
                priorite = Priorite.MOYENNE,
                repetition = "Quotidienne",
                dateDebut = LocalDate.now().toString(),
                dateFin = null,
                heure = "23:59",
                latitude = null,
                longitude = null,
                adresse = null,
                rayonMetres = 100
            )

            upsertRoutineUseCase(routine)

            val routines = database.getAllRoutines().first()
            assertEquals(1, routines.size)
        }
    }

    @Test(expected = EcareException::class)
    fun `La routine ne devrait pas etre ajoutee si le nom est vide`() {
        runBlocking {
            val routine = RoutineEntity(
                id = 0,
                nom = "",
                description = "",
                categorie = Categorie.NUTRITION,
                periodicite = Periodicite.QUOTIDIENNE,
                priorite = Priorite.MOYENNE,
                repetition = "Quotidienne",
                dateDebut = LocalDate.now().toString(),
                dateFin = null,
                heure = "10:00",
                latitude = null,
                longitude = null,
                adresse = null,
                rayonMetres = 100
            )

            upsertRoutineUseCase(routine)
        }
    }

    @Test(expected = EcareException::class)
    fun `La routine ne devrait pas etre ajoutee si la categorie est non selectionnee`() {
        runBlocking {
            val routine = RoutineEntity(
                id = 0,
                nom = "Course",
                description = "Jogging",
                categorie = Categorie.CHOISIR_CATEGORIE,
                periodicite = Periodicite.QUOTIDIENNE,
                priorite = Priorite.MOYENNE,
                repetition = "Quotidienne",
                dateDebut = LocalDate.now().toString(),
                dateFin = null,
                heure = "09:00",
                latitude = null,
                longitude = null,
                adresse = null,
                rayonMetres = 100
            )

            upsertRoutineUseCase(routine)
        }
    }

    @Test(expected = EcareException::class)
    fun `La routine ne devrait pas etre ajoutee si repetition est vide pour hebdomadaire`() {
        runBlocking {
            val routine = RoutineEntity(
                id = 0,
                nom = "Gym",
                description = "Entrainement",
                categorie = Categorie.SPORT,
                periodicite = Periodicite.HEBDOMADAIRE,
                priorite = Priorite.MOYENNE,
                repetition = "",
                dateDebut = LocalDate.now().toString(),
                dateFin = null,
                heure = "18:30",
                latitude = null,
                longitude = null,
                adresse = null,
                rayonMetres = 100
            )

            upsertRoutineUseCase(routine)
        }
    }

    @Test(expected = EcareException::class)
    fun `La routine ne devrait pas etre ajoutee si la date est avant aujourdhui`() {
        runBlocking {
            val routine = RoutineEntity(
                id = 0,
                nom = "Yoga",
                description = "Routine matin",
                categorie = Categorie.SANTE,
                periodicite = Periodicite.PONCTUELLE,
                priorite = Priorite.FAIBLE,
                repetition = "Ponctuelle",
                dateDebut = LocalDate.now().minusDays(1).toString(),
                dateFin = null,
                heure = "07:00",
                latitude = null,
                longitude = null,
                adresse = null,
                rayonMetres = 100
            )

            upsertRoutineUseCase(routine)
        }
    }

    @Test(expected = EcareException::class)
    fun `La routine ne devrait pas etre ajoutee si heure est vide`() {
        runBlocking {
            val routine = RoutineEntity(
                id = 0,
                nom = "Natation",
                description = "Piscine",
                categorie = Categorie.SPORT,
                periodicite = Periodicite.QUOTIDIENNE,
                priorite = Priorite.ELEVEE,
                repetition = "Quotidienne",
                dateDebut = LocalDate.now().toString(),
                dateFin = null,
                heure = "",
                latitude = null,
                longitude = null,
                adresse = null,
                rayonMetres = 100
            )

            upsertRoutineUseCase(routine)
        }
    }

    @Test(expected = EcareException::class)
    fun `La routine ne devrait pas etre ajoutee si heure est invalide`() {
        runBlocking {
            val routine = RoutineEntity(
                id = 0,
                nom = "Natation",
                description = "Piscine",
                categorie = Categorie.SPORT,
                periodicite = Periodicite.PONCTUELLE,
                priorite = Priorite.ELEVEE,
                repetition = "Ponctuelle",
                dateDebut = LocalDate.now().toString(),
                dateFin = null,
                heure = "25:99",
                latitude = null,
                longitude = null,
                adresse = null,
                rayonMetres = 100
            )

            upsertRoutineUseCase(routine)
        }
    }

    @Test(expected = EcareException::class)
    fun `La routine ne devrait pas etre ajoutee si heure est deja passee aujourdhui`() {
        runBlocking {
            val heurePassee = LocalTime.now()
                .minusHours(1)
                .withSecond(0)
                .withNano(0)
                .toString()

            val routine = RoutineEntity(
                id = 0,
                nom = "Marche",
                description = "Routine quotidienne",
                categorie = Categorie.SANTE,
                periodicite = Periodicite.QUOTIDIENNE,
                priorite = Priorite.MOYENNE,
                repetition = "Quotidienne",
                dateDebut = LocalDate.now().toString(),
                dateFin = null,
                heure = heurePassee,
                latitude = null,
                longitude = null,
                adresse = null,
                rayonMetres = 100
            )

            upsertRoutineUseCase(routine)
        }
    }

    @Test
    fun `La routine devrait etre ajoutee si heure est valide et future`() {
        runBlocking {
            val heureFuture = LocalTime.now()
                .plusHours(1)
                .withSecond(0)
                .withNano(0)
                .toString()

            val routine = RoutineEntity(
                id = 0,
                nom = "Lecture",
                description = "Lire 30 minutes",
                categorie = Categorie.LOISIR,
                periodicite = Periodicite.QUOTIDIENNE,
                priorite = Priorite.MOYENNE,
                repetition = "Quotidienne",
                dateDebut = LocalDate.now().toString(),
                dateFin = null,
                heure = heureFuture,
                latitude = null,
                longitude = null,
                adresse = null,
                rayonMetres = 100
            )

            upsertRoutineUseCase(routine)

            val routines = database.getAllRoutines().first()
            assertEquals(1, routines.size)
        }
    }
}