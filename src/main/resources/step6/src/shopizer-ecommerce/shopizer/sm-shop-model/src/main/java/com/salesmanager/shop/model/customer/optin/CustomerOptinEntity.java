package com.salesmanager.shop.model.customer.optin; // (rank 763) copied from https://github.com/shopizer-ecommerce/shopizer/blob/806363bcf83a22f3d97f534cc3c7a502f9192366/sm-shop-model/src/main/java/com/salesmanager/shop/model/customer/optin/CustomerOptinEntity.java

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;


public class CustomerOptinEntity extends CustomerOptin {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String firstName;
	private String lastName;
	@NotNull
	@Email
	private String email;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
