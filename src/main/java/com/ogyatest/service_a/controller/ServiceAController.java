package com.ogyatest.service_a.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.ogyatest.service_a.dto.CartDto;
import com.ogyatest.service_a.dto.CustomerOrderDto;
import com.ogyatest.service_a.dto.ItemCategoryDto;
import com.ogyatest.service_a.dto.LoginReqDto;
import com.ogyatest.service_a.dto.LoginResDto;
import com.ogyatest.service_a.dto.SearchArgsDto;
import com.ogyatest.service_a.dto.TransactionDto;
import com.ogyatest.service_a.model.Customer;
import com.ogyatest.service_a.service.ServiceAService;
import com.ogyatest.service_a.util.JwtUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
public class ServiceAController {

	private ServiceAService serviceA;
	private AuthenticationManager authManager;
	private JwtUtil jwtUtil;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginReqDto dto) {
		try {
			Authentication authentication = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
			String username = authentication.getName();
			Customer user = Customer.builder().username(username).build();
			user = serviceA.getCustomerByUsername(username);

			String token = jwtUtil.createToken(user);
			LoginResDto loginRes = LoginResDto.builder().username(user.getUsername()).token(token).build();

			return ResponseEntity.ok(loginRes);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		}
	}

	@GetMapping("/get_items")
	public ResponseEntity<List<ItemCategoryDto>> getListItemsByAllCategories() {
		List<ItemCategoryDto> list = serviceA.getItemsByCategories();
		if (list != null) {
			return ResponseEntity.status(HttpStatus.FOUND).body(list);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping("/add_item")
	public ResponseEntity<CartDto> addItem(@RequestHeader HttpHeaders headers, @RequestBody CustomerOrderDto dto) {
		Customer o = getCustomer(headers);
		CartDto cart = serviceA.addItemToCart(o.getUuid(), dto.getProductId());
		if (cart != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(cart);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping("/remove_item")
	public ResponseEntity<CartDto> removeItem(@RequestHeader HttpHeaders headers, @RequestBody CustomerOrderDto dto) {
		Customer o = getCustomer(headers);
		CartDto cart = serviceA.removeItemFromCart(o.getUuid(), dto.getOrderId());
		if (cart != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(cart);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping("/cart_items")
	public ResponseEntity<CartDto> cartItems(@RequestHeader HttpHeaders headers) {
		Customer o = getCustomer(headers);
		CartDto cart = serviceA.listAllItemsInCart(o.getUuid());
		if (cart != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(cart);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping("/pay_orders")
	public ResponseEntity<CartDto> payPurchase(@RequestHeader HttpHeaders headers) throws Exception {
		Customer o = getCustomer(headers);
		CartDto cart = serviceA.payItemsInCart(o.getUuid());
		if (cart != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(cart);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping("/search")
	public ResponseEntity<List<TransactionDto>> searchTransaction(@RequestHeader HttpHeaders headers,
			@RequestBody SearchArgsDto dto) {
		Customer o = getCustomer(headers);
		try {
			List<TransactionDto> list = serviceA.searchUserTransactions(o.getUuid(), dto);
			return ResponseEntity.status(HttpStatus.FOUND).body(list);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	private Customer getCustomer(HttpHeaders headers) {
		String username = jwtUtil.getUsername(headers);
		return serviceA.getCustomerByUsername(username);
	}

//	@GetMapping("/hello_kafka")
//	public ResponseEntity<Boolean> sendHelloKafka() {
//		serviceA.sendHelloToKafka();
//		return ResponseEntity.status(HttpStatus.CREATED).body(true);
//	}

}
