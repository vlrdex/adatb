package com.adatb.repjegy_fogalas.Controller;

import com.adatb.repjegy_fogalas.DAO.*;
import com.adatb.repjegy_fogalas.Model.Flight;
import com.adatb.repjegy_fogalas.Model.Plane_Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TicketController {
    @Autowired
    Custom_FlightDAO customFlightDAO;
    @Autowired
    InsuranceDAO insuranceDAO;
    @Autowired
    Ticket_CategoryDAO ticketCategoryDAO;
    @Autowired
    PlaneDAO planeDAO;
    @Autowired
    Plane_ModelDAO planeModelDAO;
    @Autowired
    TownDAO townDAO;
    @Autowired
    FlightDAO flightDAO;
    @Autowired
    TicketDAO ticketDAO;
    @Autowired
    BookingDAO bookingDAO;

    @GetMapping("/ticket")
    public String ticket(Model model){
        model.addAttribute("customflight", customFlightDAO.readAllCustom_Flight());
        return "ticket";

    }
    @GetMapping("/ticket/create/{flight_id}")
    public String createTicket(@PathVariable("flight_id") int flight_id, Model model) {

        model.addAttribute("flight", flightDAO.getFlightById(flight_id));
        model.addAttribute("customflight", customFlightDAO.getCustomFlightByFlightId(flight_id));
        model.addAttribute("insurance", insuranceDAO.readAllInsurance());
        model.addAttribute("ticketcategory", ticketCategoryDAO.readAllTicket_Category());
        return "user-ticket-create";
    }
    @PostMapping("/ticket/create/{flight_id}")
    public String createTicket(@PathVariable("flight_id") int flight_id,
                               @RequestParam("ticketCategoryId") int ticketCategoryId,
                               @RequestParam("seat") int seat,
                               @RequestParam("insuranceId") int insuranceId,
                               @RequestParam("name") String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Flight flight = flightDAO.getFlightById(flight_id);
        // TODO: ülőhely ellenörzése
        ticketDAO.createTicket(flight_id, seat, insuranceId, name, email);
        bookingDAO.createBooking(flight_id, ticketCategoryId, seat);
        return "redirect:/ticket";
    }
}
