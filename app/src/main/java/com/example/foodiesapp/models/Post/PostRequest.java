package com.example.foodiesapp.models.Post;

import com.example.foodiesapp.models.Post.Filter.Filter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public final class PostRequest {
    @SerializedName("user_id")
    private long userId;

    @SerializedName("user_locale")
    private String userLocale;

    @SerializedName("last_post_id")
    private long lastPostId;

    @SerializedName("last_post_time")
    private Date lastPostTime;

    @SerializedName("search_context")
    private String searchContext;

    private List<Filter> filters;

    public PostRequest(long userId, String userLocale, long lastPostId, Date lastPostTime, String searchContext, List<Filter> filters) {
        this.userId = userId;
        this.userLocale = userLocale;
        this.lastPostId = lastPostId;
        this.lastPostTime = lastPostTime;
        this.searchContext = searchContext;
        this.filters = filters;
    }
}
