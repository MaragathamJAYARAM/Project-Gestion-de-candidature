package com.GeekJob.concoursDEV.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.GeekJob.concoursDEV.entity.Utilisateur;
import com.GeekJob.concoursDEV.service.RecruteurService;
import com.GeekJob.concoursDEV.service.UtilisateurService;

@Controller
public class ControllerLogin {
	
	@Autowired
	private UtilisateurService serviceUtil;

	@Autowired
	private RecruteurService serviceRcu;


	
	@RequestMapping(value = "/loginCda", method = RequestMethod.POST)
	public String validUser(@RequestParam String email, @RequestParam String motdepasse, HttpSession session,
			Model model) {
		if (session.getAttribute("CdaLogin") != null) {
			session.removeAttribute("CdaLogin");
		}
		if (session.getAttribute("RcuLogin") != null) {
			session.removeAttribute("RcuLogin");
		}
		Utilisateur vUtil = serviceUtil.getValidCda(email, motdepasse);
		if (vUtil != null) {
			session.setAttribute("CdaLogin", vUtil);
			return "redirect:/concoursListecadidat";
		}
		if (vUtil == null) {
			model.addAttribute("msg", "Invalide");
		}
		return "index";
	}

	@RequestMapping(value = "/loginRcu", method = RequestMethod.POST)
	public String validRcu(@RequestParam String email, @RequestParam String motdepasse, HttpSession session,
			Model model) {
		if (session.getAttribute("CdaLogin") != null) {
			session.removeAttribute("CdaLogin");
		}
		if (session.getAttribute("RcuLogin") != null) {
			session.removeAttribute("RcuLogin");
		}
		Utilisateur vUtil = serviceUtil.getValidRcu(email, motdepasse);
		if (vUtil != null) {
			session.setAttribute("RcuLogin", vUtil);
			session.setAttribute("Vrcu", serviceRcu.finfByUtilID(vUtil.getUtilisateurId()));
			return "redirect:/concoursListe";
		}
		model.addAttribute("msg", "Invalide");
		return "index";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		if (session.getAttribute("CdaLogin") != null) {
			session.removeAttribute("CdaLogin");
		}
		if (session.getAttribute("RcuLogin") != null) {
			session.removeAttribute("RcuLogin");
		}
		return "index";
	}
	
	
	///////////Maxime//////////////Inscription de Nouveau candidat/////////////////////////////
	@RequestMapping(value = "/newCda", method = RequestMethod.POST)
	public String createUser(@RequestParam String email, @RequestParam String motdepasse, HttpSession session,
			Model model) {
		if (session.getAttribute("CdaLogin") != null) {
			session.removeAttribute("CdaLogin");
		}
		if (session.getAttribute("RcuLogin") != null) {
			session.removeAttribute("RcuLogin");
		}

		Utilisateur vUtil = serviceUtil.save(new Utilisateur(email, motdepasse));
		if (vUtil != null) {
			session.setAttribute("CdaLogin", vUtil);
			return "redirect:/nouveauCandidat";
		}
		if (vUtil == null) {
			model.addAttribute("msg", "Invalide");
		}
		return "redirect:/profil";
	}

}
