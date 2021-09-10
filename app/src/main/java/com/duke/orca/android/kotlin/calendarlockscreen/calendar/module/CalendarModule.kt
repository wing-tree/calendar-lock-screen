package com.duke.orca.android.kotlin.calendarlockscreen.calendar.module

import android.content.Context
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.repository.CalendarRepository
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.repository.CalendarRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CalendarModule {
    @Singleton
    @Provides
    fun provideCalendarRepository(@ApplicationContext applicationContext: Context): CalendarRepository {
        return CalendarRepositoryImpl(applicationContext)
    }
}