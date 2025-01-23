package com.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "dao_user")
@Setter
@Getter
@ToString
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(length = 30, unique = true, nullable = false)
	private String username;
	
	@Column(length = 150, nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private RoleEnum role;
}
