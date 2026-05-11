package projet.ecare.presentation.ajoutModification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.unit.sp
import projet.ecare.presentation.utilitaires.Priorite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownField(
    valeurdefaut: String,
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            StandardField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                placeholder = valeurdefaut,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )

                    if (index < options.lastIndex) {
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun PrioritySelector(
    selectedPriority: Priorite,
    onPrioritySelected: (Priorite) -> Unit
) {
    SectionTitle("Priorité *")
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Priorite.values().forEach { priority ->
            val color = when (priority) {
                Priorite.ELEVEE -> Color.Red
                Priorite.MOYENNE -> Color(0xFFFFC107)
                Priorite.FAIBLE -> Color.Green
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedPriority == priority,
                    onClick = { onPrioritySelected(priority) },
                    colors = RadioButtonDefaults.colors(selectedColor = color)
                )
                Text(text = priority.name, color = color, fontSize = 15.sp)
            }
        }
    }
}

@Composable
fun DatePickerField(
    label: String,
    date: LocalDate?,
    onClick: () -> Unit
) {
    Column {
        SectionTitle(label)
        StandardField(
            value = date?.toString() ?: "Choisir une date",
            onValueChange = {},
            readOnly = true,
            enabled = false,
            modifier = Modifier.clickable { onClick() },
            trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) }
        )
    }
}