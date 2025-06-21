package com.flab.tableorder.document;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter @Setter
public class Order {
    private ObjectId storeId;
    private int tableNum;
    private ObjectId menuId;

    private String status;
    private int quantity;
    private int price = 0;

    private List<OrderOption> options;
}
