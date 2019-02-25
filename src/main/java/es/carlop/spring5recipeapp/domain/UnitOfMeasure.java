package es.carlop.spring5recipeapp.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class UnitOfMeasure {

    @Id
    private String id;
    private String description;

}
