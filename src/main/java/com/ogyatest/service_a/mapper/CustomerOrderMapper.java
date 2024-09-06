package com.ogyatest.service_a.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import com.ogyatest.service_a.model.CustomerOrder;

public class CustomerOrderMapper implements RowMapper<CustomerOrder> {

	@Override
	public CustomerOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
		CustomerOrder o = CustomerOrder.builder().id(rs.getLong("id"))
				.customerId(UUID.fromString(rs.getString("cust_id")))
				.productId(UUID.fromString(rs.getString("prod_id"))).name(rs.getString("name"))
				.category(rs.getString("category")).price(rs.getLong("price")).build();
		return o;
	}

}
