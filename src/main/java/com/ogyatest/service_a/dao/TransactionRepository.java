package com.ogyatest.service_a.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ogyatest.service_a.model.Transaction;

@Repository
public class TransactionRepository {

	private static final String tbl_name = "transaction";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private JdbcTemplate template;
	private Connection conn;

	public TransactionRepository(DataSource ds) throws Exception {
		this.template = new JdbcTemplate(ds);
		this.conn = ds.getConnection();
	}

	public String addNewTransaction(Transaction o) {
		try {
			UUID uuid = UUID.randomUUID();
			String sql = "INSERT INTO " + tbl_name
					+ " (trx_uuid,cust_id,cust_name,list_item_name,list_category,cust_change,"
					+ "total_cost,cust_old_balance,cust_new_balance,created) VALUES(?,?,?,?,?,?,?,?,?,?);";
			template.update(conn -> {
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, uuid.toString());
				ps.setString(2, o.getCustomerId().toString());
				ps.setString(3, o.getCustomerName());
				ps.setString(4, o.getListItemName());
				ps.setString(5, o.getListCategory());
				ps.setLong(6, o.getCustomerChange());
				ps.setLong(7, o.getTotalCost());
				ps.setLong(8, o.getCustomerOldBalance());
				ps.setLong(9, o.getCustomerNewBalance());
				ps.setDate(10, new Date(o.getCreated().getTime()));
				return ps;
			});
			return uuid.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Transaction> findTransactionWithParams(UUID uid, java.util.Date start, java.util.Date end,
			String category) {
		String sql = "SELECT * FROM " + tbl_name + " AS o WHERE o.cust_id = '" + uid.toString() + "'";
		if (start != null && end != null) {
			sql += " AND o.created BETWEEN '" + sdf.format(start) + "' AND '" + sdf.format(end) + "'";
		}
		if (category != null & category.length() > 0) {
			if (start != null && end != null)
				sql += " AND ";
			sql += "o.list_category LIKE '%" + category + "%'";
		}
		System.out.println(sql);
		return null;
	}

}
