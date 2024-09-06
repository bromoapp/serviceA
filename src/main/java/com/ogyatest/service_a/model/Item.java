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
public class Item {

	private UUID uuid;
	private UUID categoryId;
	private String name;
	private Long price;

	@Override
	public String toString() {
		return "Item [uuid=" + uuid + ", categoryId=" + categoryId + ", name=" + name + ", price=" + price + "]";
	}

}
