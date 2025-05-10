package com.flab.tableorder.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import jakarta.persistence.*;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "category")
@Getter @Setter
public class Category {
    @Id @JsonProperty("_id")
    private ObjectId categoryId;

    private String categoryName;
    private boolean multiple;
    private boolean required;
    private int maxSelect;

    private ObjectId storeId;
}
