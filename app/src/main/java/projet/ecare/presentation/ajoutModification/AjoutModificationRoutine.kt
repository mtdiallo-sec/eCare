package projet.ecare.presentation.ajoutModification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import projet.ecare.presentation.utilitaires.Categorie
import projet.ecare.presentation.utilitaires.Periodicite
import java.time.ZoneId
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjoutModificationRoutine(
    viewModel: AjoutModificationViewModel = hiltViewModel(),
    routineId: Int? = null,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val state = viewModel.state


    LaunchedEffect(routineId) {
        if (routineId != null) {
            viewModel.loadRoutine(routineId)
        } else {
            viewModel.resetState()
        }
    }

    // États des Pickers
    val dateDebutPickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.dateDebut
            .atStartOfDay(ZoneId.of("UTC"))
            .toInstant()
            .toEpochMilli()
    )

    val dateFinPickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.dateFin?.atStartOfDay(ZoneId.of("UTC"))
            ?.toInstant()
            ?.toEpochMilli()
    )

    val timePickerState = rememberTimePickerState()

    // États d'affichage
    var showDateDebutPicker by remember { mutableStateOf(false) }
    var showDateFinPicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

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
                                text = if (routineId == null) "Ajouter une routine" else "Modifier la routine",
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

            // SECTION NOM & DESCRIPTION
            SectionTitle("Nom de la routine *")
            StandardField(
                value = state.nom,
                onValueChange = { viewModel.onEvent(AjoutModificationEvent.OnNomChange(it)) }
            )

            Spacer(Modifier.height(12.dp))

            SectionTitle("Description")
            StandardField(
                value = state.description,
                onValueChange = { viewModel.onEvent(AjoutModificationEvent.OnDescriptionChange(it)) },
                placeholder = "Entrez une description"
            )

            Spacer(Modifier.height(12.dp))

            // SECTION CATÉGORIE
            val categoriesLabels = Categorie.values().map { it.label }

            SectionTitle("Catégorie *")
            ExposedDropdownField(
                valeurdefaut = "",
                selectedOption = state.categorie?.label ?: "Sélectionner une catégorie",
                options = categoriesLabels,
                onOptionSelected = { labelSelectionne ->
                    val enumTrouve = Categorie.values().find { it.label == labelSelectionne } ?: Categorie.AUTRE
                    viewModel.onEvent(AjoutModificationEvent.OnCategorieChange(enumTrouve))
                }
            )

            Spacer(Modifier.height(12.dp))

            // SECTION PÉRIODICITÉ
            SectionTitle("Périodicité *")
            Periodicite.values().forEach { per ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    RadioButton(
                        selected = state.periodicite == per,
                        onClick = {
                            viewModel.onEvent(AjoutModificationEvent.OnPeriodiciteChange(per))
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF4CAF50))
                    )

                    Text(text = per.label)

                    if (per == Periodicite.HEBDOMADAIRE && state.periodicite == Periodicite.HEBDOMADAIRE) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp)
                        ) {
                            ExposedDropdownField(
                                valeurdefaut = "Choisir un jour *",
                                selectedOption = if (Periodicite.joursSemaine.contains(state.repetition)) {
                                    state.repetition
                                } else {
                                    ""
                                },
                                options = Periodicite.joursSemaine,
                                onOptionSelected = { jourChoisi ->
                                    viewModel.onEvent(AjoutModificationEvent.OnRepetitionChange(jourChoisi))
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // SECTION DATES
            Row(Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f)) {
                    DatePickerField("Date de début *", state.dateDebut) {
                        showDateDebutPicker = true
                    }
                }

                if (state.periodicite != Periodicite.PONCTUELLE) {
                    Spacer(Modifier.width(12.dp))
                    Box(Modifier.weight(1f)) {
                        DatePickerField("Date de fin", state.dateFin) {
                            showDateFinPicker = true
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // SECTION HEURE
            SectionTitle("Heure *")
            StandardField(
                value = state.heure.ifEmpty { "00:00" },
                onValueChange = {},
                readOnly = true,
                enabled = false,
                modifier = Modifier.clickable { showTimePicker = true },
                trailingIcon = { Icon(Icons.Default.AccessTime, null) }
            )

            Spacer(Modifier.height(12.dp))

            // SECTION LIEU
            LocationPickerField(
                label = "Définir un Lieu",
                adresse = state.adresse,
                suggestions = viewModel.suggestions,
                onSearchChange = { query ->
                    viewModel.searchAddress(query, context)
                },
                onSuggestionSelected = { selection ->
                    val adresseTexte = selection.getAddressLine(0) ?: ""
                    viewModel.onEvent(AjoutModificationEvent.OnLieuSelectionne(
                        lat = selection.latitude,
                        lon = selection.longitude,
                        adresse = adresseTexte
                    ))
                    viewModel.searchAddress("", context)
                },
                onClearLocation = {
                    viewModel.onEvent(AjoutModificationEvent.OnEffacerLieu)
                }
            )

            Spacer(Modifier.height(12.dp))

            // SECTION PRIORITÉ
            PrioritySelector(
                selectedPriority = state.priorite,
                onPrioritySelected = {
                    viewModel.onEvent(AjoutModificationEvent.OnPrioriteChange(it))
                }
            )

            Spacer(Modifier.height(32.dp))

            // BOUTON ENREGISTRER
            Button(
                onClick = {
                    viewModel.onSave(context) {
                        onBack()
                    }
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

    // DIALOGUES
    if (showDateDebutPicker) {
        GenericPickerDialog(
            onDismiss = { showDateDebutPicker = false },
            onConfirm = {
                viewModel.onEvent(AjoutModificationEvent.OnDateDebutChange(dateDebutPickerState.selectedDateMillis))
                showDateDebutPicker = false
            }
        ) {
            DatePicker(state = dateDebutPickerState)
        }
    }

    if (showDateFinPicker) {
        GenericPickerDialog(
            onDismiss = { showDateFinPicker = false },
            onConfirm = {
                viewModel.onEvent(AjoutModificationEvent.OnDateFinChange(dateFinPickerState.selectedDateMillis))
                showDateFinPicker = false
            }
        ) {
            DatePicker(state = dateFinPickerState)
        }
    }

    if (showTimePicker) {
        GenericPickerDialog(
            onDismiss = { showTimePicker = false },
            onConfirm = {
                val formatted = "%02d:%02d".format(timePickerState.hour, timePickerState.minute)
                viewModel.onEvent(AjoutModificationEvent.OnHeureChange(formatted))
                showTimePicker = false
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}
