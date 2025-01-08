package com.app.christmas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDateTime;

import com.app.christmas.models.Letter;
import com.app.christmas.models.LetterDto;
import com.app.christmas.repositories.LetterRepository;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/letters")
public class LettersController {

	@Autowired
	private LetterRepository letterRepo;
	
	@GetMapping({"", "/"})
	public String getLetters(Model model) {
	    var letters = letterRepo.findAll(Sort.by(Sort.Order.desc("id")));
	    model.addAttribute("letters", letters);
	    return "letters/index";
	}
	
	
	@GetMapping("/create")
	public String createLetter(Model model) {
	    LetterDto letterDto = new LetterDto();
	    model.addAttribute("letterDto", letterDto);
	    
	    return "letters/create";
	}
	
	@PostMapping("/create")
	public String createletter(
			@ModelAttribute @Valid LetterDto letterDto, BindingResult result) {
	    
		if (letterRepo.findByEmail(letterDto.getEmail()) != null) {
	        result.addError(new FieldError("letterDto", "email", letterDto.getEmail(),
	                false, null, null, "Email already used!"));
	    }

		if (result.hasErrors()) {
			return "letters/create";
		}
		
		Letter letter = new Letter();
		letter.setFirstName(letterDto.getFirstName());
		letter.setLastName(letterDto.getLastName());
		letter.setEmail(letterDto.getEmail());
		letter.setLetterText(letterDto.getLetterText());
		letter.setAddress(letterDto.getAddress());
		letter.setCreatedAt(new Date(10));
		
		letterRepo.save(letter);
		
	    return "redirect:/letters";
	}
	
	@GetMapping("/edit")
	public String editLetter(Model model, @RequestParam int id) {
		Letter letter = letterRepo.findById(id).orElse(null);
		if(letter == null) {
			return "reddirect::/letters";
		}
		
		LetterDto letterDto = new LetterDto();
		letterDto.setFirstName(letter.getFirstName());
		letterDto.setLastName(letter.getLastName());
		letterDto.setEmail(letter.getEmail());
		letterDto.setLetterText(letter.getLetterText());
		letterDto.setAddress(letter.getAddress());
		
		model.addAttribute("letter", letter);
		model.addAttribute("letterDto", letterDto);
		
		return "letters/edit";
	}
	
	@PostMapping("/edit")
	public String editLetter(
			Model model,
			@RequestParam int id,
			@Valid @ModelAttribute LetterDto letterDto,
			BindingResult result
			) {
		
		Letter letter = letterRepo.findById(id).orElse(null);
		if(letter == null) {
			return "redirect:/letters";
		}
		model.addAttribute("letter", letter);
		
		if(result.hasErrors()) {
			return "letters/edit";
		}
		
		letter.setFirstName(letterDto.getFirstName());
		letter.setLastName(letterDto.getLastName());
		letter.setEmail(letterDto.getEmail());
		letter.setLetterText(letterDto.getLetterText());
		letter.setAddress(letterDto.getAddress());
		
		try {
			letterRepo.save(letter);
		} catch (Exception ex) {
			result.addError(new FieldError("letterDto", "email", letterDto.getEmail(), false, null, null, "Email already used"));
			return "letters/edit";
		}
		
		
		return "redirect:/letters";
	}
	
	@GetMapping("/delete")
	public String deleteLetter(@RequestParam int id) {
		Letter letter = letterRepo.findById(id).orElse(null);
		
		if(letter != null) {
			letterRepo.delete(letter);
		}
		
		return "redirect:/letters";
	}
	
}
