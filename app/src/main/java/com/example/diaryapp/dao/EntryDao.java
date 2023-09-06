package com.example.diaryapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.diaryapp.Entry;

import java.util.List;

@Dao
public interface EntryDao {

    @Query("SELECT * FROM entry ORDER BY createdTime DESC")
    List<Entry> getAllEntries();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEntry(Entry entry);

    @Query("SELECT * FROM entry WHERE id like :id")
    Entry getEntryById(int id);

    @Update
    void updateEntry(Entry entry);

    @Delete
    void deleteEntry(Entry entry);
}
