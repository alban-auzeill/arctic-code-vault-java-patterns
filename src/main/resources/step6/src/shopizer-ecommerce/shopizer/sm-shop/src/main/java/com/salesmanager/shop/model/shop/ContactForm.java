package com.salesmanager.shop.model.shop; // (rank 763) copied from https://github.com/shopizer-ecommerce/shopizer/blob/806363bcf83a22f3d97f534cc3c7a502f9192366/sm-shop/src/main/java/com/salesmanager/shop/model/shop/ContactForm.java

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class ContactForm {
	
	@NotEmpty
	private String name;
	@NotEmpty
	private String subject;
	@Email
	private String email;
	@NotEmpty
	private String comment;

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}


}
