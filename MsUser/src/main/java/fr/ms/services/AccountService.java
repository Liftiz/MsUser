package fr.ms.services;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.ms.dao.InactifUserDao;
import fr.ms.dao.UserTrackTriDao;
import fr.ms.dtos.EmailForm;
import fr.ms.dtos.PasswordForm;
import fr.ms.dtos.RegisterForm;
import fr.ms.entities.InactifUser;
import fr.ms.entities.UserTrackTri;
import fr.ms.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Transactional
public class AccountService {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserTrackTriDao userDao;
	
	@Autowired
	private InactifUserDao inactifUserDao;
	
	@Autowired
    private JavaMailSender emailSender;
	
	@Autowired
	private ModelMapper modelMapper; 
	
	public InactifUser saveInactifUser(InactifUser inactifUser) {
		String hashPassword = bCryptPasswordEncoder.encode(inactifUser.getPassword());
		inactifUser.setPassword(hashPassword);
		return inactifUserDao.save(inactifUser);
	}
	
	public UserTrackTri saveUser(UserTrackTri user) {
		String hashPassword = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(hashPassword);
		return userDao.save(user);
	}
	
	
	public void activateAccount(String tokken) {
		Claims claims = Jwts.parser()
				.setSigningKey(SecurityConstants.SECRET)
				.parseClaimsJws(tokken)
				.getBody();
		InactifUser inactifUser = inactifUserDao.findByEmail(claims.getSubject());
		if(inactifUser == null)
			throw new RuntimeException("Acount Not Fount with email : "+ claims.getSubject());
		UserTrackTri newUser = modelMapper.map(inactifUser, UserTrackTri.class);
		newUser.setId(null);
		userDao.save(newUser);	
		inactifUserDao.delete(inactifUser);
	}
	
	public void setPassword(String tokken,PasswordForm passwordForm) {
		Claims claims = Jwts.parser()
				.setSigningKey(SecurityConstants.SECRET)
				.parseClaimsJws(tokken)
				.getBody();
		UserTrackTri user = userDao.findByEmail(claims.getSubject());
		if(user == null)
			throw new RuntimeException("Acount Not Fount with email : "+ claims.getSubject());
		if(!passwordForm.getPassword().equals(passwordForm.getRePassword()))
			throw new RuntimeException("Please confirm password");
		user.setPassword(passwordForm.getPassword());
		saveUser(user);
	}
	
	public void setPasswordDemande(EmailForm emailForm) {
		UserTrackTri user = userDao.findByEmail(emailForm.getEmail());
		if(user == null)
			throw new RuntimeException("Acount Not Fount with email : "+ emailForm.getEmail());
		String jwt = Jwts.builder()
				.setSubject(user.getEmail())
				.setExpiration(new Date(System.currentTimeMillis()+SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
				.claim("roles", user.getProfileType().toString())
				.compact();
        // Create a Simple MailMessage.
        SimpleMailMessage message = new SimpleMailMessage();
        
        message.setTo(user.getEmail());
        message.setSubject("Tokken for reseting password");
        String msg = "Hi "+user.getName() +"," +System.lineSeparator()+"There was a request to change your password!"+System.lineSeparator()
        				+"If you did not make this request then please ignore this email."
        				+System.lineSeparator()+"Otherwise, please click this link to change your password:"+System.lineSeparator()+
        				"http://localhost:6060/setPassword?tokken="+jwt + System.lineSeparator() + "Goodbye !"+System.lineSeparator()+
        				"Sincerely, the TrackTri team ";
        message.setText(msg);

        // Send Message!
        emailSender.send(message);
	}
	
	public void register(RegisterForm registerForm) {
		if(!registerForm.getPassword().equals(registerForm.getRePassword()))
			throw new RuntimeException("Please confirm password");
		UserTrackTri user = userDao.findByEmail(registerForm.getEmail());
		InactifUser inactiUser = inactifUserDao.findByEmail(registerForm.getEmail());
		if(user != null | inactiUser !=null)
			throw new RuntimeException("Email already used");
		InactifUser newUser = modelMapper.map(registerForm, InactifUser.class);
		saveInactifUser(newUser);
		//Verification de mail
		String jwt = Jwts.builder()
				.setSubject(newUser.getEmail())
				.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
				.compact();
        // Create a Simple MailMessage.
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(newUser.getEmail());
        message.setSubject("TrackTri email confirmation");
        String msg = "Hello "+newUser.getName() +"," +System.lineSeparator()+System.lineSeparator()+"Thank you for registering on TrackTri App!"+System.lineSeparator()+System.lineSeparator()
		+"We need some additional information to complete your registration, including confirmation of your email address."
		+System.lineSeparator()+System.lineSeparator()+"Click below to confirm your email address:"+System.lineSeparator()+System.lineSeparator()+
		"http://localhost:6060/activateAccount?tokken="+jwt + System.lineSeparator()+System.lineSeparator()+ "Goodbye !"+System.lineSeparator()+
		"Sincerely, the TrackTri team ";
        message.setText(msg);
        // Send Message!
        emailSender.send(message);
	}

	public void desactivateAccount(String tokken) {
		Claims claims = Jwts.parser()
				.setSigningKey(SecurityConstants.SECRET)
				.parseClaimsJws(tokken)
				.getBody();
		UserTrackTri user = userDao.findByEmail(claims.getSubject());
		if(user == null)
			throw new RuntimeException("Acount Not Fount with email : "+ claims.getSubject());
		InactifUser inactifUser = modelMapper.map(user, InactifUser.class);
		inactifUser.setId(null);;
		inactifUserDao.save(inactifUser);	
		userDao.delete(user);
	}
	
}
