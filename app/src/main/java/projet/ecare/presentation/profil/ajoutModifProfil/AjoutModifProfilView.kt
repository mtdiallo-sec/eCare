package projet.ecare.presentation.profil.ajoutModifProfif

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import projet.ecare.presentation.ajoutModification.SectionTitle
import projet.ecare.presentation.ajoutModification.StandardField
import projet.ecare.presentation.utilitaires.Genre
import projet.ecare.presentation.profil.ajoutModifProfil.AjoutModifProfilViewModel
import projet.ecare.presentation.profil.ajoutModifProfil.AjoutModifProfilEvent
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjoutModifProfilScreen(
    viewModel: AjoutModifProfilViewModel = hiltViewModel(),
    onBack: () -> Unit
) {

    val state = viewModel.state

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.loadProfil()
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Retour",
                                modifier = Modifier.size(42.dp)
                            )
                        }
                    },
                    title = {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(
                                text = if (viewModel.isEditMode) "Modifier votre profil" else "Créer un profil",
                                modifier = Modifier.align(Alignment.Center),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            viewModel.errorMessage?.let { msg ->
                Text(
                    text = msg,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold
                )
            }


            //NOM
            SectionTitle("Nom *")
            StandardField(
                value = state.nom,
                onValueChange = { viewModel.onEvent(AjoutModifProfilEvent.OnNomChange(it)) }
            )

            Spacer(Modifier.height(12.dp))

            //PRENOM
            SectionTitle("Prénom *")
            StandardField(
                value = state.prenom,
                onValueChange = { viewModel.onEvent(AjoutModifProfilEvent.OnPrenomChange(it)) }
            )

            Spacer(Modifier.height(12.dp))

            //AGE
            SectionTitle("Âge")
            StandardField(
                value = state.age,
                onValueChange = { viewModel.onEvent(AjoutModifProfilEvent.OnAgeChange(it)) }
            )


            Spacer(Modifier.height(12.dp))

            //GENRE
            SectionTitle("Genre *")
            Genre.values().forEach { genre ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    RadioButton(
                        selected = state.genre == genre,
                        onClick = {
                            viewModel.onEvent(AjoutModifProfilEvent.OnGenreChange(genre))
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF4CAF50))
                    )

                    Text(text = genre.label)
                }

            }

            Spacer(Modifier.height(12.dp))

            // BOUTON ENREGISTRER
            Button(
                onClick = {
                    viewModel.onEvent(AjoutModifProfilEvent.OnSave)
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("ENREGISTRER", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}