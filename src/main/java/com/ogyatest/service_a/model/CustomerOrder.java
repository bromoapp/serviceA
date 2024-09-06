package com.ogyatest.service_a.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class CustomerOrder {

	private Long id;
	private UUID customerId;
	private UUID productId;
	private String category;
	private String name;
	private Long price;

}
