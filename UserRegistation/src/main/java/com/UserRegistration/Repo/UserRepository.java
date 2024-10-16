package com.UserRegistration.Repo;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.UserRegistration.modal.UserInfo;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {

	
	UserInfo findByMobileNumber(String mobileNumber);
	
	UserInfo findByPassword(String password);
	
   // Optional<UserInfo>  findbyusername(String userName);



	Optional<UserInfo> findByUserName(String username);


  //  UserInfo findByUsername(String trim);
}
