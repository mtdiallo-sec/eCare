package projet.ecare.presentation.accueil_historique

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationServices
import projet.ecare.presentation.composants.CarteRoutineAccueilHistorique
import projet.ecare.presentation.notifications.NotificationViewModel
import projet.ecare.presentation.utilitaires.TypeEcran
import com.google.android.gms.location.Priority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranRoutines(
    viewModel: ListeRoutineViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel,
    type: TypeEcran,
    onNavigate: (String) -> Unit
) {
    val isAccueil = type == TypeEcran.ACCUEIL
    val isLoading = viewModel.isLoading.value
    val routinesAccueil = viewModel.routinesAccueil.value
    val routinesHistorique = viewModel.routinesHistorique.value
    val unreadCount by notificationViewModel.unreadCount

    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    LaunchedEffect(Unit) {
        val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    viewModel.updatePosition(it.latitude, it.longitude)
                }
            }

            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    location?.let {
                        viewModel.updatePosition(it.latitude, it.longitude)
                    }
                }
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "eCare+",
                            color = Color(0xFF2E7D67),
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        BoiteNotification(
                            nombre = unreadCount,
                            onClick = { onNavigate("notifications") }
                        )
                    }
                )
                HorizontalDivider()
            }
        },
        floatingActionButton = {
            if (isAccueil) {
                FloatingActionButton(
                    onClick = { onNavigate("ajout") },
                    containerColor = Color(0xFF4CAF50),
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Ajouter",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        },
        bottomBar = {
            BottomBar(type, onNavigate)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (isAccueil) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    if (routinesAccueil.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = viewModel.messageBonjour.value,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = viewModel.messageBienvenue.value,
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Historique",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider()
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(60.dp),
                        color = Color(0xFF4CAF50),
                        strokeWidth = 5.dp
                    )
                }
            } else {
                val listIsEmpty = if (isAccueil) routinesAccueil.isEmpty() else routinesHistorique.isEmpty()

                if (listIsEmpty) {
                    EcranVide(isAccueil, viewModel.messageBonjour.value)
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (isAccueil) {
                            items(routinesAccueil) { uiState ->
                                CarteRoutineAccueilHistorique(
                                    routine = uiState.item,
                                    isActiveToday = uiState.isActive,
                                    isAccueil = true,
                                    onClick = { onNavigate("details/${uiState.item.id}") }
                                )
                            }
                        } else {
                            items(routinesHistorique) { routine ->
                                CarteRoutineAccueilHistorique(
                                    routine = routine,
                                    isActiveToday = false,
                                    isAccueil = false,
                                    onClick = { onNavigate("details/${routine.id}") }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EcranVide(isAccueil: Boolean, mesBonjour: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isAccueil) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = mesBonjour, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Pas de routine pour le moment", fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Profitez de l'occasion pour créer de nouvelles habitudes et prendre le contrôle de vos journées.",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = Color.Gray
                )
            }
        } else {
            Text(
                text = "Votre historique est prêt à se remplir !\n" +
                        "Chaque routine terminée sera ajoutée ici pour vous permettre de suivre vos progrès.\n" +
                        "Relevez vos défis et transformez cet espace en un reflet de vos accomplissements.",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}

@Composable
private fun BoiteNotification(
    nombre: Int,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Box {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier.size(28.dp)
            )
            if (nombre > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color.Red),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (nombre > 9) "9+" else nombre.toString(),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
