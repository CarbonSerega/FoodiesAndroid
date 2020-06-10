package com.example.foodiesapp.ui.adapters.adapter.ingredient;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodiesapp.R;
import com.example.foodiesapp.models.Ingredient.Ingredient;
import com.example.foodiesapp.models.Post.Post;
import com.example.foodiesapp.ui.adapters.adapter.post.PostListAdapter;
import com.example.foodiesapp.ui.adapters.adapter.post.PostSelectedListener;

import de.hdodenhof.circleimageview.CircleImageView;

final class IngredientListHolder extends RecyclerView.ViewHolder {

    private Ingredient ingredient;
    private TextView ingredientShortenedTextView;
    private View itemView;



    IngredientListHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;

        ingredientShortenedTextView = itemView.findViewById(R.id.ingredient_shortened_text_view);

    }

    void bind(Ingredient ingredient) {

        this.ingredient = ingredient;

        ingredientShortenedTextView.setText(ingredient.getName());

    }
}
