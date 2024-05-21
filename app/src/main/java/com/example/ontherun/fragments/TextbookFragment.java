package com.example.ontherun.fragments;

import static com.example.ontherun.activities.MainActivity.LOG_CODE;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ontherun.R;
import com.example.ontherun.activities.MainActivity;
import com.example.ontherun.adapters.MyArrayAdapter;
import com.example.ontherun.adapters.TextbookAdapter;
import com.example.ontherun.clients.RedactorClient;
import com.example.ontherun.clients.TextToSpeechClient;
import com.example.ontherun.databinding.FragmentTextbookBinding;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

public class TextbookFragment extends Fragment {
    Bundle args;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NAME = "name";
    private static final String TEXT = "text";
    private static final String PAGES_COUNT = "pagesCount";

    private String name;

    private final ArrayList<Bitmap> pages = new ArrayList<>();
    private List<List<String>> text;

    private int firstPage;

    TextToSpeechClient ttsClient;


    public TextbookFragment() {
        // Required empty public constructor
    }

    public static TextbookFragment newInstance(String param1, String param2) {
        TextbookFragment fragment = new TextbookFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ttsClient = new TextToSpeechClient(getContext());
        text = new ArrayList<>();

        args = this.getArguments();
        name = args.getString(NAME);
        for (int i = 0; i < 114; ++i) {
            text.add(((MainActivity) getActivity()).loadList(name, i));
        }

        firstPage = RedactorClient.getFirstPage(text);

        ttsClient.setTextbook(text.subList(firstPage, text.size()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTextbookBinding binding = FragmentTextbookBinding.inflate(inflater, container, false);

        //TODO: save progress after closing app

        RecyclerView recyclerView = binding.recycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(new TextbookAdapter(generateList()));


        int page = ((MainActivity) getActivity()).loadInt(name + "_page");
        int sentence = ((MainActivity) getActivity()).loadInt(name + "_sentence");
        ttsClient.setPage(page);
        ttsClient.setSentence(sentence);
        ttsClient.speakText();

        ttsClient.pause();

//        FloatingActionButton fab = binding.fab;

        AutoCompleteTextView dropdown = binding.autoCompleteTextView;

        String[] items = new String[args.getInt(PAGES_COUNT) - firstPage];

        for (int i = 0; i < args.getInt(PAGES_COUNT) - firstPage; ++i) {
            items[i] = "Стр " + (i + firstPage) ;
        }
        Log.d(LOG_CODE, "size: " + items.length);
        MyArrayAdapter<String> adapter = new MyArrayAdapter<>(getContext(), R.layout.list_item, items);
        dropdown.setAdapter(adapter);
        Log.d(LOG_CODE, "pppp");
        dropdown.setText(items[((MainActivity) getActivity()).loadInt(name + "_page")], false);

        Drawable play = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_play_button_24, null);
        Drawable pause = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_pause_button_24, null);
        ImageButton playButton = binding.playButton;
        playButton.setOnClickListener(v -> {
            if (playButton.getDrawable() == play) {
                playButton.setImageDrawable(pause);
                ttsClient.resume();
            } else {
                playButton.setImageDrawable(play);
                ttsClient.pause();
            }

        });

        dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ttsClient.pause();
                ttsClient.setPage(position);
                if (playButton.getDrawable() == play) {
                    playButton.setImageDrawable(pause);
                }
                ttsClient.resume();
            }
        });




        binding.backButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            MenuFragment textbookFragment = (MenuFragment) fragmentManager.findFragmentByTag("menu");

            transaction.detach(this);
            transaction.attach(textbookFragment);
            transaction.commit();

            ttsClient.pause();
        });



        binding.rewindButton.setOnClickListener(v -> {
            ttsClient.nextSentence(false);
        });
        binding.fastForwardButton.setOnClickListener(v -> {
            ttsClient.nextSentence(true);
        });

//        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .setAnchorView(R.id.fab).show());


        getActivity().findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.blackout).setVisibility(View.INVISIBLE);
        return binding.getRoot();
    }

    private ArrayList<Bitmap> generateList() {
        if (pages.isEmpty()) {
            for (int i = 0; i < args.getInt(PAGES_COUNT); ++i) {
                pages.add(BitmapFactory.decodeFile("/data/user/0/com.example.ontherun/files/textbooks/" + name + "/" + name + "-" + String.format("%03d", i + 1) + ".jpg"));
            }
        }

        return pages;
    }


    @Override
    public void onStop() {
        ttsClient.onStop((MainActivity) getActivity(), name);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        ttsClient.onDestroy((MainActivity) getActivity(), name);
        super.onDestroy();
    }
}