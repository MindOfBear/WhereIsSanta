package com.app.christmas.models;

import jakarta.validation.constraints.*;

public class LetterDto {

	@NotEmpty(message = "First name is required")
	private String firstName;
	
	@NotEmpty(message = "Last name is required")
	private String lastName;
	
	@NotEmpty(message = "Letter message is required")
	private String letterText;
	
	@NotEmpty(message = "Email is required")
	private String email;
	
	private String phone;
	private String address;
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
	public String getLetterText() {
		return letterText;
	}
	public void setLetterText(String LetterText) {
		letterText = LetterText;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
