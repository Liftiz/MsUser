package fr.ms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.ms.entities.InactifUser;

public interface InactifUserDao extends JpaRepository<InactifUser, Long> {
	 /**
	   * Cette methode retourne l'objet AppUser par son email
	   * @param username Le nom de l'utilisateur
	   * @return AppUser Retourne l'objet UserTrackTri s'il existe dans la base de donnee
	   */
	public InactifUser findByEmail(String email);

}
