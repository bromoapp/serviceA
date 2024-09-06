package com.ogyatest.service_a.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import com.ogyatest.service_a.model.Item;

public class ItemMapper implements RowMapper<Item> {

	@Override
	public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
		Item o = Item.builder().uuid(UUID.fromString(rs.getString("uuid")))
				.categoryId(UUID.fromString(rs.getString("category_id"))).name(rs.getString("name"))
				.price(rs.getLong("price")).build();
		return o;
	}

}
