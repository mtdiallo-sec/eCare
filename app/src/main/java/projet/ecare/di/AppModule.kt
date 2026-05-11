package projet.ecare.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.app.Application
import projet.ecare.data.source.EcareDatabase
import projet.ecare.domain.usecases.routine.*
import projet.ecare.domain.usecases.profil.*
import projet.ecare.domain.usecases.notification.*
import projet.ecare.presentation.notifications.AlarmScheduler

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideEcareDatabase(app: Application): EcareDatabase {
        return EcareDatabase.getDatabase(app)
    }

    @Provides
    @Singleton
    fun provideRoutinesUseCases(db: EcareDatabase): RoutinesUseCases {
        val dao = db.routineDao()
        return RoutinesUseCases(
            getRoutines = GetRoutinesUseCase(dao),
            getRoutine = GetRoutineUseCase(dao),
            upsertRoutine = UpsertRoutineUseCase(dao),
            deleteRoutine = DeleteRoutineUseCase(dao)
        )
    }

    @Provides
    @Singleton
    fun provideProfilUseCases(db: EcareDatabase): ProfilUseCases {
        val dao = db.profilDao()
        return ProfilUseCases(
            getProfil = GetProfilUseCase(dao),
            upsertProfil = UpsertProfilUseCase(dao),
            deleteProfil = DeleteProfilUseCase(dao)
        )
    }

    @Provides
    @Singleton
    fun provideNotificationUseCases(db: EcareDatabase): NotificationUseCases {
        val dao = db.notificationDao()
        return NotificationUseCases(
            getNotifications = GetNotificationsUseCase(dao),
            addNotification = AddNotificationUseCase(dao),
            deleteAllNotifications = DeleteAllNotificationsUseCase(dao),
            deleteByRoutine = DeleteByRoutineUseCase(dao),
            markAsRead = MarkAsReadUseCase(dao),
            getUnreadCount = GetUnreadCountUseCase(dao)
        )
    }

    @Provides
    @Singleton
    fun provideAlarmScheduler(app: Application): AlarmScheduler {
        return AlarmScheduler(app)
    }
}