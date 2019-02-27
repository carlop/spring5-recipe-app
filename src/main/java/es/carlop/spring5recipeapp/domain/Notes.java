package es.carlop.spring5recipeapp.domain;

import lombok.*;
import org.springframework.data.annotation.Id;


@Data
public class Notes {

    @Id
    private String id;
    private Recipe recipe;
    private String recipeNotes;

}
