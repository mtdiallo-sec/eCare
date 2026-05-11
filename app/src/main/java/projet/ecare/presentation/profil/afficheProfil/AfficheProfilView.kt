package projet.ecare.presentation.afficheprofil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import projet.ecare.presentation.utilitaires.Genre
import projet.ecare.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import projet.ecare.presentation.accueil_historique.BottomBar
import projet.ecare.presentation.utilitaires.TypeEcran

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AfficheProfilScreen(
    viewModel: AfficheProfilViewModel = hiltViewModel(),
    onAddProfil: () -> Unit,
    onEditProfil: () -> Unit,
    onDeleteProfil: () -> Unit,
    onNavigate: (String) -> Unit
) {

    val profil = viewModel.profil.value

    Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Profil Utilisateur",
                                    modifier = Modifier.align(Alignment.Center),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    )
                    HorizontalDivider()
                }
        },

        bottomBar = {
            BottomBar(
                selected = TypeEcran.PROFIL,
                onNavigate = onNavigate
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            val avatarRes = when (profil?.genre) {
                Genre.HOMME -> R.drawable.avatar_homme
                Genre.FEMME -> R.drawable.avatar_femme
                else -> R.drawable.avatar_inconnu
            }

            Image(
                painter = painterResource(id = avatarRes),
                contentDescription = "Avatar",
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            //CAS PROFIL EXISTE
            if (profil != null) {


                Text(
                    text = viewModel.nomComplet.value,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Âge : " + viewModel.ageAffiche.value,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Genre : " + viewModel.genreAffiche.value,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                HorizontalDivider(modifier = Modifier.fillMaxWidth()
                    .padding(20.dp))

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onEditProfil() },
                    modifier = Modifier.fillMaxWidth()
                        .padding(20.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text(
                        text = "Modifier le profil",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedButton(
                    onClick = { onDeleteProfil() },
                    modifier = Modifier.fillMaxWidth()
                        .padding(20.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Red),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Text(
                        text = "Supprimer le profil",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            //CAS PAS DE PROFIL
            else {

                Text(
                    text = "Aucun profil créé",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Créer votre profil pour personnaliser votre application",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { onAddProfil() },
                    modifier = Modifier.fillMaxWidth()
                        .padding(20.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text(
                        text = "Créer un profil",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}