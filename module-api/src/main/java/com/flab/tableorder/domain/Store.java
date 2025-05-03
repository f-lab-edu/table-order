package com.flab.tableorder.domain;

import java.util.*;

import lombok.*;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Getter @Setter
public class Store {

    @Id
    private Long storeId;

    private String apiKey;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<MenuCategory> categories = new ArrayList<>();
}
