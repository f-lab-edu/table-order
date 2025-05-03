package com.flab.tableorder.domain;

import java.util.*;

import lombok.*;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.*;

@Entity
@Getter @Setter
public class MenuCategory {

    @Id
    private Long categoryId;

    private String categoryName;
    private boolean multiple;
    private boolean required;
    private int maxSelect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @JsonBackReference
    private Store store;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Menu> menu;
}
