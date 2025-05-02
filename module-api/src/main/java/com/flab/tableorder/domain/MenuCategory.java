package com.flab.tableorder.domain;

import java.util.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
public class MenuCategory {

    @Id
    private Long categoryId;

    private String categoryName;
    private boolean multiple;
    private boolean required;
    private int maxSelect;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Menu> menu;
}
