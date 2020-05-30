package com.example.foodiesapp.ui.adapters.adapter.post;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodiesapp.R;
import com.example.foodiesapp.models.Post.Post;

import de.hdodenhof.circleimageview.CircleImageView;

final class PostViewHolder extends RecyclerView.ViewHolder {

    private Post post;

    private View itemView;

    private CircleImageView currentUserImageView;
    private TextView postTitleTextView;
    private TextView cookTimeTextView;
    private TextView categoryTextView;

    private ImageView postMainImageView;
    private TextView postDescriptionTextView;
    private TextView postPubTimeTextView;
    private TextView likesAmountTextView;

    private ImageView likeIconImageView;

    PostViewHolder(@NonNull View itemView, PostSelectedListener postSelectedListener) {
        super(itemView);
        this.itemView = itemView;
        currentUserImageView = itemView.findViewById(R.id.current_user_image_view);
        postTitleTextView = itemView.findViewById(R.id.post_title_text_view);
        cookTimeTextView = itemView.findViewById(R.id.cook_time_text_view);
        categoryTextView = itemView.findViewById(R.id.category_text_view);
        postMainImageView = itemView.findViewById(R.id.post_main_image_view);
        postDescriptionTextView = itemView.findViewById(R.id.post_description_text_view);
        postPubTimeTextView = itemView.findViewById(R.id.post_pub_time_text_view);
        likesAmountTextView = itemView.findViewById(R.id.likes_amount_text_view);
        likeIconImageView = itemView.findViewById(R.id.like_icon_image_view);

        itemView.setOnClickListener(v -> {
            if(post != null) {
                postSelectedListener.onPostSelected(post);
            }
        });
    }

    void bind(Post post) {

        this.post = post;

        if(post.getUser().getPicture() != null)
            Glide.with(itemView)
                    .load(post.getUser().getPicture())
                    .into(currentUserImageView);
        else
            currentUserImageView.setImageDrawable(null);

        postTitleTextView.setText(post.getTitle());
        cookTimeTextView.setText(post.getCooktime());

        if(post.getImage() != null)
            Glide.with(itemView)
                    .load(post.getImage())
                    .centerCrop()
                    .into(postMainImageView);
        else
            postMainImageView.setImageDrawable(null);

        postDescriptionTextView.setText(post.getDescription());
        postPubTimeTextView.setText(post.getPubtime().toString());
        categoryTextView.setText(post.getCategory().getName());

        String likes = post.getLikes()+"";
        likesAmountTextView.setText(likes);

        ImageViewCompat.setImageTintList(likeIconImageView,
                ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(),
                        post.isLikedByUser() ? R.color.colorPrimary : R.color.userGrayColor)));

    }
}
