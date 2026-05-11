package projet.ecare.presentation.accueil_historique

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.sp

import projet.ecare.presentation.utilitaires.TypeEcran

@Composable
fun BottomBar(
    selected: TypeEcran,
    onNavigate: (String) -> Unit
) {
    Column {
        Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            NavigationBarItem(
                selected = selected == TypeEcran.ACCUEIL,
                onClick = { onNavigate("accueil") },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Accueil",
                        modifier = Modifier.size(28.dp)
                    )
                },
                label = { Text("Accueil", fontSize = 16.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF2E7D67),
                    selectedTextColor = Color(0xFF2E7D67),
                    indicatorColor = Color(0xFFE8F5E9)
                )
            )

            NavigationBarItem(
                selected = selected == TypeEcran.HISTORIQUE,
                onClick = { onNavigate("historique") },
                icon = {
                    Icon(
                        Icons.Filled.History,
                        contentDescription = "Historique",
                        modifier = Modifier.size(28.dp)
                    )
                },
                label = { Text("Historique", fontSize = 16.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF2E7D67),
                    selectedTextColor = Color(0xFF2E7D67),
                    indicatorColor = Color(0xFFE8F5E9)
                )
            )

            NavigationBarItem(
                selected = selected == TypeEcran.PROFIL,
                onClick = { onNavigate("profil") },
                icon = {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "Profil",
                        modifier = Modifier.size(28.dp)
                    )
                },
                label = { Text("Profil", fontSize = 16.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF2E7D67),
                    selectedTextColor = Color(0xFF2E7D67),
                    indicatorColor = Color(0xFFE8F5E9)
                )
            )
        }
    }
}


