package fr.ms.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import fr.ms.enums.GarbageCanType;
import fr.ms.enums.ProfileType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class InactifUser {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String surName;
	private String psuedo;
	private String email;
	private String adress;
	private String postalCode;
	private String city;
	private String country;
	private String password;
	private ProfileType profileType;
	private GarbageCanType garbageCanType;

}
