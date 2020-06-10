package com.example.foodiesapp.ui.adapters.adapter.ingredient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiesapp.R;
import com.example.foodiesapp.models.Ingredient.Ingredient;

import java.util.List;

public final class IngredientListAdapter extends RecyclerView.Adapter<IngredientListHolder> {

    private List<Ingredient> data;
    public IngredientListAdapter(List<Ingredient> ingredients) {
        data = ingredients;
    }

    @NonNull
    @Override
    public IngredientListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view_ingredients_view, parent, false);
        return new IngredientListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientListHolder holder, int position) {
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