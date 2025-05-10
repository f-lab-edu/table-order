package com.flab.tableorder.domain;

import lombok.*;

import java.util.List;

@Getter @Setter
public class Option {
    private Long optionId;
    private String optionName;
    private int quantity;
    private int price;
    private String image;
    private boolean soldOut;
    private boolean onlyOne;
    private List<String> tags;
}