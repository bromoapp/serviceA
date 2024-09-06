package com.ogyatest.service_a.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDto {

	private UUID transactionId;
	private String customerName;
	private String listItemName;
	private String listCategory;
	private Long customerChange;
	private Long totalCost;
	private Long customerOldBalance;
	private Long customerNewBalance;
	private String created;

}
