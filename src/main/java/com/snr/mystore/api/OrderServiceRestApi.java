/**
 * 
 */
package com.snr.mystore.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.snr.mystore.bean.MyOrders;
import com.snr.mystore.response.bean.Order;
import com.snr.mystore.response.bean.OrderProducts;
import com.snr.mystore.response.bean.User;
import com.snr.mystore.response.bean.UserOrders;

/**
 * @author I326319
 *
 */
@RestController
@RequestMapping("/myOrders")
@Service
public class OrderServiceRestApi {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@HystrixCommand(fallbackMethod = "getOrderListFallback")
	@RequestMapping("/{userId}")
	public MyOrders getOrdersList(@PathVariable("userId") String userId){
		
		List<ServiceInstance> instances = discoveryClient.getInstances("user-info-service");
		
		for(ServiceInstance instance : instances) {
			System.out.println(instance.getHost() +" -  "+ instance.getPort());
		}
		
		// get userInformation for userId
		
		User userInfo = restTemplate.getForObject("http://user-info-service/user/userDetails/"+userId, User.class);
		
		// get all orderIds under userId
		
		UserOrders orders = restTemplate.getForObject("http://order-info-service/order/myOrders/"+userId, UserOrders.class);
		
		// for each orderId, call product info service to get list of products
		for(Order order : orders.getOrders()) {
			OrderProducts products = restTemplate.getForObject("http://product-info-service/product/myProducts/"+order.getOrderId(), OrderProducts.class);
			order.setProductList(products.getProducts());
		}
		// put them all together
		
		MyOrders myOrders = new MyOrders();
		myOrders.setUserDetails(userInfo);
		myOrders.setOrders(orders);
		
		return myOrders; 
		
	}
	
	public MyOrders getOrderListFallback(@PathVariable("userId")String userId) {
		System.out.println("fallback method called");
		MyOrders myOrders = new MyOrders();
		myOrders.setUserDetails(null);
		myOrders.setOrders(null);
		return myOrders;
		
	}

}
