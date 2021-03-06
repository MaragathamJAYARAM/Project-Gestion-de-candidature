package com.GeekJob.concoursDEV.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.GeekJob.concoursDEV.entity.Candidature;


public interface CandidatureI extends JpaRepository<Candidature, Integer> {

	@Query(value = "SELECT * FROM candidature c WHERE c.cda = ?1 AND NOT statut_cdu IN (103, 106, 108, 111, 113) ORDER BY statut_cdu ", nativeQuery = true)
	List<Candidature> findByCda(int id);

	@Query(value = "SELECT * FROM candidature c WHERE c.cda = ?1 AND statut_cdu IN (106, 108, 111, 113) ORDER BY statut_cdu ", nativeQuery = true)
	List<Candidature> findArchiveByCda(int id);

}
