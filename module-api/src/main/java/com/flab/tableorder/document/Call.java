package com.flab.tableorder.document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "call")
@Getter @Setter
public class Call {
    @Id @Field("_id")
    private ObjectId callId;
    private String callName;
    private ObjectId storeId;
}
