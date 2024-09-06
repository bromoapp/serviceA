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
public class CustomerOrderDto {

	private Long orderId;
	private UUID productId;
	private String name;
	private String category;
	private Long price;

}
