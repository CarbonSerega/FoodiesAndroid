package com.example.foodiesapp.models.Post;

import com.example.foodiesapp.models.Assignment.Assignment;
import com.example.foodiesapp.models.Category.Category;
import com.example.foodiesapp.models.Cuisine.Cuisine;
import com.example.foodiesapp.models.Error;
import com.example.foodiesapp.models.Ingredient.Ingredient;
import com.example.foodiesapp.models.Step.Step;
import com.example.foodiesapp.models.User.User;

import java.util.Date;
import java.util.List;

public class Post {
    private long id;
    private String title;
    private String cooktime;
    private String image;
    private String description;
    private Date pubtime;
    private int shares;
    private int likes;
    private float proteins;
    private float lipids;
    private float carbohydrates;
    private float calorific;

    private User user;
    private Cuisine cuisine;
    private Category category;
    private List<Ingredient> ingredients;
    private List<Assignment> assignments;
    private List<Step> steps;

    private Error error;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCooktime() {
        return cooktime;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public Date getPubtime() {
        return pubtime;
    }

    public int getShares() {
        return shares;
    }

    public int getLikes() {
        return likes;
    }

    public float getProteins() {
        return proteins;
    }

    public float getLipids() {
        return lipids;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public float getCalorific() {
        return calorific;
    }

    public User getUser() {
        return user;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public Category getCategory() {
        return category;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public Error getError() {
        return error;
    }
}