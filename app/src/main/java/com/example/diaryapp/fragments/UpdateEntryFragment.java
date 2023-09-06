package com.example.diaryapp.fragments;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diaryapp.Entry;
import com.example.diaryapp.R;
import com.example.diaryapp.database.EntryRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateEntryFragment extends Fragment {

    EditText title;
    EditText text;
    Button updatebtn;
    EntryRepository entryRepo;
    Entry entry;
    int id;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateEntryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateEntryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateEntryFragment newInstance(String param1, String param2) {
        UpdateEntryFragment fragment = new UpdateEntryFragment();
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
        return inflater.inflate(R.layout.fragment_update_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.update_title);
        text = view.findViewById(R.id.update_text);
        updatebtn = view.findViewById(R.id.update_btn);

        entryRepo = new EntryRepository(getContext());
        id = getArguments().getInt("id");
        entry = entryRepo.getEntryById(id);

        title.setText(entry.getTitle());
        text.setText(entry.getText());

        updatebtn.setOnClickListener(v -> {
            entry.setTitle(title.getText().toString());
            entry.setText(text.getText().toString());
            entryRepo.updateEntry(entry);
            Toast.makeText(getActivity(), "Entry updated", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                EntryDetailsFragment detailsFragment = new EntryDetailsFragment();
                Bundle b = new Bundle();
                b.putInt("id", entry.getId());
                detailsFragment.setArguments(b);
                replaceFragment(detailsFragment);
            }, 2000);
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                EntryDetailsFragment detailsFragment = new EntryDetailsFragment();
                Bundle b = new Bundle();
                b.putInt("id", entry.getId());
                detailsFragment.setArguments(b);
                replaceFragment(detailsFragment);
            }
        });
    }

    private void replaceFragment(Fragment currFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.entry_frame_layout, currFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }
}