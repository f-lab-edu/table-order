package com.flab.tableorder.domain;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter @Setter
public class OrderOption {
    private ObjectId optionId;
    private int quantity;
    private int price = 0;
}
