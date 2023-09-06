package com.example.diaryapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.diaryapp.Entry;
import com.example.diaryapp.dao.EntryDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Entry.class}, version = 2, exportSchema = false)
@TypeConverters(Converter.class)
public abstract class EntryDatabase extends RoomDatabase {

    private static EntryDatabase INSTANCE;
    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized EntryDatabase getEntryDatabase(Context context){
        if (INSTANCE == null){
            synchronized (EntryDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EntryDatabase.class, "android-database").
                            allowMainThreadQueries().fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract EntryDao entryDao();
}
