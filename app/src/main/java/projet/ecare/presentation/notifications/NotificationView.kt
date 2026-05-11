package projet.ecare.presentation.notifications

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import projet.ecare.presentation.NotificationVM
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.filled.DoneAll
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val notifications by viewModel.notifications

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Notifications",
                                modifier = Modifier.align(Alignment.Center),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Retour",
                                modifier = Modifier.size(42.dp)
                            )
                        }
                    },
                    actions = {
                        if (notifications.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onEvent(NotificationEvent.OnToutEffacer) }) {
                                Icon(Icons.Default.DeleteSweep, contentDescription = "Tout effacer", tint = Color.Red)
                            }
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    ) { padding ->
        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(32.dp))

                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = null,
                        modifier = Modifier.size(120.dp),
                        tint = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Aucune notification",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Vous n'avez aucune notification pour le moment",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(350.dp))

                    HorizontalDivider(
                        modifier = Modifier.padding(16.dp),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Les nouvelles alertes apparaîtront ici",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(notifications) { notification ->
                    NotificationItem(
                        notification = notification,
                        onMarkRead = { viewModel.onEvent(NotificationEvent.OnMarquerCommeLu(notification.id)) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: NotificationVM,
    onMarkRead: () -> Unit
) {
    val color = when(notification.priorite) {
        "Élevée" -> Color.Red
        "Moyenne" -> Color(0xFF2E7D67)
        else -> Color.Gray
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(
                    color = if (notification.estLu) Color.Transparent else color,
                    shape = CircleShape
                )
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.titre,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = notification.message,
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            val relativeTime = DateUtils.getRelativeTimeSpanString(
                notification.timestamp,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            ).toString()

            Text(
                text = relativeTime,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        IconButton(onClick = onMarkRead) {
            if (notification.estLu) {
                Icon(Icons.Default.DoneAll, contentDescription = null, tint = Color.Gray)
            } else {
                Text("Lire", color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
