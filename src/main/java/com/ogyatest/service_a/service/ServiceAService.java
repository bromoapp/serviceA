package com.ogyatest.service_a.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogyatest.service_a.dao.CustomerOrderRepository;
import com.ogyatest.service_a.dao.CustomerRepository;
import com.ogyatest.service_a.dao.ItemCategoryRepository;
import com.ogyatest.service_a.dao.ItemRepository;
import com.ogyatest.service_a.dao.TransactionRepository;
import com.ogyatest.service_a.dto.CartDto;
import com.ogyatest.service_a.dto.CustomerOrderDto;
import com.ogyatest.service_a.dto.ItemCategoryDto;
import com.ogyatest.service_a.dto.ItemDto;
import com.ogyatest.service_a.dto.SearchArgsDto;
import com.ogyatest.service_a.dto.TransactionDto;
import com.ogyatest.service_a.model.Customer;
import com.ogyatest.service_a.model.CustomerOrder;
import com.ogyatest.service_a.model.Item;
import com.ogyatest.service_a.model.ItemCategory;
import com.ogyatest.service_a.model.Transaction;
import com.ogyatest.service_a.util.RowMapperUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ServiceAService {

	private static Logger log = Logger.getLogger(ServiceAService.class.getName());
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static final String topic = "NEW_TRANS";

	private ItemRepository itemRepository;
	private CustomerRepository customerRepository;
	private CustomerOrderRepository orderRepository;
	private TransactionRepository transactionRepository;
	private ItemCategoryRepository itemCategoryRepository;
	private KafkaTemplate<String, String> kafkaTemplate;

	public Customer getCustomerByUsername(String username) {
		return customerRepository.findCustomerByUsername(username);
	}

	public List<ItemCategoryDto> getItemsByCategories() {
		List<ItemCategoryDto> list1 = null;
		List<ItemCategory> list2 = itemCategoryRepository.findAllCategories();
		if (list2 != null && !list2.isEmpty()) {
			list1 = new ArrayList<>();
			for (ItemCategory o1 : list2) {
				ItemCategoryDto dto1 = RowMapperUtil.fromItemCategory(o1);
				List<Item> list3 = itemRepository.findItemsByCategoryId(o1.getUuid());
				if (list3 != null && !list3.isEmpty()) {
					for (Item o2 : list3) {
						ItemDto dto2 = RowMapperUtil.fromItem(o2);
						if (dto1.getItems() == null)
							dto1.setItems(new ArrayList<>());
						dto1.getItems().add(dto2);
					}
				}
				list1.add(dto1);
			}
		}
		return list1;
	}

	public CartDto addItemToCart(UUID custId, UUID itemId) {
		CartDto dto = null;
		List<CustomerOrderDto> list2 = null;
		Item o1 = itemRepository.findByItemId(itemId);
		Customer o2 = customerRepository.findCustomerById(custId);
		if (o1 != null && o2 != null) {
			ItemCategory o3 = itemCategoryRepository.findCategoryById(o1.getCategoryId());
			CustomerOrder o = CustomerOrder.builder().customerId(custId).productId(itemId).name(o1.getName())
					.category(o3.getCategory()).price(o1.getPrice()).build();
			long id = orderRepository.addNewOrder(o);
			if (id > 0) {
				List<CustomerOrder> list1 = orderRepository.findOrdersByCustomerId(custId);
				if (list1 != null && !list1.isEmpty()) {
					list2 = new ArrayList<>();
					long total = 0;
					for (CustomerOrder o4 : list1) {
						total += o4.getPrice();
						CustomerOrderDto cdto = RowMapperUtil.fromOrder(o4);
						list2.add(cdto);
					}
					dto = CartDto.builder().orders(list2).total(total).build();
				}
			}
		}
		return dto;
	}

	public CartDto removeItemFromCart(UUID custId, Long orderId) {
		CartDto dto = null;
		List<CustomerOrderDto> list2 = null;
		Customer o1 = customerRepository.findCustomerById(custId);
		CustomerOrder o2 = orderRepository.findByOrderId(orderId);
		if (o1 != null && o2 != null) {
			long id = orderRepository.removeOrder(orderId);
			if (id > 0) {
				List<CustomerOrder> list1 = orderRepository.findOrdersByCustomerId(custId);
				if (list1 != null && !list1.isEmpty()) {
					list2 = new ArrayList<>();
					long total = 0;
					for (CustomerOrder o3 : list1) {
						total += o3.getPrice();
						CustomerOrderDto cdto = RowMapperUtil.fromOrder(o3);
						list2.add(cdto);
					}
					dto = CartDto.builder().orders(list2).total(total).build();
				}
			}
		}
		return dto;
	}

	public CartDto listAllItemsInCart(UUID custId) {
		CartDto dto = null;
		List<CustomerOrderDto> list1 = null;
		Customer o1 = customerRepository.findCustomerById(custId);
		if (o1 != null) {
			List<CustomerOrder> list2 = orderRepository.findOrdersByCustomerId(custId);
			if (list2 != null && !list2.isEmpty()) {
				list1 = new ArrayList<>();
				long total = 0;
				for (CustomerOrder o3 : list2) {
					total += o3.getPrice();
					CustomerOrderDto cdto = RowMapperUtil.fromOrder(o3);
					list1.add(cdto);
				}
				dto = CartDto.builder().orders(list1).total(total).build();
			}
		}
		return dto;
	}

	public CartDto payItemsInCart(UUID custId) throws Exception {
		String status = "GAGAL";
		CartDto dto = null;
		List<CustomerOrderDto> list1 = null;
		Customer o1 = customerRepository.findCustomerById(custId);
		if (o1 != null) {
			List<CustomerOrder> list2 = orderRepository.findOrdersByCustomerId(custId);
			if (list2 != null && !list2.isEmpty()) {
				list1 = new ArrayList<>();
				long total = 0;
				StringBuffer categories = new StringBuffer();
				StringBuffer items = new StringBuffer();
				for (CustomerOrder o3 : list2) {
					total += o3.getPrice();
					CustomerOrderDto cdto = RowMapperUtil.fromOrder(o3);
					list1.add(cdto);
					items.append(o3.getName() + "~");
					categories.append(o3.getCategory() + "~");
				}
				dto = CartDto.builder().orders(list1).total(total).build();
				if (total > 0) {
					long newBalance = o1.getBalance() - total;
					if (newBalance > 0) {
						Transaction o = Transaction.builder().customerId(custId).customerName(o1.getName())
								.listItemName(items.toString()).listCategory(categories.toString())
								.customerChange(total).totalCost(total).customerOldBalance(o1.getBalance())
								.customerNewBalance(newBalance).created(new Date()).build();
						String trxId = transactionRepository.addNewTransaction(o);
						if (trxId != null) {
							o.setTransactionId(UUID.fromString(trxId));
							if (customerRepository.updateCustomerBalance(custId, newBalance)) {
								status = "LUNAS";
								orderRepository.deleteOrders(custId);

								ObjectMapper mapper = new ObjectMapper();
								String json = mapper.writeValueAsString(o);
								kafkaTemplate.send(topic, json).thenApplyAsync(result -> {
									RecordMetadata metadata = result.getRecordMetadata();
									int partition = metadata.partition();
									long offset = metadata.offset();
									String recordVal = result.getProducerRecord().value();
									log.info(">>> " + recordVal);
									return null;
								}).exceptionally(err -> {
									err.printStackTrace();
									return null;
								});
							}
						}
					} else {
						status = "DANA KURANG";
					}
				}
			}
		}
		dto.setStatus(status);
		return dto;
	}

	public List<TransactionDto> searchUserTransactions(UUID uid, SearchArgsDto dto) throws Exception {
		List<TransactionDto> list1 = new ArrayList<>();
		List<Transaction> list2 = transactionRepository.findTransactionWithParams(uid, sdf.parse(dto.getStartDate()),
				sdf.parse(dto.getEndDate()), dto.getCategory());
		if (list2 != null) {
			for (Transaction o : list2) {
				TransactionDto dt = RowMapperUtil.fromTransaction(o);
				list1.add(dt);
			}
		}
		return list1;
	}

//	public void sendHelloToKafka() {
//		log.info(">>> SENDING MESSAGE");
//		kafkaTemplate.send("TRANS", "HELLO KAFKA").thenApplyAsync(result -> {
//			RecordMetadata metadata = result.getRecordMetadata();
//			int partition = metadata.partition();
//			long offset = metadata.offset();
//			String recordVal = result.getProducerRecord().value();
//			log.info(">>> " + metadata.topic() + " | " + partition + " | " + offset + " | " + recordVal);
//			return null;
//		}).exceptionally(err -> {
//			err.printStackTrace();
//			return null;
//		});
//	}

}
