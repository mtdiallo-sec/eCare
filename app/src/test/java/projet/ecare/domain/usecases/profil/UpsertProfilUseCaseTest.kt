package projet.ecare.domain.usecases.profil

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import projet.ecare.FakeDatabase
import projet.ecare.domain.exception.EcareException
import projet.ecare.domain.model.ProfilEntity
import projet.ecare.presentation.utilitaires.Genre

class UpsertProfilUseCaseTest {

    private lateinit var upsertProfilUseCase: UpsertProfilUseCase
    private val database = FakeDatabase()

    @Before
    fun setUp() {
        upsertProfilUseCase = UpsertProfilUseCase(database)
    }

    @Test
    fun `Le profil devrait etre ajoute si les champs sont valides`() {
        runBlocking {
            val profil = ProfilEntity(
                id = 1,
                nom = "Diallo",
                prenom = "Alpha",
                age = 22,
                genre = Genre.HOMME
            )

            upsertProfilUseCase(profil)

            val resultat: ProfilEntity? = database.getProfil().first()

            assertNotNull("Le profil ne devrait pas être nul apres l'insertion", resultat)
            assertEquals("Diallo", resultat?.nom)
            assertEquals("Alpha", resultat?.prenom)
            assertEquals(22, resultat?.age)
            assertEquals(Genre.HOMME, resultat?.genre)
        }
    }

    @Test(expected = EcareException::class)
    fun `Le profil ne devrait pas etre ajoute si le nom est vide`() {
        runBlocking {
            val profil = ProfilEntity(
                id = 1,
                nom = "",
                prenom = "Marie",
                age = 22,
                genre = Genre.FEMME
            )

            upsertProfilUseCase(profil)
        }
    }

    @Test(expected = EcareException::class)
    fun `Le profil ne devrait pas etre ajoute si le prenom est vide`() {
        runBlocking {
            val profil = ProfilEntity(
                id = 1,
                nom = "Valentin",
                prenom = "",
                age = null,
                genre = Genre.NON_BINAIRE
            )

            upsertProfilUseCase(profil)
        }
    }

    @Test(expected = EcareException::class)
    fun `Le profil ne devrait pas etre ajoute si lage est negatif`() {
        runBlocking {
            val profil = ProfilEntity(
                id = 1,
                nom = "Diallo",
                prenom = "Alpha",
                age = -1,
                genre = Genre.NON_REPONSE
            )

            upsertProfilUseCase(profil)
        }
    }
}
