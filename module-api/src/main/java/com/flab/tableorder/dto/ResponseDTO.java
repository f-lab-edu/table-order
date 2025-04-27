package com.flab.tableorder.dto;

import lombok.*;

@Getter
@Setter
public class ResponseDTO<T> {
	private int code;
	private String message;
	private T data;

	public ResponseDTO(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public ResponseDTO(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}
}