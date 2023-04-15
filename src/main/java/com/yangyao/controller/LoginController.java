package com.yangyao.controller;

import com.yangyao.dao.AccountDao;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    AccountDao accounts;

    @RequestMapping("/user/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession session) {
        int id = accounts.checkLogin(username, password);
        if (id >= 0) {
            session.setAttribute("loginUser", username);
            session.setAttribute("loginUserId", id);
            return "redirect:/main.html";
        } else {
            model.addAttribute("msg", "Wrong username or password");
            return "index";
        }
    }

    @RequestMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/index.html";
    }

    @RequestMapping("/user/register")
    public String toRegisterPage() {
        return "register";
    }

    @PostMapping("/user/register")
    public String register(/*@RequestParam("username") */String username, /*@RequestParam("password") */String password, Model model) {
        if (username == null || password == null) {
            model.addAttribute("msg", "Username cannot be null");
        } else if (username.length() < 6 || username.length() > 60) {
            model.addAttribute("msg", "Username must be between 6 and 60 characters long");
        } else if (password.length() < 8 || password.length() > 20) {
            model.addAttribute("msg", "Password must be between 8 and 20 characters long");
        } else if (!accounts.validUsername(username)) {
            model.addAttribute("msg", "Username must start with letters or numbers, and may only contain letters, numbers, period (.), at symbol (@), underscore (_), and hyphen (-)");
        } else if (!accounts.validUsername(password)) {
            model.addAttribute("msg", "Password must start with letters or numbers, and may only contain letters, numbers, period (.), at symbol (@), underscore (_), and hyphen (-)");
        } else if (accounts.checkDuplicate(username)) {
            model.addAttribute("msg", "Username already exists");
        } else {
            accounts.addAccount(username, password);
            model.addAttribute("msg", "Account successfully created");
        }
        return "register";
    }

    @RequestMapping("/user/back")
    public String toLoginPage() {
        return "redirect:/index.html";
    }
}
