package com.example.diaryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.diaryapp.database.EntryRepository;
import com.example.diaryapp.fragments.EntryDetailsFragment;

public class EntryDetailsActivity extends AppCompatActivity {

    EntryRepository entryRepo;
    Entry entry;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_details);

        entryRepo = new EntryRepository(getApplicationContext());

        id = getIntent().getIntExtra("id", 0);
        entry = entryRepo.getEntryById(id);

        EntryDetailsFragment detailsFragment = new EntryDetailsFragment();
        Bundle b = new Bundle();
        b.putInt("id", entry.getId());
        detailsFragment.setArguments(b);
        replaceFragment(detailsFragment);
    }

    private void replaceFragment(Fragment currFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.entry_frame_layout, currFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }
}