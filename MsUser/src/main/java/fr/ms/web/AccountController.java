package fr.ms.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import fr.ms.dao.UserTrackTriDao;
import fr.ms.dtos.EmailForm;
import fr.ms.dtos.PasswordForm;
import fr.ms.dtos.RegisterForm;
import fr.ms.entities.UserTrackTri;
import fr.ms.services.AccountService;



@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class AccountController {
	@Autowired 
	private AccountService accountService;
	@Autowired
	private UserTrackTriDao userDao;
	@Autowired
    public JavaMailSender emailSender;
	
	@PostMapping(value="/register",consumes = MediaType.APPLICATION_JSON_VALUE)
	public String register(@RequestBody RegisterForm registerForm) {
		accountService.register(registerForm);
		return "Enregistrement réussi !!";
	}
	
	@PostMapping("/setPasswordDemande")
	public String setPasswordDemande(@RequestBody EmailForm emailForm) {
		accountService.setPasswordDemande(emailForm);
        return "Email Sent!";
	}
	@PostMapping("/setPassword")
	public String setPassword( @RequestParam(required = true) String tokken,@RequestBody PasswordForm passwordForm) {
		accountService.setPassword(tokken, passwordForm);
        return "Password modified!";
	}
	
	@GetMapping("/activateAccount")
	public String activateAccount(@RequestParam(required = true) String tokken) {
		accountService.activateAccount(tokken);
		return "Compte Activée";
	}
	
	@GetMapping("/desativateAccount")
	public String desactivateAccount(@RequestParam(required = true) String tokken) {
		accountService.desactivateAccount(tokken);
		return "Compte Désactivée";
	}
	
	@GetMapping("/Users")
	public List<UserTrackTri> getALL(){
		return userDao.findAll();
	}


}
