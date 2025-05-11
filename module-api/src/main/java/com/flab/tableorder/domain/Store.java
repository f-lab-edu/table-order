package com.flab.tableorder.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "store")
@Getter @Setter
public class Store {
    @Id @JsonProperty("_id")
    private ObjectId storeId;
    private String apiKey;
}