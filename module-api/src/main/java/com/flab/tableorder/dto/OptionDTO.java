package com.flab.tableorder.dto;

import java.util.List;

import lombok.*;

@Getter @Setter @Data
public class OptionDTO {
	private long optionId = 0;
	private String optionName = "";
	private int quantity = 0;
	private int price = 0;
	private String image = "";
	private boolean soldOut = false;
	//	onlyOne == true : 한 개만 선택 가능
	private boolean onlyOne = false;
	private List<String> tags;
}
