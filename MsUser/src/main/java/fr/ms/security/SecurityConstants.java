package fr.ms.security;



/**
 * 
 * <b>Interface representant les constantes relatives a la securisation des authentifications et authorisations.</b>
 * 
 * @author Salah
 * @version 1.0
 */
public interface SecurityConstants {
	 /**
     * Le secret utiliser pour signer les jetons.
     * 
     */
	public static final String SECRET = "AplkS@/lm/mP;??&)[{152&fsc#zrzd.zadef";
	 /**
     * La duree de validite des jetons.
     * 
     */
	public static final long EXPIRATION_TIME = 864_000_000; // 10 days
	public static final long EXPIRATION_TIME2 = 143_200_000; // 0.5 day modified
	 /**
     * Le prefixe des jetons pour les authorisations.
     * 
     */
	public static final String TOKEN_PREFIX = "Bearer ";
	 /**
     * L'entete de la requête Http contenant le jeton
     * 
     */
	public static final String HEADER_STRING = "Authorization";

}
