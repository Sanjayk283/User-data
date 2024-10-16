package com.UserRegistration.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class APIResponse {

	private Object data;
	private String message;
	private Integer status;
}
