package com.flab.tableorder.domain;

import java.util.*;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "category_id")
    private MenuCategory category;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
