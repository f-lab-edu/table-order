package com.flab.tableorder.domain;

import java.util.*;

import lombok.*;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.*;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "menu")
@Getter @Setter
public class Menu {
    @Id @JsonProperty("_id")
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
