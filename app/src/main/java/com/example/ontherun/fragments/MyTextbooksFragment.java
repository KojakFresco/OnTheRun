package com.example.ontherun.fragments;

import static com.example.ontherun.activities.MainActivity.LOG_CODE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ontherun.adapters.MyTextbooksAdapter;
import com.example.ontherun.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class MyTextbooksFragment extends Fragment {

    private final ArrayList<Item> items = new ArrayList<>();

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int columnCount = 2;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyTextbooksFragment() {
    }

    @SuppressWarnings("unused")
    public static MyTextbooksFragment newInstance(int columnCount) {
        MyTextbooksFragment fragment = new MyTextbooksFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            columnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_textbooks_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (columnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
            }
            recyclerView.setAdapter(new MyTextbooksAdapter((AppCompatActivity) getActivity(), generateList()));
        }
        return view;
    }

    private ArrayList<Item> generateList() {
        //TODO: normal list
        if (items.isEmpty()) {
            File folder = new File("/data/user/0/com.example.ontherun/files/textbooks/");
            File[] textbooks = folder.listFiles();
            Log.d(LOG_CODE, "size:" + textbooks.length);
            for (int i = 0; i < textbooks.length; ++i) {
                items.add(new Item(textbooks[i].getName(), 114));
            }

        }
        return items;
    }
    public class Item {
        private final Bitmap bitmap;
        private final String name;
        private final int pageCount;
        //private final List<String> text;

        private TextbookFragment textbookFragment;

        public Bitmap getBitmap() {
            return bitmap;
        }

        public String getName() {
            return name;
        }

        public int getPageCount() {
            return pageCount;
        }
//        public List<String> getText() {
//            return text;
//        }

        public TextbookFragment getTextbookFragment() {
            return textbookFragment;
        }

        public Item(String name, int pageCount) {
            this.textbookFragment = new TextbookFragment();
            this.name = name;
            this.pageCount = pageCount;
            //this.text = text;

            Bitmap bitmap = BitmapFactory
                    .decodeFile("/data/user/0/com.example.ontherun/files/textbooks/" +
                            name + "/" + name + "-001.jpg");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byteArray = stream.toByteArray();
            this.bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        }
    }
}