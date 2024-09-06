package com.ogyatest.service_a.util;

import java.text.SimpleDateFormat;

import com.ogyatest.service_a.dto.CustomerOrderDto;
import com.ogyatest.service_a.dto.ItemCategoryDto;
import com.ogyatest.service_a.dto.ItemDto;
import com.ogyatest.service_a.dto.TransactionDto;
import com.ogyatest.service_a.model.CustomerOrder;
import com.ogyatest.service_a.model.Item;
import com.ogyatest.service_a.model.ItemCategory;
import com.ogyatest.service_a.model.Transaction;

public class RowMapperUtil {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static ItemCategoryDto fromItemCategory(ItemCategory o) {
		ItemCategoryDto dto = ItemCategoryDto.builder().uuid(o.getUuid()).category(o.getCategory()).build();
		return dto;
	}

	public static ItemDto fromItem(Item o) {
		ItemDto dto = ItemDto.builder().uuid(o.getUuid()).categoryId(o.getCategoryId()).name(o.getName())
				.price(o.getPrice()).build();
		return dto;
	}

	public static CustomerOrderDto fromOrder(CustomerOrder o) {
		CustomerOrderDto dto = CustomerOrderDto.builder().orderId(o.getId()).name(o.getName()).category(o.getCategory())
				.price(o.getPrice()).productId(o.getProductId()).build();
		return dto;
	}

	public static TransactionDto fromTransaction(Transaction o) {
		TransactionDto dto = TransactionDto.builder().transactionId(o.getTransactionId())
				.customerName(o.getCustomerName()).listItemName(o.getListItemName()).listCategory(o.getListCategory())
				.customerChange(o.getCustomerChange()).totalCost(o.getTotalCost())
				.customerOldBalance(o.getCustomerOldBalance()).customerNewBalance(o.getCustomerNewBalance())
				.created(sdf.format((o.getCreated()))).build();
		return dto;
	}

}
