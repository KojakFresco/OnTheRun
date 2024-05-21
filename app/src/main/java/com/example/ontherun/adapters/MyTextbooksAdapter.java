package com.example.ontherun.adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ontherun.R;
import com.example.ontherun.databinding.FragmentMyTextbookBinding;

import java.util.ArrayList;

import com.example.ontherun.fragments.MyTextbooksFragment.Item;
import com.example.ontherun.fragments.TextbookFragment;

public class MyTextbooksAdapter
        extends RecyclerView.Adapter<MyTextbooksAdapter.ViewHolder> {

    private final ArrayList<Item> list;

    AppCompatActivity activity;

    public MyTextbooksAdapter(AppCompatActivity activity, ArrayList<Item> items) {
        this.activity = activity;
        list = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentMyTextbookBinding.inflate(LayoutInflater.from(
                parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = list.get(position);
        holder.imageView.setImageBitmap(item.getBitmap());
        holder.textView.setText(item.getName());
        holder.imageView.setOnClickListener(v -> {

            ProgressBar progressBar = activity.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            activity.findViewById(R.id.blackout).setVisibility(View.VISIBLE);

            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.main_fragment);
            TextbookFragment textbookFragment = (TextbookFragment) fragmentManager.findFragmentByTag(item.getName());

            transaction.detach(currentFragment);

            if (textbookFragment == null) {
                Bundle bundle = new Bundle();
                bundle.putString("name", item.getName());
                bundle.putInt("pagesCount", item.getPageCount());
                //bundle.putStringArrayList("text", (ArrayList<String>) item.getText());
                textbookFragment = new TextbookFragment();
                textbookFragment.setArguments(bundle);
                transaction.add(R.id.main_fragment, textbookFragment, item.getName());
            } else {
                transaction.attach(textbookFragment);
            }
            transaction.commit();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(FragmentMyTextbookBinding binding) {
            super(binding.getRoot());
            imageView = binding.picture;
            textView = binding.name;
        }
    }
}