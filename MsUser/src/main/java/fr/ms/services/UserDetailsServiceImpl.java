package fr.ms.services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.stereotype.Service;

import fr.ms.dao.UserTrackTriDao;
import fr.ms.entities.UserTrackTri;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserTrackTriDao userTrackTriDao;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserTrackTri user = userTrackTriDao.findByEmail(email);
		if(user==null) throw new UsernameNotFoundException("User Name Not Found");
		//creation de la liste des autorites (le role ici)
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		//Ajoute le type de profile qui va nous servir pour la precision du role
		authorities.add(new SimpleGrantedAuthority(user.getProfileType().toString()));
		User userspring = new User(user.getEmail(),user.getPassword(),authorities);
		System.out.println(userspring.toString());
		return userspring;
	}

}
