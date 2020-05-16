package com.GeekJob.concoursDEV.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.GeekJob.concoursDEV.entity.Statut;

@Repository
public interface StatutI extends JpaRepository<Statut, Integer> {
	
	@Query(value = "SELECT statut.nom_statut FROM Statut statut where statut.statut_ID = ?1")
	Statut finfByStatutID(Integer id);

}
