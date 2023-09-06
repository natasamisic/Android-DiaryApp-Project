package com.example.diaryapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.diaryapp.AddEntryActivity;
import com.example.diaryapp.Entry;
import com.example.diaryapp.EntryAdapter;
import com.example.diaryapp.EntryDetailsActivity;
import com.example.diaryapp.MusicService;
import com.example.diaryapp.R;
import com.example.diaryapp.database.EntryRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntriesFragment extends Fragment implements AdapterView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button addEntryBtn;
    ListView entriesView;
    EntryRepository entryRepo;
    List<Entry> entries;
    EntryAdapter adapter;

    public EntriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EntriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EntriesFragment newInstance(String param1, String param2) {
        EntriesFragment fragment = new EntriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entries, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        entriesView = view.findViewById(R.id.listview);
        addEntryBtn = view.findViewById(R.id.addnewentry);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().stopService(new Intent(getActivity().getApplicationContext(), MusicService.class));
                getActivity().finish();
            }
        });

        getContent();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Entry entry = adapter.getEntry(position);
        Intent intent = new Intent(getActivity(), EntryDetailsActivity.class);
        intent.putExtra("id", entry.getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        getContent();
    }

    private void getContent(){
        entryRepo = new EntryRepository(getContext());
        entries = new ArrayList<>();
        entries = entryRepo.getEntries();
        adapter = new EntryAdapter(getContext(), R.layout.item_view, entries);
        adapter.notifyDataSetChanged();
        entriesView.setAdapter(adapter);
        entriesView.setOnItemClickListener(this);

        entriesView.setOnItemLongClickListener((parent, view, position, id) -> {
            PopupMenu menu = new PopupMenu(getContext(), view);
            menu.getMenu().add("DELETE");
            menu.setOnMenuItemClickListener(item -> {
                if(item.getTitle().equals("DELETE")){
                    Entry toDelete = entries.get(position);
                    entryRepo.deleteEntry(toDelete);
                    Toast.makeText(getContext(), "Entry deleted", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    new Handler().postDelayed(() -> replaceFragment(new EntriesFragment()), 2000);
                }
                return true;
            });
            menu.show();
            return true;
        });

        addEntryBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddEntryActivity.class)));
    }

    private void replaceFragment(Fragment currFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, currFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }
}