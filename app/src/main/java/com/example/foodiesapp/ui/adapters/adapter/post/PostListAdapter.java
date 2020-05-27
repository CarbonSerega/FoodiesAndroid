package com.example.foodiesapp.ui.adapters.adapter.post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiesapp.R;
import com.example.foodiesapp.models.Post.Post;
import com.example.foodiesapp.models.Post.PostResponse;
import com.example.foodiesapp.ui.home.HomeViewModel;
import com.example.foodiesapp.utils.web.StatusCodes;

import java.util.ArrayList;
import java.util.List;

public final class PostListAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private PostSelectedListener postSelectedListener;
    private List<Post> data = new ArrayList<>();

    public PostListAdapter(HomeViewModel viewModel, LifecycleOwner lifecycleOwner, PostSelectedListener repoSelectedListener) {
        this.postSelectedListener = repoSelectedListener;
        viewModel.getPostsResponse().observe(lifecycleOwner, posts -> {
            data.clear();
            if (posts.getStatusCode() == StatusCodes.SUCCESS) {
                PostResponse postResponse = posts.getPostResponse();
                if(postResponse != null) {
                    if (!postResponse.getPosts().isEmpty()) {
                        data.addAll(postResponse.getPosts());
                        notifyDataSetChanged();
                    }
                }
            }
        });

        setHasStableIds(true);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view, parent, false);
        return new PostViewHolder(view, postSelectedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
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