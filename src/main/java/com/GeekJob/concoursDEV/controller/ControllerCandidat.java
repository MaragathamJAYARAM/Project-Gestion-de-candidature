package com.GeekJob.concoursDEV.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.GeekJob.concoursDEV.entity.Candidat;
import com.GeekJob.concoursDEV.entity.Utilisateur;
import com.GeekJob.concoursDEV.service.CandidatService;
import com.GeekJob.concoursDEV.service.CandidatureService;
import com.GeekJob.concoursDEV.service.StatutService;
import com.GeekJob.concoursDEV.service.VilleService;

@Controller
public class ControllerCandidat {
///////////////////////////////////////////////// Maxime/////////////////////////////////////////////////

////////// Maxime////////// Candidat Mangement
	@Autowired
	private VilleService serviceVilles;

	@Autowired
	private CandidatService serviceCda;

	@Autowired
	private CandidatureService serviceCdu;

	
	
	@Value("${upload.path}")
	private String img_path;

	@RequestMapping("/cdaListe")
	public String listeCda(Model model) {
		model.addAttribute("listCda", serviceCda.listAll());
		return "CandidatListBack";
	}

	@RequestMapping("/profil")
	public String vueProfilCandidat(Model model, HttpSession session) {
		String returnPath = "index";

		System.out.println(((Utilisateur) session.getAttribute("CdaLogin")).getUtilisateurId());

		if (null != session.getAttribute("CdaLogin")) {

			Utilisateur u = ((Utilisateur) session.getAttribute("CdaLogin"));

			Candidat monCda = serviceCda.get(u.getUtilisateurId());
			monCda.setMesCdu(serviceCdu.listByCda(monCda.getCda_ID()));
			model.addAttribute("Candidat", monCda);
			model.addAttribute("upload", img_path);
			returnPath = "profil";
		}
		return returnPath;
	}

	@RequestMapping("/nouveauCandidat")
	public String NouveauCandidatPage(HttpSession session) {
		String returnPath = "index";
		if (null != session.getAttribute("CdaLogin")) {
			Utilisateur u = ((Utilisateur) session.getAttribute("CdaLogin"));
			Candidat monCda = new Candidat();
			monCda.setStatut_cda(201);
			monCda.setUtilisateur_id(u.getUtilisateurId());
			serviceCda.save(monCda);
			returnPath = "redirect:/infoCda";
		}
		return returnPath;
	}

	@RequestMapping("/infoCda")
	public String updateCda(Model model, HttpSession session) {
		String returnPath = "index";
		if (null != session.getAttribute("CdaLogin")) {
			Utilisateur u = ((Utilisateur) session.getAttribute("CdaLogin"));
			Candidat monCda = serviceCda.get(u.getUtilisateurId());
			model.addAttribute("Candidat", monCda);
			model.addAttribute("mesVilles", serviceVilles.listAll());
			returnPath = "FicheCandidat";
		}
		return returnPath;
	}

	@RequestMapping(value = "/saveCda", method = RequestMethod.POST)
	public String saveCda(@ModelAttribute("Candidat") Candidat monCda) {
		System.out.println("saveCda id: " + monCda.getCda_ID());
		monCda.getMonAdresse().setVille(serviceVilles.get(monCda.getMonAdresse().getMaVille().getVille_nom_reel()));

		serviceCda.save(monCda);
		return "redirect:/profil";
	}

	@RequestMapping("/deleteAccount/{id}")
	public String deleteCda(@PathVariable(name = "id") int id) {
		Candidat monCda = serviceCda.get(id);
		monCda.setStatut_cda(203);
		serviceCda.save(monCda);
		return "redirect:/cdaListe";
	}

	@RequestMapping("/reactivateAccount/{id}")
	public String reactivateCda(@PathVariable(name = "id") int id) {
		Candidat monCda = serviceCda.get(id);
		monCda.setStatut_cda(201);
		serviceCda.save(monCda);
		return "redirect:/cdaListe";
	}
}
