package com.example.diaryapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diaryapp.Entry;
import com.example.diaryapp.MapActivity;
import com.example.diaryapp.R;
import com.example.diaryapp.database.EntryRepository;
import com.google.android.gms.maps.model.LatLng;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntryDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntryDetailsFragment extends Fragment {

    TextView title;
    TextView text;
    ImageView image;
    Button updatebtn;
    EntryRepository entryRepo;
    Entry entry;
    int id;
    Button mapbtn;
    double latitude;
    double longitude;
    ScrollView scrollView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EntryDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EntryDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EntryDetailsFragment newInstance(String param1, String param2) {
        EntryDetailsFragment fragment = new EntryDetailsFragment();
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
        return inflater.inflate(R.layout.fragment_entry_details, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        entryRepo = new EntryRepository(getContext());
        title = view.findViewById(R.id.entrytitle);
        text = view.findViewById(R.id.entrytext);
        image = view.findViewById(R.id.entryimage);
        updatebtn = view.findViewById(R.id.updatebtn);
        mapbtn = view.findViewById(R.id.open_locationbtn);
        scrollView = view.findViewById(R.id.entry_details_scrollview);

        scrollView.setOnTouchListener((v, event) -> {
            scrollView.getParent().requestDisallowInterceptTouchEvent(false);
            return false;
        });

        text.setOnTouchListener((v, event) -> {
            text.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish();
            }
        });

        id = getArguments().getInt("id");
        entry = entryRepo.getEntryById(id);

        title.setText(entry.getTitle());
        text.setText(entry.getText());
        text.setMovementMethod(new ScrollingMovementMethod());
        image.setImageBitmap(entry.getImage());

        mapbtn.setOnClickListener(v -> {
            LatLng latLng = getLatlng();
            if(latitude != 0 && longitude != 0){
                Intent intent =  new Intent(getActivity(), MapActivity.class);
                intent.putExtra("latlng",latLng);
                intent.putExtra("id", entry.getId());
                startActivity(intent);
            }else{
                Toast.makeText(getActivity(), "You did not enter a location for this entry", Toast.LENGTH_SHORT).show();
            }
        });

        updatebtn.setOnClickListener(v -> {
            UpdateEntryFragment detailsFragment = new UpdateEntryFragment();
            Bundle b = new Bundle();
            b.putInt("id", entry.getId());
            detailsFragment.setArguments(b);
            replaceFragment(detailsFragment);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private LatLng getLatlng(){
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(entry.getLocation());
        String from_lat_lng = "";
        while(m.find()) {
            from_lat_lng = m.group(1) ;
        }
        String[] location = from_lat_lng.split(",");
        latitude = Double.parseDouble(location[0]);
        longitude = Double.parseDouble(location[1]);
        return new LatLng(latitude, longitude);
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