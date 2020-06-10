package com.example.foodiesapp.ui.adapters.adapter.hashtag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiesapp.R;
import com.example.foodiesapp.models.Assignment.Assignment;
import com.example.foodiesapp.models.Ingredient.Ingredient;

import java.util.List;

public final class HashtagListAdapter extends RecyclerView.Adapter<HashtagListHolder> {

    private List<Assignment> data;
    public HashtagListAdapter(List<Assignment> ingredients) {
        data = ingredients;
    }

    @NonNull
    @Override
    public HashtagListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view_hashtags_view, parent, false);
        return new HashtagListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HashtagListHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }
}