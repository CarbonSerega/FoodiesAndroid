package com.example.foodiesapp.models.Post.Filter;

import java.util.List;

public final class Filter {
    private int id;
    private List<String> content;


    public Filter(int id, List<String> content) {
        this.id = id;
        this.content = content;
    }
}
