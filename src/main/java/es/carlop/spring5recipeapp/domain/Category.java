package es.carlop.spring5recipeapp.domain;

import lombok.*;

import java.util.Set;

@Data
public class Category {

    private String id;
    private String description;
    private Set<Recipe> recipes;

}
