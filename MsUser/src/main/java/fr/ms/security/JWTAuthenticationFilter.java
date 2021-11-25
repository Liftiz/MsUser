package fr.ms.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.ms.entities.UserTrackTri;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * <b>Classe representant le Filtre des authentifications.</b>
 * <p>Cette classe herite UsernamePasswordAuthenticationFilter qui traite une soumission d'authentification.  </p>
 * <p>
 * Cette classe a pour attribut :
 * <ul>
 * <li>Un authenticationManager qui permet d'authetifier l'utilisateur avec ses roles. </li>
 * </ul>
 * </p> 
 * @see UsernamePasswordAuthenticationFilter
 * @see AuthenticationManager
 * @author Salah
 * @version 1.0
 */
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	 /**
     * authenticationManager permet authentifier l'objet d'authentification transmis, 
     * en renvoyant un objet d'authentification comprenant egalement (les autorites accordees) en cas de succès.
     * 
     * @see AuthenticationManager
     */
	private AuthenticationManager authenticationManager;
	
	 /**
     * Constructeur JWTAuthenticationFilter.
     * @param authenticationManager
     *           Manager de l'authentification
     * 
     * @see AuthenticationManager
     * @see UsernamePasswordAuthenticationFilter
     */
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		super();
		this.authenticationManager = authenticationManager;
		}
	
	 /**
	   * Cette methode effectue l'authentification initiale 
	   * @param request La requête http
	   * @param response La reponse http
	   * @param response La reponse http
	   * @see HttpServletRequest
	   * @see HttpServletResponse
	   * @exception AuthenticationException Erreur relative a l'authentification 
	   * 
	   */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("******attemptAuthentication");
		UserTrackTri userTrackTri = null;
		//V2 TO DO ADD BEAN
		try {
			userTrackTri = new ObjectMapper().readValue(request.getInputStream(), UserTrackTri.class);
			System.out.println(userTrackTri.toString());
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		//System.out.println("**************");
		//System.out.println("UserNAme = "+ appUser.getUsername());
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userTrackTri.getEmail(), userTrackTri.getPassword()));
	}
	 /**
	   * Cette methode represente le cas de succèes de l'authentification, cree le token et l'insère a l'entête de a reponse http 
	   * @param request La requête http
	   * @param response La reponse http
	   * @param response La reponse http
	   * @param filterChain la chaîne d'invocation d'une requête filtree pour une ressource.
	   * @param authResult le resultat de l'operation d'authentification qui encapsule les informations relatives a l'utilisateur User de Spring Security 
	   * @see HttpServletRequest
	   * @see HttpServletResponse
	   * @see FilterChain
	   * @see User
	   * @exception ServletException,IOException Erreur relative a l'authentification
	   * 
	   */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("***********successfulauth");
		User springUser = (User) authResult.getPrincipal();
		String jwt = Jwts.builder()
				.setSubject(springUser.getUsername())
				.setExpiration(new Date(System.currentTimeMillis()+SecurityConstants.EXPIRATION_TIME2))
				.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
				.claim("roles", springUser.getAuthorities())
				.compact();
		//System.out.println(jwt);
		//response.addHeader(SecurityConstants.HEADER_STRING,jwt);
		response.addHeader(SecurityConstants.HEADER_STRING, jwt);
		//System.out.println(response.getHeader(SecurityConstants.HEADER_STRING));
		//System.out.println("success+       "+response.toString());
		response.setStatus(HttpServletResponse.SC_OK);
		System.out.println(response.containsHeader("Authorization"));
		System.out.println(response.getStatus() + response.getHeader("Authorization"));
		//super.successfulAuthentication(request, response, chain, authResult);
		//chain.doFilter(request, response);
	}

}
