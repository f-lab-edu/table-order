package com.flab.tableorder.domain;

import java.util.List;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "menu")
@Getter @Setter
public class Menu {
    @Id @Field("_id")
    private ObjectId menuId;

    private String menuName;
    private String description;
    private int quantity;
    private int price;
    private int salePrice;
    private String image;
    private boolean soldOut;
    private boolean optionEnabled;
    private List<String> tags;

    private ObjectId categoryId;
}
