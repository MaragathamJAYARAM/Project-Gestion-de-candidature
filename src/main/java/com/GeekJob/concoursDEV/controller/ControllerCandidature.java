package com.GeekJob.concoursDEV.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.GeekJob.concoursDEV.entity.Candidat;
import com.GeekJob.concoursDEV.entity.Candidature;
import com.GeekJob.concoursDEV.entity.Utilisateur;
import com.GeekJob.concoursDEV.service.CandidatService;
import com.GeekJob.concoursDEV.service.CandidatureService;
import com.GeekJob.concoursDEV.service.ConcoursService;
import com.GeekJob.concoursDEV.service.StatutService;

@Controller
public class ControllerCandidature {
//////////Maxime////////// Candidature Mangement
	
	@Autowired
	private CandidatureService serviceCdu;
	
	@Autowired
	private StatutService serviceStatut;
	
	@Autowired
	private CandidatService serviceCda;
	
	@Autowired
	private ConcoursService service;

	@RequestMapping("/gestionCandidature")
	public String listeCda(Model model, HttpSession session) {
		String returnPath = "index";
		if (null != session.getAttribute("CdaLogin")) {
			Utilisateur u = ((Utilisateur) session.getAttribute("CdaLogin"));
			Candidat monCda = serviceCda.get(u.getUtilisateurId());
			model.addAttribute("Candidat", monCda);
			model.addAttribute("listCdu", serviceCdu.listByCda(monCda.getCda_ID()));
			returnPath = "CandidaturesList";
		}
		return returnPath;

	}

	@RequestMapping("/archiveCandidature")
	public String listeArchiveCda(Model model, HttpSession session) {
		String returnPath = "index";
		if (null != session.getAttribute("CdaLogin")) {
			Utilisateur u = ((Utilisateur) session.getAttribute("CdaLogin"));
			Candidat monCda = serviceCda.get(u.getUtilisateurId());
			model.addAttribute("Candidat", monCda);
			model.addAttribute("listCdu", serviceCdu.archiveByCda(monCda.getCda_ID()));
			returnPath = "CandidaturesList";
		}
		return returnPath;
	}

	@RequestMapping("/cduBackListe")
	public String listeCdu(Model model, HttpSession session) {
		String returnPath = "index";
		if (null != session.getAttribute("RcuLogin")) {
			model.addAttribute("listCdu", serviceCdu.listAll());
			returnPath = "CandidaturesList";
		}
		return returnPath;
	}

	@RequestMapping("/postuler/{id}")
	public String postulerCdu(@PathVariable(name = "id") int ccs, Model model, HttpSession session) {
		String returnPath = "index";
		if (null != session.getAttribute("CdaLogin")) {
			Utilisateur u = ((Utilisateur) session.getAttribute("CdaLogin"));
			Candidat monCda = serviceCda.get(u.getUtilisateurId());
			Candidature maCdu = new Candidature(monCda, service.get(ccs), serviceStatut.get(101));
			serviceCdu.save(maCdu);
			returnPath = "redirect:/gestionCandidature";
		}
		return returnPath;
	}

	@RequestMapping("/archiverCda/{id}")
	public String archiverParCda(@PathVariable(name = "id") int id, HttpSession session) {
		Candidature maCdu = serviceCdu.get(id);
		int stat = maCdu.getStatut_cdu().getStatut_ID();
		if (stat == 105) {
			maCdu.setStatut_cdu(serviceStatut.get(106));
		}
		if (stat == 107) {
			maCdu.setStatut_cdu(serviceStatut.get(108));
		}
		if (stat == 110) {
			maCdu.setStatut_cdu(serviceStatut.get(111));
		}
		if (stat == 112) {
			maCdu.setStatut_cdu(serviceStatut.get(113));
		}
		serviceCdu.save(maCdu);
		return "redirect:/gestionCandidature";
	}

	@RequestMapping("/archiverRcu/{id}")
	public String archiverParRcu(@PathVariable(name = "id") int id, HttpSession session) {
		Candidature maCdu = serviceCdu.get(id);
		int stat = maCdu.getStatut_cdu().getStatut_ID();
		if (stat == 105) {
			maCdu.setStatut_cdu(serviceStatut.get(107));
		}
		if (stat == 106) {
			maCdu.setStatut_cdu(serviceStatut.get(108));
		}
		if (stat == 110) {
			maCdu.setStatut_cdu(serviceStatut.get(112));
		}
		if (stat == 111) {
			maCdu.setStatut_cdu(serviceStatut.get(113));
		}
		serviceCdu.save(maCdu);
		return "redirect:/cduBackListe";
	}

	@RequestMapping("/updateCdu/{id}")
	public String deleteCdu(@PathVariable(name = "id") int id, HttpSession session) {
		Candidature maCdu = serviceCdu.get(id);
		maCdu.setFichier_CV(maCdu.getCda().getCv());
		maCdu.setStatut_cdu(serviceStatut.get(102));
		serviceCdu.save(maCdu);
		return "redirect:/gestionCandidature";
	}

	@RequestMapping("/deleteCdu/{id}")
	public String updateCdu(@PathVariable(name = "id") int id, HttpSession session) {
		Candidature maCdu = serviceCdu.get(id);
		maCdu.setStatut_cdu(serviceStatut.get(103));
		serviceCdu.save(maCdu);
		return "redirect:/gestionCandidature";
	}

	@RequestMapping("/traiterCdu/{id}")
	public String traiterCdu(@PathVariable(name = "id") int id, HttpSession session) {
		Candidature maCdu = serviceCdu.get(id);
		maCdu.setStatut_cdu(serviceStatut.get(104));
		maCdu.setDate_traitement(new java.util.Date());
		serviceCdu.save(maCdu);
		return "redirect:/cduBackListe";
	}

	@RequestMapping("/refuserCdu/{id}")
	public String refuserCdu(@PathVariable(name = "id") int id, HttpSession session) {
		Candidature maCdu = serviceCdu.get(id);
		maCdu.setDate_traitement(new java.util.Date());

		maCdu.setStatut_cdu(serviceStatut.get(105));
		serviceCdu.save(maCdu);
		return "redirect:/cduBackListe";
	}

	@RequestMapping("/accepterCdu/{id}")
	public String accepterCdu(@PathVariable(name = "id") int id, HttpSession session) {
		Candidature maCdu = serviceCdu.get(id);
		maCdu.setDate_traitement(new java.util.Date());

		maCdu.setStatut_cdu(serviceStatut.get(110));
		serviceCdu.save(maCdu);
		return "redirect:/cduBackListe";
	}
}
