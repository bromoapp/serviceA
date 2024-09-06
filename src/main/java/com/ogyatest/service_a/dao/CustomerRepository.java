package com.ogyatest.service_a.dao;

import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ogyatest.service_a.mapper.CustomerMapper;
import com.ogyatest.service_a.model.Customer;

@Repository
public class CustomerRepository {

	private static final String tbl_name = "customer";
	private JdbcTemplate template;

	public CustomerRepository(DataSource ds) {
		this.template = new JdbcTemplate(ds);
	}

	public Customer findCustomerById(UUID uuid) {
		try {
			String sql = "SELECT * FROM " + tbl_name + " AS o WHERE o.uuid = '" + uuid.toString() + "'";
			return template.queryForObject(sql, new CustomerMapper());
		} catch (Exception e) {
			return null;
		}
	}

	public Customer findCustomerByUsername(String username) {
		try {
			String sql = "SELECT * FROM " + tbl_name + " AS o WHERE o.username = '" + username + "'";
			return template.queryForObject(sql, new CustomerMapper());
		} catch (Exception e) {
			return null;
		}
	}

	public boolean updateCustomerBalance(UUID uuid, Long balance) {
		try {
			String sql = "UPDATE " + tbl_name + " SET balance = " + balance + " WHERE uuid = '" + uuid.toString() + "'";
			template.execute(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
