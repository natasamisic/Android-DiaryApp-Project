package com.example.diaryapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.diaryapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutAppFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutAppFragment extends Fragment {

    String about = "• This app is meant to be used as a daily diary.\n" +
            "• Once you open the app a list of your entries is going to be shown, " +
            "unless it is your first time launching the app in which case it will be empty.\n" +
            "• If you click on the menu in the top right corner you will see a button for music " +
            "which will play or stop the music. " +
            "You can also shake your phone to play or stop the music! Isn't that interesting :)\n" +
            "• Next button is used to change the theme of the app to night theme and back to light.\n" +
            "• The \"About\" button you have already figured out.\n" +
            "• On your home page there is a button to add a new entry, where you can add a title, " +
            "text and an image in an entry and save it clicking on the button in the bottom.\n" +
            "• If you tap on the specific entry on the home page it will show you all the " +
            "information that entry contains and you can edit the title and the text of the entry if needed.\n" +
            "• If you long press an entry you will have an option to delete that entry.\n" +
            "• We hope that this helped you understand how to use this app better! :)";
    String linkText = "• Visit the <a href='https://www.urmc.rochester.edu/encyclopedia/content.aspx?ContentID=4552&ContentTypeID=1'>" +
            "Journaling for Mental Health</a> web page to see the benefits of keeping a journal.";
    TextView text;
    TextView link;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AboutAppFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutAppFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutAppFragment newInstance(String param1, String param2) {
        AboutAppFragment fragment = new AboutAppFragment();
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
        return inflater.inflate(R.layout.fragment_about_app, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        link = view.findViewById(R.id.link);
        text = view.findViewById(R.id.abouttext);
        text.setText(about);
        link.setText(Html.fromHtml(linkText));
        link.setMovementMethod(LinkMovementMethod.getInstance());
    }
}