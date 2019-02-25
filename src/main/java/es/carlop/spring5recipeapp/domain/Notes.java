package es.carlop.spring5recipeapp.domain;

import lombok.*;


@Data
public class Notes {

    private String id;
    private Recipe recipe;
    private String recipeNotes;

}
