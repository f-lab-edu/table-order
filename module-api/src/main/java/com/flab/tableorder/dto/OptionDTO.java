package com.flab.tableorder.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
public class OptionDTO {
	private long optionId;
	private String optionName;
	private int quantity;
	private int price;
	private String image;
	private boolean isSoldOut;
	private boolean isOnlyOne;
	private List<String> tags;
}
