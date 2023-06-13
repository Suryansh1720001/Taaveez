package com.itssuryansh.taaveez.di

import android.content.Context
import androidx.room.Room
import com.itssuryansh.taaveez.data.database.NotesDao
import com.itssuryansh.taaveez.data.database.NotesDatabase
import com.itssuryansh.taaveez.data.repository.NotesRepositoryImpl
import com.itssuryansh.taaveez.domain.repository.NotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideNotesDao(database: NotesDatabase): NotesDao {
        return database.NotesDao()
    }

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Singleton
    @Provides
    fun provideNotesDatabase(@ApplicationContext context: Context): NotesDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            NotesDatabase::class.java,
            "notes_database"
        ).build()
    }
    @Singleton
    @Provides
    fun provideNotesRepository(dao: NotesDao): NotesRepository {
        return NotesRepositoryImpl(dao)
    }


}