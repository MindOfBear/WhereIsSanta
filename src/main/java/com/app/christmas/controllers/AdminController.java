package com.app.christmas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.christmas.models.User;
import com.app.christmas.repositories.UserRepository;

import jakarta.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/admin")
    public String adminLoginPage() {
        return "admin/login";
    }

    @PostMapping("/admin/login")
    public String adminLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {
    	
        if (session.getAttribute("adminUser") != null) {
            return "redirect:/letters";
        }

        User user = userRepo.findByEmail(email);
        if (user == null || !Objects.equals(user.getPassword(), password) || user.getAdmin() != 1) {
            return "redirect:/admin";
        }

        System.out.println("A ajuns");
        session.setAttribute("adminUser", user);
        return "redirect:/letters";
    }

    @GetMapping("/admin/logout")
    public String adminLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin";
    }
}