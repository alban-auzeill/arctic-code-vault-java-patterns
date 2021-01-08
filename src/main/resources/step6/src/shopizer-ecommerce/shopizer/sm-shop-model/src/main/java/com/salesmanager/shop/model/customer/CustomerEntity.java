package com.salesmanager.shop.model.customer; // (rank 763) copied from https://github.com/shopizer-ecommerce/shopizer/blob/806363bcf83a22f3d97f534cc3c7a502f9192366/sm-shop-model/src/main/java/com/salesmanager/shop/model/customer/CustomerEntity.java

import java.io.Serializable;

import javax.validation.Valid;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.salesmanager.shop.model.customer.address.Address;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringEscapeUtils;

public class CustomerEntity extends Customer implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(notes = "Customer email address. Required for registration")
	@Email (message="{messages.invalid.email}")
    @NotEmpty(message="{NotEmpty.customer.emailAddress}")
	private String emailAddress;
	@Valid
	@ApiModelProperty(notes = "Customer billing address")
	private Address billing;
	private Address delivery;
	@ApiModelProperty(notes = "Customer gender M | F")
	private String gender;

	@ApiModelProperty(notes = "2 letters language code en | fr | ...")
	private String language;
	private String firstName;
	private String lastName;
	
	private String provider;//online, facebook ...

	
	private String storeCode;
	
	//@ApiModelProperty(notes = "Username (use email address)")
	//@NotEmpty(message="{NotEmpty.customer.userName}")
	//can be email or anything else
	private String userName;
	
	private Double rating = 0D;
	private int ratingCount;
	
	public void setUserName(final String userName) {
		this.userName = StringEscapeUtils.escapeHtml4(userName);
	}

	public String getUserName() {
		return userName;
	}


	public void setStoreCode(final String storeCode) {
		this.storeCode = StringEscapeUtils.escapeHtml4(storeCode);
	}


	public String getStoreCode() {
		return storeCode;
	}


	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = StringEscapeUtils.escapeHtml4(emailAddress);
	}
	

	public String getEmailAddress() {
		return emailAddress;
	}


	public void setLanguage(final String language) {
		this.language = StringEscapeUtils.escapeHtml4(language);
	}
	public String getLanguage() {
		return language;
	}
	

	public Address getBilling() {
		return billing;
	}
	public void setBilling(final Address billing) {
		this.billing = billing;
	}
	public Address getDelivery() {
		return delivery;
	}
	public void setDelivery(final Address delivery) {
		this.delivery = delivery;
	}
	public void setGender(final String gender) {
		this.gender = StringEscapeUtils.escapeHtml4(gender);
	}
	public String getGender() {
		return gender;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = StringEscapeUtils.escapeHtml4(firstName);
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = StringEscapeUtils.escapeHtml4(lastName);
	}


	public int getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(int ratingCount) {
		this.ratingCount = ratingCount;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = StringEscapeUtils.escapeHtml4(provider);
	}



    

}
