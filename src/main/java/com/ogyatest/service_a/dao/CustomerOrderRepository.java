package com.ogyatest.service_a.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ogyatest.service_a.mapper.CustomerOrderMapper;
import com.ogyatest.service_a.model.CustomerOrder;

@Repository
@SuppressWarnings("unused")
public class CustomerOrderRepository {

	private static final String tbl_name = "cust_order";
	private JdbcTemplate template;
	private Connection conn;

	public CustomerOrderRepository(DataSource ds) throws Exception {
		this.template = new JdbcTemplate(ds);
		this.conn = ds.getConnection();
	}

	public long addNewOrder(CustomerOrder o) {
		try {
			KeyHolder key = new GeneratedKeyHolder();
			String sql = "INSERT INTO " + tbl_name + " (cust_id,prod_id,name,category,price) VALUES(?,?,?,?,?);";
			template.update(conn -> {
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, o.getCustomerId().toString());
				ps.setString(2, o.getProductId().toString());
				ps.setString(3, o.getName());
				ps.setString(4, o.getCategory());
				ps.setLong(5, o.getPrice());
				return ps;
			}, key);
			return key.getKey().longValue();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public long removeOrder(Long orderId) {
		try {
			String sql = "DELETE FROM " + tbl_name + " WHERE id = " + orderId;
			template.execute(sql);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	public List<CustomerOrder> findOrdersByCustomerId(UUID id) {
		try {
			String sql = "SELECT * FROM " + tbl_name + " AS o WHERE o.cust_id = '" + id.toString() + "'";
			return template.query(sql, new CustomerOrderMapper());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public CustomerOrder findByOrderId(Long id) {
		try {
			String sql = "SELECT * FROM " + tbl_name + " AS o WHERE o.id = " + id;
			return template.queryForObject(sql, new CustomerOrderMapper());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public long deleteOrders(UUID uuid) {
		try {
			String count = "SELECT COUNT(*) AS 'rows' FROM " + tbl_name + " AS o WHERE o.cust_id = '" + uuid.toString()
					+ "'";
			Long rows = template.queryForObject(count, Long.class);
			String delete = "DELETE FROM " + tbl_name + " WHERE cust_id = '" + uuid.toString() + "'";
			template.execute(delete);
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
