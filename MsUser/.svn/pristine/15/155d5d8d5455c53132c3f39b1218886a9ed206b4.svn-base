package fr.ms;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import fr.ms.entities.UserTrackTri;
import fr.ms.enums.GarbageCanType;
import fr.ms.enums.ProfileType;
import fr.ms.services.AccountService;





@SpringBootApplication

@EnableDiscoveryClient
public class MsUserApplication implements CommandLineRunner {
	
	@Autowired
	private AccountService accountService;
	
	@Bean
	public BCryptPasswordEncoder getBCPE() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(MsUserApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		accountService.saveUser(new UserTrackTri(null, "Micheal", "Platini","Mplat1885", "salaheddine.charko@gmail.com", "35 rue Pastorelli", "06000", "Nice", "France", "TrackTriTest0000", ProfileType.INTERNAUTE, GarbageCanType.INDIVIDUEL));
	}

}
