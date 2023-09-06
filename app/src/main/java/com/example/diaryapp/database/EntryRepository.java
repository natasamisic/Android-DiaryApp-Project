package com.example.diaryapp.database;

import android.content.Context;

import com.example.diaryapp.Entry;
import com.example.diaryapp.dao.EntryDao;

import java.util.List;

public class EntryRepository {

    private EntryDao entryDao;
    List<Entry> entries;

    public EntryRepository(Context context){
        EntryDatabase db = EntryDatabase.getEntryDatabase(context);
        entryDao = db.entryDao();
        entries = entryDao.getAllEntries();
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void insertEntry(Entry entry){
        EntryDatabase.databaseWriteExecutor.execute(() -> entryDao.insertEntry(entry));
    }

    public Entry getEntryById(int id){ return entryDao.getEntryById(id);}

    public void deleteEntry(Entry entry){
        EntryDatabase.databaseWriteExecutor.execute(() -> entryDao.deleteEntry(entry));
    }

    public void updateEntry(Entry entry){
        EntryDatabase.databaseWriteExecutor.execute(() -> entryDao.updateEntry(entry));
    }
}
