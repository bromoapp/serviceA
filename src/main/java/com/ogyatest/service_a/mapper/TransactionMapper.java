package com.ogyatest.service_a.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import com.ogyatest.service_a.model.Transaction;

public class TransactionMapper implements RowMapper<Transaction> {

	@Override
	public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
		Transaction o = Transaction.builder().transactionId(UUID.fromString(rs.getString("trx_uuid")))
				.customerId(UUID.fromString(rs.getString("cust_id"))).customerName(rs.getString("cust_name"))
				.listItemName("list_item_name").listCategory(rs.getString("list_category"))
				.customerChange(rs.getLong("cust_change")).totalCost(rs.getLong("total_cost"))
				.customerOldBalance(rs.getLong("cust_old_balance")).customerNewBalance(rs.getLong("cust_new_balance"))
				.created(rs.getDate("created")).build();
		return o;
	}

}
