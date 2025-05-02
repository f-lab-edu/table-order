package com.flab.tableorder.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    private String optionName;
    private int quantity;
    private int price;
    private String image;
    private boolean isSoldOut;
    private boolean isOnlyOne;

}