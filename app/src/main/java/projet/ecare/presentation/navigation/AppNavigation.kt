package projet.ecare.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import androidx.hilt.navigation.compose.hiltViewModel
import projet.ecare.presentation.accueil_historique.EcranRoutines
import projet.ecare.presentation.accueil_historique.ListeRoutineViewModel
import projet.ecare.presentation.detailsRoutine.DetailsRoutine
import projet.ecare.presentation.detailsRoutine.DetailsRoutineViewModel
import projet.ecare.presentation.ajoutModification.AjoutModificationViewModel
import projet.ecare.presentation.ajoutModification.AjoutModificationRoutine
import projet.ecare.presentation.utilitaires.TypeEcran
import projet.ecare.presentation.afficheprofil.AfficheProfilScreen
import projet.ecare.presentation.afficheprofil.AfficheProfilViewModel
import projet.ecare.presentation.profil.ajoutModifProfif.AjoutModifProfilScreen
import projet.ecare.presentation.profil.ajoutModifProfil.AjoutModifProfilViewModel
import projet.ecare.presentation.notifications.NotificationViewModel
import projet.ecare.presentation.notifications.NotificationScreen
import androidx.compose.ui.platform.LocalContext

@Composable
fun AppNavigation(startDestination: String? = null) {

    val navController = rememberNavController()
    val context = LocalContext.current

    val notificationVM: NotificationViewModel = hiltViewModel()

    val start = when (startDestination) {
        "notifications" -> "notifications"
        else -> "accueil"
    }

    NavHost(
        navController = navController,
        startDestination = start
    ) {

        composable("accueil") {
            val viewModel: ListeRoutineViewModel = hiltViewModel()
            
            EcranRoutines(
                viewModel = viewModel,
                notificationViewModel = notificationVM,
                type = TypeEcran.ACCUEIL,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable("historique") {
            val viewModel: ListeRoutineViewModel = hiltViewModel()

            EcranRoutines(
                viewModel = viewModel,
                notificationViewModel = notificationVM,
                type = TypeEcran.HISTORIQUE,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable("notifications") {
            NotificationScreen(
                viewModel = notificationVM,
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate("accueil") {
                            popUpTo("accueil") { inclusive = true }
                        }
                    }
                }
            )
        }

        composable("details/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
            val detailsViewModel: DetailsRoutineViewModel = hiltViewModel()

            DetailsRoutine(
                viewModel = detailsViewModel,
                routineId = id,
                onBack = { navController.popBackStack() },
                onEdit = { routine ->
                    navController.navigate("edit/${routine.id}")
                },
                onDelete = { routine ->
                    detailsViewModel.deleteRoutine(context, routine)
                    navController.popBackStack()
                }
            )
        }

        composable("ajout") {
            val vm: AjoutModificationViewModel = hiltViewModel()
            AjoutModificationRoutine(
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }

        composable("edit/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
            val vm: AjoutModificationViewModel = hiltViewModel()

            AjoutModificationRoutine(
                viewModel = vm,
                routineId = id,
                onBack = { navController.popBackStack() }
            )
        }

        composable("profil") {
            val viewModel: AfficheProfilViewModel = hiltViewModel()
            AfficheProfilScreen(
                viewModel = viewModel,
                onAddProfil = { navController.navigate("ajoutModifProfil") },
                onEditProfil = { navController.navigate("ajoutModifProfil") },
                onDeleteProfil = { viewModel.deleteProfil() },
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable("ajoutModifProfil") {
            val vm: AjoutModifProfilViewModel = hiltViewModel()
            AjoutModifProfilScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }
    }
}