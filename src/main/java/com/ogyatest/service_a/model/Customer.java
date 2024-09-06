package com.ogyatest.service_a.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
public class Customer {

	private UUID uuid;
	private String name;
	private String username;
	private String password;
	private Long balance;
	private String email;

	@Override
	public String toString() {
		return "Customer [uuid=" + uuid + ", name=" + name + ", username=" + username + ", password=" + password
				+ ", balance=" + balance + ", email=" + email + "]";
	}

}
