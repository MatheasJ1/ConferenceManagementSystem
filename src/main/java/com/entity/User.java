package com.entity;

public class User {
	private int userId;
	private String fullName;
	private String email;
	private int age;
	private Role role;
	public User(int userId, String fullName, String email, int age) {
		this(userId,fullName,email,age,Role.PARTICIPANT);
	}
	
public User(int userId, String fullName, String email, int age, Role role) {
	this.userId = userId;
		setFullName(fullName);
		setEmail(email);
		setAge(age);
		if(role==null) {
			throw new IllegalArgumentException("Invalid role.");
		}
		this.role = role;
		
	}
public int getUserId() {
	return userId;
}
public String getFullName() {
	return fullName;
}
public void setFullName(String fullName) {
	if(fullName == null || fullName.trim().isEmpty()) {
		throw new IllegalArgumentException("Name cannot be empty.");
	}
	this.fullName = fullName;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	if(email == null || email.trim().isEmpty()) {
		throw new IllegalArgumentException("Email cannot be empty.");
	}
	this.email = email;
}
public int getAge() {
	
	return age;
}
public void setAge(int age) {
	if(age<0) {
		throw new IllegalArgumentException("Age cannot be negative.");
	}
	this.age = age;
}
public Role getRole() {
	return (Role) role;
}
public void setRole(Role role) {
	if(role== null) {
		throw new IllegalArgumentException("Role cannot be null");
	}
	this.role = role;
}

@Override
public String toString() {
	return "User ID : " + userId
			+ "\nFull Name: " + fullName 
			+ "\nEmail: " + email
			+ "\nAge: " + age
			+ "\n Role: " + role;
}
}



