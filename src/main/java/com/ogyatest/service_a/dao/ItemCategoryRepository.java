package com.ogyatest.service_a.dao;

import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ogyatest.service_a.mapper.ItemCategoryMapper;
import com.ogyatest.service_a.model.ItemCategory;

@Repository
public class ItemCategoryRepository {

	private static final String tbl_name = "item_category";
	private JdbcTemplate template;

	public ItemCategoryRepository(DataSource ds) {
		this.template = new JdbcTemplate(ds);
	}

	public List<ItemCategory> findAllCategories() {
		try {
			String sql = "SELECT * FROM " + tbl_name;
			return template.query(sql, new ItemCategoryMapper());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ItemCategory findCategoryById(UUID uuid) {
		try {
			String sql = "SELECT * FROM " + tbl_name + " AS o WHERE o.uuid = '" + uuid.toString() + "'";
			return template.queryForObject(sql, new ItemCategoryMapper());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
