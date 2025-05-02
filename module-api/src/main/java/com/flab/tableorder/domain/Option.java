package com.flab.tableorder.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter
public class Option {

    @Id
    private Long optionId;

    private String optionName;
    private int quantity;
    private int price;
    private String image;
    private boolean soldOut;
    private boolean onlyOne;

    @ElementCollection
    private List<String> tags;

}