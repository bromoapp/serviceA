package com.ogyatest.service_a.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import com.ogyatest.service_a.model.ItemCategory;

public class ItemCategoryMapper implements RowMapper<ItemCategory> {

	@Override
	public ItemCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
		ItemCategory o = ItemCategory.builder().uuid(UUID.fromString(rs.getString("uuid")))
				.category(rs.getString("category")).build();
		return o;
	}

}
