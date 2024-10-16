package com.UserRegistration.config;

import java.util.ArrayList;
import java.util.Optional;

import com.UserRegistration.Repo.UserRepository;
import com.UserRegistration.modal.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
@Primary
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userLoginRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserInfo> userOptional = userLoginRepo.findByUserName(username);

		if (userOptional.isPresent()) {
			UserInfo user = userOptional.get();
			return new User(user.getUserName(), user.getPassword(), new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}
