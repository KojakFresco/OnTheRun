package com.example.ontherun.fragments;

import static com.example.ontherun.activities.MainActivity.LOG_CODE;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ontherun.R;
//import com.example.ontherun.activities.DataBaseActivity;
import com.example.ontherun.activities.MainActivity;
import com.example.ontherun.clients.RedactorClient;
import com.example.ontherun.page.ParsedResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddingFragment newInstance(String param1, String param2) {
        AddingFragment fragment = new AddingFragment();
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
        View view = inflater.inflate(R.layout.fragment_adding, container, false);
        view.findViewById(R.id.add_demo).setOnClickListener(v -> {
            Log.d(LOG_CODE, "Started");
            getActivity().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.blackout).setVisibility(View.VISIBLE);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            DocumentReference textbook = db.collection("textbooks").document("demo-textbook");

            textbook.get().addOnSuccessListener(documentSnapshot -> {
                Log.d(LOG_CODE, "Document loaded");
                int pages = documentSnapshot.get("size", Integer.class);
                File innerFolder = new File("/data/user/0/com.example.ontherun/files/textbooks/", "demo-textbook");
                if(!innerFolder.exists()) innerFolder.mkdir();

                //downloading the files
                for (int i = 0; i < pages; ++i) {
                    String fileName = "demo-textbook" + "-" + String.format("%03d", i + 1) + ".jpg";
                    File file = new File(innerFolder, fileName);
                    StorageReference fileRef = storage.getReference().child("demo-textbook" + "/" + fileName);
                    int finalI = i;
                    fileRef.getFile(file)
                            .addOnSuccessListener(taskSnapshot -> {
                                Log.d("Files download", "Download Success");
                                if (finalI == pages - 1) {
                                    getActivity().findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                                    getActivity().findViewById(R.id.blackout).setVisibility(View.INVISIBLE);
                                }

                            })
                            .addOnFailureListener(e -> Log.w("Files download", e));
                }

                //downloading the text
                textbook.collection("pages").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(LOG_CODE, "Everything is fine");
                    int number = 0;
                    for (QueryDocumentSnapshot docSnapshot : queryDocumentSnapshots) {
                        List<String> sentences = new ArrayList<>();
                        ParsedResult page = docSnapshot.toObject(ParsedResult.class);
                        sentences.addAll(RedactorClient.getPageText(page));
                        Log.d(LOG_CODE, "page added");
                        ((MainActivity) getActivity()).saveList(sentences, number++, "demo-textbook");
                    }

                }).addOnFailureListener(e -> {
                    Log.e(LOG_CODE, "Error with getting collection");
                });
            }).addOnFailureListener(e -> Log.e(LOG_CODE, "Size load failed: " + e));
        });

//        view.findViewById(R.id.database).setOnClickListener(v -> {
//            startActivity(new Intent(requireContext(), DataBaseActivity.class));
//        });
        return view;
    }
}