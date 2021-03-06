package com.GeekJob.concoursDEV.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.GeekJob.concoursDEV.entity.Recruteur;

public interface RecruteurI extends JpaRepository<Recruteur, Integer>{

	List<Recruteur> Statutrcu(int i);
	Recruteur findByRcuID(Integer id);
	
	@Query(value = "SELECT rcu FROM Recruteur rcu where rcu.utilRcu.utilisateurId = ?1")
	Recruteur finfByUtilID(Integer id);
	
	@Query(value = "SELECT rcu FROM Recruteur rcu ORDER BY rcuID")
	List<Recruteur> sortByID();
	
	@Query(value = "SELECT rcu FROM Recruteur rcu ORDER BY statutrcu")
	List<Recruteur> sortByStatut();
	
	@Query(value = "SELECT rcu FROM Recruteur rcu ORDER BY utilRcu.email")
	List<Recruteur> sortByEmail();
	
}
