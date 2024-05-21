package com.example.ontherun.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;

import java.util.List;

public class MyArrayAdapter<T> extends ArrayAdapter<T> {
    public MyArrayAdapter(@NonNull Context context, int resource, @NonNull Object[] objects) {
        super(context, resource, (T[]) objects);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }
        };
    }
}
