package com.flab.tableorder.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Data
public class OptionDTO {
    private String optionId;
    private String optionName;
    private String image;
    private boolean soldOut = false;
    //	onlyOne == true : 한 개만 선택 가능
    private boolean onlyOne = false;
    private List<String> tags;
}
