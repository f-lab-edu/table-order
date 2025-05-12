package com.flab.tableorder.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "option")
@Getter @Setter
public class OptionCategory {
    @Id @Field("_id")
    private ObjectId categoryId;

    private String categoryName;
    private boolean multiple;
    private boolean required;
    private int maxSelect;

    private List<Option> options;
}
