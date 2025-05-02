package com.flab.tableorder.domain;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
public class OptionCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long optionCategoryId;

	private String optionCategoryName;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Option> options;

}