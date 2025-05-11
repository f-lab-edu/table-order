package com.flab.tableorder.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

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