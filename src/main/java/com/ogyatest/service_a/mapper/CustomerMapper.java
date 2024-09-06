package com.ogyatest.service_a.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import com.ogyatest.service_a.model.Customer;

public class CustomerMapper implements RowMapper<Customer> {

	@Override
	public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
		Customer o = Customer.builder().uuid(UUID.fromString(rs.getString("uuid"))).name(rs.getString("name"))
				.username(rs.getString("username")).password(rs.getString("password")).balance(rs.getLong("balance"))
				.email(rs.getString("email")).build();
		return o;
	}

}
