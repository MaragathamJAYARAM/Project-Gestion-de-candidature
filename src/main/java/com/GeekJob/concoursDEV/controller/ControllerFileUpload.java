package com.GeekJob.concoursDEV.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.GeekJob.concoursDEV.entity.Candidat;
import com.GeekJob.concoursDEV.entity.Utilisateur;
import com.GeekJob.concoursDEV.service.CandidatService;
import com.GeekJob.concoursDEV.service.FilesStorageService;

@Controller
public class ControllerFileUpload {

	////////// Maxime////////// Upload Management ////

	@Autowired
	private CandidatService serviceCda;

	// Save the uploaded file to this folder
	@Value("${upload.path}")
	private String img_path;
	@Value("${upload.folder}")
	private String upload;

	@Autowired
	FilesStorageService storageService;

	@PostMapping("/uploadCv")
	public String uploadFile(@RequestParam("file") MultipartFile file, HttpSession session) {
		String message = "";
		try {
			storageService.save(file);
			Utilisateur u = ((Utilisateur) session.getAttribute("CdaLogin"));
			Candidat monCda = serviceCda.get(u.getUtilisateurId());
			monCda.setCv(file.getOriginalFilename());
			serviceCda.save(monCda);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			// return ResponseEntity.status(HttpStatus.OK).body(new
			// ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			// return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new
			// ResponseMessage(message));
		}
		return "redirect:/profil";
	}
}
