package com.ogyatest.service_a.model;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
public class Transaction {

	private UUID transactionId;
	private UUID customerId;
	private String customerName;
	private String listItemName;
	private String listCategory;
	private Long customerChange;
	private Long totalCost;
	private Long customerOldBalance;
	private Long customerNewBalance;
	private Date created;

}
