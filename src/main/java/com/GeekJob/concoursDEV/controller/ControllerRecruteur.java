package com.GeekJob.concoursDEV.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.GeekJob.concoursDEV.entity.Recruteur;
import com.GeekJob.concoursDEV.entity.Statut;
import com.GeekJob.concoursDEV.entity.Utilisateur;
import com.GeekJob.concoursDEV.service.RecruteurService;
import com.GeekJob.concoursDEV.service.StatutService;
import com.GeekJob.concoursDEV.service.UtilisateurService;

@Controller
public class ControllerRecruteur {

	@Autowired
	private RecruteurService serviceRcu;
	
	@Autowired
	private StatutService serviceStatut;
	
	@Autowired
	private UtilisateurService serviceUtil;
	
	@RequestMapping("/nouveauRcu")
	public String newRcu(Model model) {
		Recruteur recruteur = new Recruteur();
		model.addAttribute("recruteur", recruteur);
		List<Statut> stuRcu = serviceStatut.findStatutListe("Recruteur");
		model.addAttribute("stuRcu", stuRcu);
		return "NouveauRecruteur";
	}

	@RequestMapping("/rcuListe")
	public String viewListeRcuByID(Model model) {
		List<Recruteur> listRcu = serviceRcu.listByID();
		model.addAttribute("listRcu", listRcu);
		return "RecruteursListBack";
	}

	@RequestMapping("/rcuListeByEmail")
	public String viewListeRcuByEmail(Model model) {
		List<Recruteur> listRcu = serviceRcu.listByEmail();
		model.addAttribute("listRcu", listRcu);
		return "RecruteursListBack";
	}

	@RequestMapping("/rcuListeByStatut")
	public String viewListeRcuByStatut(Model model) {
		List<Recruteur> listRcu = serviceRcu.listByStatut();
		model.addAttribute("listRcu", listRcu);
		return "RecruteursListBack";
	}

	@RequestMapping("/editRcu/{id}")
	public ModelAndView ModifieRecruteur(@PathVariable(name = "id") int id) {
		ModelAndView mav = new ModelAndView("ModifieRecruteur");
		Recruteur recruteurDemande = serviceRcu.get(id);
		mav.addObject("recruteurDemande", recruteurDemande);
		List<Statut> stuRcu = serviceStatut.findStatutListe("Recruteur");
		mav.addObject("stuRcu", stuRcu);
		return mav;
	}

	@RequestMapping(value = "/saveRcu", method = RequestMethod.POST)
	public String saveRcu(@ModelAttribute("recruteur") Recruteur recruteur, HttpSession session) {
		System.out.println(((Utilisateur) session.getAttribute("RcuLogin")).getEmail());
		// Check existing user
		if (serviceUtil.findByEmailIgnoreCase(recruteur.getUtilRcu().getEmail()) == null) {
			recruteur.getUtilRcu().setStatut_util(recruteur.getStatutrcu().getStatut_ID());
			Utilisateur u = serviceUtil.save(recruteur.getUtilRcu());
			recruteur.setUtilisateurId(u.getUtilisateurId());
			serviceRcu.save(recruteur);
		} else {
			if (((Utilisateur) session.getAttribute("RcuLogin")).getEmail().equalsIgnoreCase("admin@GeekJob.com")) {
				System.out.println(
						"Admin staus modif" + serviceUtil.findByEmailIgnoreCase(recruteur.getUtilRcu().getEmail()));
				serviceUtil.findByEmailIgnoreCase(recruteur.getUtilRcu().getEmail())
						.setStatut_util(recruteur.getStatutrcu().getStatut_ID());
				serviceRcu.findByRcuID(recruteur.getRcuID()).setStatutrcu(recruteur.getStatutrcu());
				serviceUtil.findByEmailIgnoreCase(recruteur.getUtilRcu().getEmail())
						.setMotdepasse(recruteur.getUtilRcu().getMotdepasse());
				System.out
						.println("Admin modif" + serviceUtil.findByEmailIgnoreCase(recruteur.getUtilRcu().getEmail()));
				return "redirect:/rcuListe";
			} else {
				System.out.println("Common recruteur" + ((Utilisateur) session.getAttribute("RcuLogin")).getEmail());
				serviceUtil.findByEmailIgnoreCase(recruteur.getUtilRcu().getEmail())
						.setMotdepasse(recruteur.getUtilRcu().getMotdepasse());
				System.out.println("Common recruteur nouveau user"
						+ serviceUtil.findByEmailIgnoreCase(recruteur.getUtilRcu().getEmail()));
			}
		}
		return "redirect:/";
	}

	@RequestMapping("/deleteRcu/{id}")
	public String deleterecruteur(@PathVariable(name = "id") int id) {
		serviceRcu.delete(id);
		return "redirect:/rcuListe";
	}

	@RequestMapping("/getRcuId/{id}")
	public int returnRcuId(@PathVariable(name = "id") int id) {
		Recruteur rcu = serviceRcu.finfByUtilID(id);
		System.out.println(rcu.getRcuID());
		return rcu.getRcuID();
	}

}
