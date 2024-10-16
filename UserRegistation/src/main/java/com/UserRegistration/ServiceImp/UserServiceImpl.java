package com.UserRegistration.ServiceImp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.UserRegistration.Repo.UserRepository;
import com.UserRegistration.RequestPayload.LoginRequest;
import com.UserRegistration.RequestPayload.UserRequest;
import com.UserRegistration.Service.UserService;
import com.UserRegistration.config.APIResponse;
import com.UserRegistration.modal.UserInfo;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public APIResponse addUser(UserRequest request) {
        APIResponse response = new APIResponse();
        request.setPassword(encoder.encode(request.getPassword()));
        LocalDateTime creationDate = LocalDateTime.now();
        if (request.getId() == 0) {// add user
            if (!request.getMobileNumber().isBlank() && !request.getPassword().isBlank()) {
                Optional<UserInfo> oldUser = userRepository.findByUserName(request.getUserName());
                if (oldUser.isPresent()) {
                    response.setData("");
                    response.setMessage("User already exists!");
                    response.setStatus(400);
                    return response;
                }
                UserInfo userInfo = new UserInfo();
                userInfo.setIsActive(true);
                userInfo.setCreationDate(creationDate);

                BeanUtils.copyProperties(request, userInfo);
                UserInfo addUser = userRepository.save(userInfo);
                if (addUser != null) {
                    response.setData("");
                    response.setMessage("Data added successfully!");
                    response.setStatus(200);
                    return response;
                }
            } else {
                response.setData("");
                response.setMessage("Please enter mobile number and password");
                response.setStatus(400);
                return response;
            }
            return null;
        } else {// update user
            UserInfo userInfo = userRepository.findById(request.getId()).orElse(null);
            if (userInfo != null) {
                userInfo.setUpdationDate(creationDate);
                BeanUtils.copyProperties(request, userInfo);
                UserInfo updateUser = userRepository.save(userInfo);
                if (updateUser != null) {
                    response.setData("");
                    response.setMessage("Data updated successfully!");
                    response.setStatus(200);
                    return response;
                }
            } else {
                response.setData("");
                response.setMessage("User not found!");
                response.setStatus(404);
                return response;
            }
        }
        return response;

    }

    @Override
    public APIResponse getAllList() {
        APIResponse response = new APIResponse();
        List<UserInfo> list = userRepository.findAll();
        if (!list.isEmpty()) {
            //	Collections.sort(list);
            response.setData(list);
            response.setMessage("Data fetch successfully!");
            response.setStatus(200);
            return response;
        } else {
            response.setData(new ArrayList<>());
            response.setMessage("Record not found!");
            response.setStatus(404);
            return response;
        }
    }

    @Override
    public APIResponse getAllListpagination(Integer pageNumber, Integer pageSize, String SortBy) {

        Pageable p = PageRequest.of(pageNumber, pageSize, Sort.by(SortBy));
        APIResponse response = new APIResponse();
        Page<UserInfo> list = userRepository.findAll(p);
        List<UserInfo> content = list.getContent();

        if (!list.isEmpty()) {
            //Collections.sort(list);
            response.setData(content);
            response.setMessage("Data fetch successfully!");
            response.setStatus(200);
            return response;
        }
        response.setData(new ArrayList<>());
        response.setMessage("Record not found!");
        response.setStatus(404);
        return response;
    }

    @Override
    public APIResponse login(LoginRequest loginRequest) {
        APIResponse response = new APIResponse();
        if (loginRequest.getMobileNumber().isBlank() && loginRequest.getPassword().isBlank()) {
            response.setData("");
            response.setMessage("Invalid Credential!");
            response.setStatus(400);
            return response;
        }
        UserInfo mobileNumber = userRepository.findByMobileNumber(loginRequest.getMobileNumber());
        if (mobileNumber != null) {
            if (mobileNumber.getPassword().equalsIgnoreCase(loginRequest.getPassword())) {
                response.setData("");
                response.setMessage("Login successfully!");
                response.setStatus(200);
                return response;
            } else {
                response.setData("");
                response.setMessage("Invalid Credential!");
                response.setStatus(400);
                return response;
            }
        } else {
            response.setData("");
            response.setMessage("Invalid Credential!");
            response.setStatus(400);
            return response;
        }
    }


}
