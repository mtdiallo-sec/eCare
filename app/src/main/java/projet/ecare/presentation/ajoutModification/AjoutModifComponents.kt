package projet.ecare.presentation.ajoutModification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.filled.Place

@Composable
fun SectionTitle(text: String) {
    Text(text = text, fontWeight = FontWeight.Bold, fontSize = 20.sp)
    Spacer(Modifier.height(4.dp))
}

@Composable
fun StandardField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    readOnly: Boolean = false,
    enabled: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        readOnly = readOnly,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        placeholder = { Text(placeholder) },
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF2F2F2),
            unfocusedContainerColor = Color(0xFFF2F2F2),
            disabledContainerColor = Color(0xFFF2F2F2),
            disabledTextColor = Color.Black,
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            disabledBorderColor = Color.Gray
        )
    )
}

@Composable
fun GenericPickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Annuler") }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        text = {
            Column(
                modifier = Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    )
}

@Composable
fun LocationPickerField(
    label: String,
    adresse: String?,
    suggestions: List<android.location.Address>,
    onSearchChange: (String) -> Unit,
    onSuggestionSelected: (android.location.Address) -> Unit,
    onClearLocation: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Column {
        SectionTitle(label)
        StandardField(
            value = adresse ?: "Choisir un lieu",
            onValueChange = {},
            readOnly = true,
            enabled = false,
            modifier = Modifier.clickable { showDialog = true },
            trailingIcon = {
                if (adresse != null) {
                    IconButton(onClick = onClearLocation) {
                        Icon(Icons.Default.Close, contentDescription = "Effacer")
                    }
                } else {
                    Icon(Icons.Default.Place, contentDescription = null)
                }
            }
        )
    }

    if (showDialog) {
        GenericPickerDialog(
            onDismiss = {
                showDialog = false
                onSearchChange("")
            },
            onConfirm = { showDialog = false }
        ) {
            LocationSearchContent(
                suggestions = suggestions,
                onSearchChange = onSearchChange,
                onSuggestionSelected = { addr ->
                    onSuggestionSelected(addr)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun LocationSearchContent(
    suggestions: List<android.location.Address>,
    onSearchChange: (String) -> Unit,
    onSuggestionSelected: (android.location.Address) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onSearchChange(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Tapez une adresse...") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (suggestions.isNotEmpty()) {
            Column {
                suggestions.forEach { address ->
                    val fullAddress = address.getAddressLine(0) ?: ""
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSuggestionSelected(address) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Place, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(12.dp))
                        Text(text = fullAddress, fontSize = 14.sp)
                    }
                    HorizontalDivider(thickness = 0.5.dp)
                }
            }
        }
    }
}