package com.flab.tableorder.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "option")
@Getter @Setter
public class OptionCategory {
	@Id @JsonProperty("_id")
	private ObjectId categoryId;

	private String categoryName;
	private boolean multiple;
	private boolean required;
	private int maxSelect;

	private List<Option> options;
}