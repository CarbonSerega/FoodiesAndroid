package com.example.foodiesapp.ui.adapters.adapter.hashtag;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiesapp.R;
import com.example.foodiesapp.models.Assignment.Assignment;
import com.example.foodiesapp.models.Ingredient.Ingredient;

final class HashtagListHolder extends RecyclerView.ViewHolder {

    private Assignment assignment;
    private TextView hashtagTextView;
    private View itemView;



    HashtagListHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;

        hashtagTextView = itemView.findViewById(R.id.hashtag_text_view);

    }

    void bind(Assignment assignment) {

        this.assignment = assignment;

        if(assignment != null)
           hashtagTextView.setText(assignment.getName());

    }
}
