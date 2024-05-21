package com.example.ontherun.fragments;

import static com.example.ontherun.activities.MainActivity.params;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ontherun.R;
import com.example.ontherun.activities.MainActivity;
import com.example.ontherun.clients.TextToSpeechClient;
import com.example.ontherun.databinding.FragmentMenuBinding;
import com.google.android.material.navigation.NavigationBarView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    MyTextbooksFragment myTextbooksFragment = new MyTextbooksFragment();
    AddingFragment addingFragment =  new AddingFragment();
    SettingsFragment settingsFragment =  new SettingsFragment();

    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        getChildFragmentManager().beginTransaction().replace(R.id.inner_fragment, myTextbooksFragment).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentMenuBinding binding = FragmentMenuBinding.inflate(inflater, container, false);

        binding.bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.home_page) {
                    getChildFragmentManager().beginTransaction().replace(R.id.inner_fragment, myTextbooksFragment).commit();
                }
                if (menuItem.getItemId() == R.id.add_page) {
                    getChildFragmentManager().beginTransaction().replace(R.id.inner_fragment, addingFragment).commit();
                    MainActivity.ttsClient.speakSentence("Здравствуйте, мои дорогие", "Hello");
                }
                if (menuItem.getItemId() == R.id.settings_page) {
                    getChildFragmentManager().beginTransaction().replace(R.id.inner_fragment, settingsFragment).commit();
                }
                menuItem.setChecked(true);
                return false;
            }
        });

        return binding.getRoot();
    }
}