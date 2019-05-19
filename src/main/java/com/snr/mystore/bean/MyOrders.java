/**
 * 
 */
package com.snr.mystore.bean;

import com.snr.mystore.response.bean.User;
import com.snr.mystore.response.bean.UserOrders;

/**
 * @author I326319
 *
 */
public class MyOrders {
	
	private User userDetails;
	private UserOrders userOrders;
	
	public User getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(User userDetails) {
		this.userDetails = userDetails;
	}
	public UserOrders getOrders() {
		return userOrders;
	}
	public void setOrders(UserOrders userOrders) {
		this.userOrders = userOrders;
	}
	
}
