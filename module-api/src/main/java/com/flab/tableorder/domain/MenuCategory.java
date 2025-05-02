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

    @OneToMany(cascade = CascadeType.ALL)
    private List<Menu> menu;
}
