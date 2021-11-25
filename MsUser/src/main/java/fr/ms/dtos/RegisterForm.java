package fr.ms.dtos;

import fr.ms.enums.GarbageCanType;
import fr.ms.enums.ProfileType;
import lombok.Data;

@Data 
public class RegisterForm {
	private String name;
	private String surName;
	private String psuedo;
	private String email;
	private String adress;
	private String postalCode;
	private String city;
	private String country;
	private String password;
	private String rePassword;
	private ProfileType profileType;
	private GarbageCanType garbageCanType;
}
