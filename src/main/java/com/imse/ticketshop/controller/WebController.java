package com.imse.ticketshop.controller;

import com.imse.ticketshop.service.CustomerService;
import com.imse.ticketshop.service.VenueService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Controller
public class WebController {

    private final CustomerService customerService;
    private final VenueService venueService;

    public WebController(CustomerService customerService, VenueService venueService) {
        this.venueService = venueService;
        this.customerService = customerService;
    }

    @GetMapping("/home")
    public String getHomeScreen(Model model, HttpSession session) {
        session.setAttribute("LOGGEDIN", true);
        //Customer c = customerService.getCustomer();
        //model.addAttribute("abc", c);
        return "home";
    }

    @PostMapping("/filldb")
    public void fillDb() {

    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        //Combine all users and send them to front
        Map<UUID, String> idUsernameMap = new HashMap<>();
        venueService.getAllVenues().forEach(venue -> idUsernameMap.put(venue.getVenueId(), venue.getEmail() + " (venue)"));
        customerService.getAllCustomer().forEach(customer -> idUsernameMap.put(customer.getCustomerId(), customer.getEmail() + " (customer)"));
        model.addAttribute("users", idUsernameMap);
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestBody String id) {

        return "home";
    }

    @PostMapping("/addConcert")
    public String addConcert(@RequestBody MultiValueMap<String, String> values) {

        return "home";
    }
}
