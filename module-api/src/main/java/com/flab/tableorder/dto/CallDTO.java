package com.flab.tableorder.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Data
public class CallDTO {
    private long callId = 0;
    private String callName = "";
}
