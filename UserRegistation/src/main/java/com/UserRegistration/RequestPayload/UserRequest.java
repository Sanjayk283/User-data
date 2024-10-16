package com.UserRegistration.RequestPayload;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Setter
@Getter
public class UserRequest {

	private Long id;
	private String userName;
	private String email;
	private String mobileNumber;
	private String address;
	private String password;
	private String role;
}
