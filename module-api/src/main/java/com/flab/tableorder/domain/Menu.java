package com.flab.tableorder.domain;

import java.util.*;

import lombok.*;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.*;

@Entity
@Getter @Setter
public class Menu {

    @Id
    private Long menuId;

    private String menuName;
    private String description;
    private int quantity;
    private int price;
    private int salePrice;
    private String image;
    private boolean soldOut;
    private boolean optionEnabled;

    @ElementCollection
    private List<String> tags;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OptionCategory> options = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private MenuCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @JsonBackReference
    private Store store;
}
