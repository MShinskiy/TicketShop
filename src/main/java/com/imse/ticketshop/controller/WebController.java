package com.imse.ticketshop.controller;

import com.imse.ticketshop.entity.Concert;
import com.imse.ticketshop.entity.Venue;
import com.imse.ticketshop.entity.dto.GenrePopularityReportDto;
import com.imse.ticketshop.entity.enumeration.Role;
import com.imse.ticketshop.service.ConcertService;
import com.imse.ticketshop.service.CustomerService;
import com.imse.ticketshop.service.VenueService;
import com.imse.ticketshop.service.db.DBFillerService;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.*;

@Controller
public class WebController {

    private final CustomerService customerService;
    private final ConcertService concertService;
    private final VenueService venueService;
    private final DBFillerService dbFillerService;
    private static final Logger log = LogManager.getLogger();


    public WebController(CustomerService customerService, ConcertService concertService, VenueService venueService, DBFillerService dbFillerService) {
        this.concertService = concertService;
        this.venueService = venueService;
        this.customerService = customerService;
        this.dbFillerService = dbFillerService;
    }

    @GetMapping("/db/fill")
    @ResponseStatus(HttpStatus.OK)
    public void fillDb() {
        dbFillerService.fillDatabase();
    }

    @DeleteMapping("/db/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDb() {
        //dbFillerService.fillDatabase();
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
    public String doLogin(@RequestBody String id, HttpSession session) {
        UUID uuid = UUID.fromString(id.replace("id=", ""));
        session.setAttribute("login", uuid);
        Role role = null;
        String name = null;
        if(customerService.doesCustomerWithIdExist(uuid)) {
            role = Role.CUSTOMER;
            name = customerService.getCustomerById(uuid).getEmail();
        } else if(venueService.doesVenueWithIdExist(uuid)) {
            role = Role.VENUE;
            name = venueService.getVenueById(uuid).getName();
        } else {
            /* TODO handle non-existing using */
        }
        session.setAttribute("role", role);
        session.setAttribute("name", name);
        log.info("User with id {} logged in", uuid);
        return "redirect:/home";
    }

    @PostMapping( "/concert/create")
    public String addConcert(@ModelAttribute("concert") Concert concert, HttpSession session) {
        //Set owner of concert
        UUID venueUUID = (UUID) session.getAttribute("login");
        concert.setVenue(venueService.getVenueById(venueUUID));
        concertService.addConcert(concert);
        log.info("Concert created: {}", concert.toString());
        return "redirect:/home";
    }

    @GetMapping("/concert/create")
    public String getConcertForm(Model model) {
        //Init concert for creation
        Concert concert = new Concert();
        model.addAttribute("concert", concert);
        return "create-concert";
    }

    @GetMapping("/report/popularity")
    public String getPopularityReport(Model model, HttpSession session) {
        //Generate report
        List<GenrePopularityReportDto> report = venueService.getReport();
        if(report != null) log.debug("Report has been successfully generated.");
        //Send to frontend
        model.addAttribute("report", report);
        return "popularity-report";
    }
}
