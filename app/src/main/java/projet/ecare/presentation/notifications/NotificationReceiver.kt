package projet.ecare.presentation.notifications

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import projet.ecare.data.source.EcareDatabase
import projet.ecare.domain.model.NotificationEntity
import projet.ecare.domain.model.RoutineEntity
import projet.ecare.presentation.utilitaires.calculerDistancePosition

class NotificationReceiver : BroadcastReceiver() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent) {
        val routineId = intent.getIntExtra("routine_id", -1)
        if (routineId == -1) return

        val pendingResult = goAsync()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val db = EcareDatabase.getDatabase(context)
                val routine = db.routineDao().getRoutineById(routineId)

                if (routine != null) {
                    processRoutine(context, db, routine, routineId)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                pendingResult.finish()
            }
        }
    }

    private suspend fun processRoutine(
        context: Context,
        db: EcareDatabase,
        routine: RoutineEntity,
        routineId: Int
    ) {
        val client = LocationServices.getFusedLocationProviderClient(context)

        val shouldNotify = try {
            if (routine.latitude == null || routine.longitude == null) {
                true
            } else {
                val hasPermission =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

                if (!hasPermission) {
                    true
                } else {
                    val location = try {
                        client.lastLocation.await() ?: client.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).await()
                    } catch (e: Exception) {
                        null
                    }

                    if (location == null) {
                        true
                    } else {
                        val distance = calculerDistancePosition(
                            location.latitude,
                            location.longitude,
                            routine.latitude,
                            routine.longitude
                        )
                        distance <= routine.rayonMetres
                    }
                }
            }
        } catch (e: Exception) {
            true
        }

        if (shouldNotify) {
            envoyerNotification(context, db, routine, routineId)
        }
    }

    private suspend fun envoyerNotification(
        context: Context,
        db: EcareDatabase,
        routine: RoutineEntity,
        routineId: Int
    ) {
        withContext(Dispatchers.IO) {
            val notification = NotificationEntity(
                routineId = routineId,
                titre = routine.nom,
                message = "C'est l'heure de votre routine : ${routine.nom}",
                timestamp = System.currentTimeMillis(),
                estLu = false,
                priorite = routine.priorite.label
            )
            db.notificationDao().insertNotification(notification)
        }

        NotificationHelper(context).showNotification(
            routine.nom,
            "Il est l'heure de votre routine !",
            routine.priorite.label
        )
    }
}