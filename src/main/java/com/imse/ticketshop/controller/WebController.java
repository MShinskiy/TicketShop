package com.imse.ticketshop.controller;

import com.imse.ticketshop.entity.Customer;
import com.imse.ticketshop.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class WebController {

    CustomerService customerService;

    public WebController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/home")
    public String getHomeScreen(Model model, HttpSession session) {
        session.setAttribute("LOGGEDIN", true);
        Customer c = customerService.getCustomer();
        model.addAttribute("abc", c);
        return "home";
    }

    @PostMapping("/filldb")
    public void fillDb() {

    }

    @GetMapping("/login")
    public String getLoginPage() {
        /*
        TODO get all customers and venues
         */

        return "login";
    }

    @PostMapping("/login")
    public String doLogin() {
        return "home";
    }

    @PostMapping("/addConcert")
    public String addConcert(@RequestBody MultiValueMap<String, String> values) {

        return "home";
    }
}
