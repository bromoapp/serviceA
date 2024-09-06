package com.ogyatest.service_a.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ogyatest.service_a.dao.CustomerRepository;
import com.ogyatest.service_a.model.Customer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerDetailsService implements UserDetailsService {

	private CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Customer o = customerRepository.findCustomerByUsername(username);
		List<String> roles = new ArrayList<>();
		roles.add("USER");
		UserDetails details = User.builder().username(o.getUsername()).password(o.getPassword())
				.roles(roles.toArray(new String[0])).build();
		return details;
	}

}
