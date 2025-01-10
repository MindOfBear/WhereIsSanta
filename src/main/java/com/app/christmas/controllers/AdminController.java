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

        User user = userRepo.findByEmail(email);
        if (user == null || !user.getPassword().equals(password) || user.getAdmin() != 1) {
            model.addAttribute("errorMessage", "Invalid credentials or not an admin.");
            return "admin/login";
        }

        session.setAttribute("adminUser", user);

        return "redirect:/letters";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        User adminUser = (User) session.getAttribute("adminUser");
        if (adminUser == null) {
        	return "redirect:/letters";
        }

        model.addAttribute("adminUser", adminUser);
        return "letters";
    }

    @GetMapping("/admin/logout")
    public String adminLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin";
    }
}