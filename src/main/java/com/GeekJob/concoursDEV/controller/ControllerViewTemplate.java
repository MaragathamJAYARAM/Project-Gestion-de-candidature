package com.GeekJob.concoursDEV.controller;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ControllerViewTemplate {

	@RequestMapping(value = "/logo", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> getImage() throws IOException {
		ClassPathResource imgFile = new ClassPathResource("static/Logo.png");
		byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String viewHomePage(Model model) {
		return "index";
	}
}
