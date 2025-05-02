package com.flab.tableorder.domain;

import java.util.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
public class Store {

    @Id
    private Long storeId;

    private String apiKey;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MenuCategory> categories = new ArrayList<>();
}
