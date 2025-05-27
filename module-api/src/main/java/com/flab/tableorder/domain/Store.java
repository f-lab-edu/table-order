package com.flab.tableorder.domain;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "store")
@Getter @Setter
public class Store {
    @Id @Field("_id")
    private ObjectId storeId;
    private String apiKey;
}
