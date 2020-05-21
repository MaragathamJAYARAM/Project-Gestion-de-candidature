package com.GeekJob.concoursDEV.controller;

import java.io.IOException;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;
import com.GeekJob.concoursDEV.entity.Candidat;
import com.GeekJob.concoursDEV.entity.Candidature;
import com.GeekJob.concoursDEV.entity.Utilisateur;
import com.GeekJob.concoursDEV.entity.concours;

import com.GeekJob.concoursDEV.service.CandidatService;
import com.GeekJob.concoursDEV.service.CandidatureService;
import com.GeekJob.concoursDEV.service.ConcoursService;
import com.GeekJob.concoursDEV.service.FilesStorageService;
import com.GeekJob.concoursDEV.service.VilleService;

import com.GeekJob.concoursDEV.service.RecruteurService;
import com.GeekJob.concoursDEV.service.StatutService;
import com.GeekJob.concoursDEV.service.UtilisateurService;

@Controller
public class ControllerConcours {

	@Autowired
	private ConcoursService service;

	@Autowired
	private UtilisateurService serviceUtil;

	@Autowired
	private RecruteurService serviceRcu;


///////////////////Maragatham////////Concours methods/////////
	@RequestMapping("/concoursListe")
	public String viewListeConcours(Model model) {
		List<concours> listConcours = service.listAll();
		model.addAttribute("listConcours", listConcours);
		return "ConcoursListBack";
	}

	@RequestMapping("/sortBydate")
	public String sortBydate(Model model) {
		List<concours> listConcours = service.sortBydate();
		model.addAttribute("listConcours", listConcours);
		return "ConcoursListBack";
	}

	@RequestMapping("/sortByStatut")
	public String sortByStatut(Model model) {
		List<concours> listConcours = service.sortByStatut();
		model.addAttribute("listConcours", listConcours);
		return "ConcoursListBack";
	}

	@RequestMapping(value = "/rechercheccs")
	public String rechercheccs(@RequestParam("nom") String nom, Model model, HttpSession session) {
		List<concours> searchResult = service.findByNom(nom);
		model.addAttribute("listConcours", searchResult);
		if (session.getAttribute("CdaLogin") != null) {
			return "ConcoursListFront";
		} else if (session.getAttribute("RcuLogin") != null) {
			return "ConcoursListBack";
		}
		return "ConcoursListFront";
	}

	@RequestMapping("/concoursListeActive")
	public String viewListeConcourActive(Model model) {
		List<concours> listConcours = service.listAllCda();
		model.addAttribute("listConcours", listConcours);
		return "ConcoursListBack";
	}

	@RequestMapping("/concoursListecadidat")
	public String viewListeConcourfront(Model model) {
		List<concours> listConcours = service.listAll();
		model.addAttribute("listConcours", listConcours);
		return "ConcoursListFront";
	}

	@RequestMapping("/concoursListeCdaSortByNom")
	public String viewListeConcourfrontSortByNom(Model model) {
		List<concours> listConcours = service.listAllCdaNom();
		model.addAttribute("listConcours", listConcours);
		return "ConcoursListFront";
	}

	@RequestMapping("/details/{id}")
	public ModelAndView detailsConcours(@PathVariable(name = "id") int id) {
		ModelAndView mav = new ModelAndView("detailsConcours");
		concours concoursDemande = service.get(id);
		mav.addObject("concoursDemande", concoursDemande);
		return mav;
	}

	@RequestMapping(value = "/image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> getImage(@PathVariable(name = "id") int id) throws IOException {
		concours cImageDemande = service.get(id);
		byte[] imageContent = null;
		try {
			Blob b = cImageDemande.getImage_css();
			imageContent = b.getBytes(1, (int) b.length());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
	}

	@RequestMapping("/edit/{id}")
	public ModelAndView ModifieConcours(@PathVariable(name = "id") int id) {
		ModelAndView mav = new ModelAndView("ModifieConcours");
		concours concoursDemande = service.get(id);
		mav.addObject("concoursDemande", concoursDemande);
		return mav;
	}

	@RequestMapping("/new")
	public String NewconcoursPage(Model model) {
		concours concours = new concours();
		model.addAttribute("concours", concours);
		return "NouveauConcours";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveconcours(@ModelAttribute("concours") concours concours,
			@RequestParam("imgfile") MultipartFile file, RedirectAttributes redirectAttributes) {

		if (!file.isEmpty()) {
			try {
				byte[] imageInByte = file.getBytes();
				concours.setImage_css(new javax.sql.rowset.serial.SerialBlob(imageInByte));
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}
		} else if (service.get(concours.getCcs_ID()) != null) {
			concours.setImage_css(service.get(concours.getCcs_ID()).getImage_css());
		}
		service.save(concours);
		return "redirect:/concoursListe";
	}

	@RequestMapping("/delete/{id}")
	public String deleteconcours(@PathVariable(name = "id") int id) {
		service.delete(id);
		return "redirect:/concoursListe";
	}

	@RequestMapping("/deleteperm/{id}")
	public String deletePermCcs(@PathVariable(name = "id") int id) {
		service.deletePerm(id);
		return "redirect:/concoursListe";
	}

/////////Concours methods/////////

}
