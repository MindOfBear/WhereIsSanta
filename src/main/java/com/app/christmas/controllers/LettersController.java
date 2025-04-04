package com.app.christmas.controllers;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.christmas.models.Letter;
import com.app.christmas.models.LetterDto;
import com.app.christmas.models.User;
import com.app.christmas.repositories.LetterRepository;
import com.app.christmas.repositories.UserRepository;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/letters")
public class LettersController {

	@Autowired
	private LetterRepository letterRepo;

	@Autowired
	private UserRepository userRepo;

	@GetMapping({"", "/"})
	public String getLetters(
	    @RequestParam(required = false) Integer year,
	    @RequestParam(required = false) String email,
	    Model model,
	    HttpSession session
	) {
	    User adminUser = (User) session.getAttribute("adminUser");

	    List<Letter> letters;
	    Sort sort;
	    
	    if (adminUser != null && adminUser.getAdmin() == 1) {
	        sort = Sort.by(
	                Sort.Order.asc("approved"),
	                Sort.Order.desc("createdAt")
	        );
	        letters = letterRepo.findAllByEmailAndYear(email, year, sort);
	    } else {
	    	sort = Sort.by(Sort.Order.desc("createdAt"));
	        letters = letterRepo.findAllByEmailAndYear(email, year, sort);
	        letters = letters.stream()
	                .filter(letter -> letter.getApproved() == 1)
	                .toList();
	    }
	    
	    if (email != null && email.trim().isEmpty()) {
	        email = null;
	    }

	    List<Integer> years = letterRepo.findDistinctYears();

	    model.addAttribute("letters", letters);
	    model.addAttribute("years", years);
	    model.addAttribute("selectedYear", year);
	    model.addAttribute("searchEmail", email);

	    return "letters/index";
	}

	@GetMapping("/create")
	public String createLetter(Model model) {
	    LetterDto letterDto = new LetterDto();
	    model.addAttribute("letterDto", letterDto);
	    return "letters/create";
	}

	@PostMapping("/create")
	public String createLetter(
	        @ModelAttribute @Valid LetterDto letterDto,
	        BindingResult result,
	        @RequestParam String email,
	        Model model) {

	    if (result.hasErrors()) {
	        return "letters/create";
	    }

	    User user = userRepo.findByEmail(email);
	    if (user == null) {
	        user = new User();
	        user.setEmail(email);
	        userRepo.save(user);
	    }

	    int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
	    long letterCount = letterRepo.countByUserIdAndYear(user.getId(), currentYear);
	    if (letterCount >= 3) {
	        model.addAttribute("limitMessage", "The elves are SO BUSY making the other wishes come to life!");
	        return "letters/create";
	    }

	    Letter letter = new Letter();
	    letter.setFirstName(letterDto.getFirstName());
	    letter.setLastName(letterDto.getLastName());
	    letter.setLetterText(letterDto.getLetterText());
	    letter.setAddress(letterDto.getAddress());
	    Date currentDate = new Date(System.currentTimeMillis());
		letter.setCreatedAt(currentDate);
	    letter.setUser(user);

	    letterRepo.save(letter);

	    return "/letters/information";
	}
	
	@GetMapping("/edit")
	public String editLetter(Model model, @RequestParam int id, HttpSession session) {
	    User adminUser = (User) session.getAttribute("adminUser");
	    if (adminUser == null || adminUser.getAdmin() != 1) {
	        return "redirect:/letters";
	    }
	    Letter letter = letterRepo.findById(id).orElse(null);
	    System.out.println("ajunge aici");
	    if (letter == null) {
	        return "redirect:/letters";
	    }

	    LetterDto letterDto = new LetterDto();
	    letterDto.setFirstName(letter.getFirstName());
	    letterDto.setLastName(letter.getLastName());
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
	        HttpSession session,
	        @Valid @ModelAttribute LetterDto letterDto,
	        BindingResult result) {
		
	    User adminUser = (User) session.getAttribute("adminUser");
	    if (adminUser == null || adminUser.getAdmin() != 1) {
	        return "redirect:/letters";
	    }
	    
	    Letter letter = letterRepo.findById(id).orElse(null);
	    if (letter == null) {
	        return "redirect:/letters";
	    }
	    
	    User user = letter.getUser();
	    if (user != null) {
	        model.addAttribute("email", user.getEmail());
	    }
	    
	    if (result.hasErrors()) {
	    	 System.out.println(result);
	        return "letters/edit";
	    }
	    
	    letter.setFirstName(letterDto.getFirstName());
	    letter.setLastName(letterDto.getLastName());
	    letter.setLetterText(letterDto.getLetterText());
	    letter.setAddress(letterDto.getAddress());


	    letterRepo.save(letter);
	    return "redirect:/letters";
	}
	
	@GetMapping("/delete")
	public String deleteLetter(@RequestParam int id, HttpSession session) {
		User adminUser = (User) session.getAttribute("adminUser");
			if (adminUser == null || adminUser.getAdmin() != 1) {
				return "redirect:/letters";
		}
		Letter letter = letterRepo.findById(id).orElse(null);
		
		if(letter != null) {
			letterRepo.delete(letter);
		}
		
		return "redirect:/letters";
	}
	
	@GetMapping("/approve")
	public String approveLetter(@RequestParam int id, HttpSession session) {
		User adminUser = (User) session.getAttribute("adminUser");
			if (adminUser == null || adminUser.getAdmin() != 1) {
				return "redirect:/letters";
		}
		Letter letter = letterRepo.findById(id).orElse(null);
		
		if(letter != null) {
			letter.setApproved(1);
			letterRepo.save(letter);
		}
		
		return "redirect:/letters";
	}
}
