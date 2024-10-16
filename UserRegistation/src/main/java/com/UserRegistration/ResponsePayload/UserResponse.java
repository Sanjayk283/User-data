package com.UserRegistration.ResponsePayload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Setter
@Getter
public class UserResponse {

	private Long id;
	private String userName;
	private String email;
	private String mobileNumber;
	private String address;
	private Boolean isActive;
	
}
