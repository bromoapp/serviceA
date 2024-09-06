package com.ogyatest.service_a.dao;

import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ogyatest.service_a.mapper.ItemMapper;
import com.ogyatest.service_a.model.Item;

@Repository
public class ItemRepository {

	private static final String tbl_name = "item";
	private JdbcTemplate template;

	public ItemRepository(DataSource ds) {
		this.template = new JdbcTemplate(ds);
	}

	public List<Item> findItemsByCategoryId(UUID uuid) {
		try {
			String sql = "SELECT * FROM " + tbl_name + " AS o WHERE o.category_id = '" + uuid.toString() + "'";
			return template.query(sql, new ItemMapper());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Item findByItemId(UUID uuid) {
		try {
			String sql = "SELECT * FROM " + tbl_name + " AS o WHERE o.uuid = '" + uuid.toString() + "'";
			return template.queryForObject(sql, new ItemMapper());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
