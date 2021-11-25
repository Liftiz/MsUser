package fr.ms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.ms.entities.UserTrackTri;

public interface UserTrackTriDao extends JpaRepository<UserTrackTri, Long>{
	 /**
	   * Cette methode retourne l'objet AppUser par son email
	   * @param username Le nom de l'utilisateur
	   * @return AppUser Retourne l'objet UserTrackTri s'il existe dans la base de donnee
	   */
	public UserTrackTri findByEmail(String email);
}
