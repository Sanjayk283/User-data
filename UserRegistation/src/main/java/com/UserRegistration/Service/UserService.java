package com.UserRegistration.Service;

import com.UserRegistration.RequestPayload.LoginRequest;
import com.UserRegistration.RequestPayload.UserRequest;
import com.UserRegistration.config.APIResponse;

public interface UserService {

	APIResponse addUser(UserRequest request);
	
	APIResponse getAllList();
	
	APIResponse login(LoginRequest loginRequest);
	
	APIResponse getAllListpagination(Integer pageNumber,Integer pageSize,String SortBy);
}
