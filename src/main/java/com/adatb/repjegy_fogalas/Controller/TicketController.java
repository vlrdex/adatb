package com.adatb.repjegy_fogalas.Controller;

import com.adatb.repjegy_fogalas.DAO.*;
import com.adatb.repjegy_fogalas.Model.Flight;
import com.adatb.repjegy_fogalas.Model.Hotel;
import com.adatb.repjegy_fogalas.Model.Plane_Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Controller
public class TicketController {
    @Autowired
    HotelDAO hotelDAO;
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
        model.addAttribute("city",townDAO.readAllTown());
        model.addAttribute("customflight", customFlightDAO.readAllCustom_Flight());
        return "ticket";

    }


    @GetMapping("/ticket/create/{flight_id}")
    public String createTicket(@PathVariable("flight_id") int flight_id, Model model) {

        model.addAttribute("flight", flightDAO.getFlightById(flight_id));
        model.addAttribute("customflight", customFlightDAO.getCustomFlightByFlightId(flight_id));
        model.addAttribute("insurance", insuranceDAO.readAllInsurance());
        model.addAttribute("ticketcategory", ticketCategoryDAO.readAllTicket_Category());

        Flight flight = flightDAO.getFlightById(flight_id);
        int startingTownId = flight.getLandingTown();
        List<Hotel> hotels = hotelDAO.getHotelByTownId(startingTownId);
        model.addAttribute("hotels", hotels);

        return "user-ticket-create";
    }

    @GetMapping("/ticket/search")
    public String search(RedirectAttributes model,
                         @RequestParam("nap") String date,
                         @RequestParam("be_hely") int be_hely, @RequestParam("ki_hely") int ki_hely){
        model.addFlashAttribute("searchResult",flightDAO.serch(ki_hely,be_hely,date));
        return "redirect:/ticket";
    }

    @PostMapping("/ticket/create/{flight_id}")
    public String createTicket(@PathVariable("flight_id") int flight_id,
                               @RequestParam("ticketCategoryId") int ticketCategoryId,
                               @RequestParam("seat") int seat,
                               @RequestParam("insuranceId") int insuranceId,
                               @RequestParam("name") String name,
                               @RequestParam(value = "hotelId", required = false) Integer hotelId,
                               RedirectAttributes redirectAttributes) { // ÚJ SOR
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Flight flight = flightDAO.getFlightById(flight_id);

        try{
            bookingDAO.createBooking(flight_id, ticketCategoryId, seat);
        }catch (DataAccessException ex){
            Throwable rootCause = ex.getCause();
            if (rootCause instanceof SQLException) {
                String message = rootCause.getMessage();
                if (message != null && message.contains("ORA-20005")) {
                    // Az ORA-20001 hibát (trigger dobta) elkapjuk
                    String error = "Az ülöhely már foglalt!";
                    redirectAttributes.addFlashAttribute("error", error);
                    return "redirect:/ticket/create/"+flight_id;
            }
        }
        }
        ticketDAO.createTicket(flight_id, seat, insuranceId, name, email);

        // Ha szállodát is választott:
        if (hotelId != null) {
            // TODO: itt el tudod menteni a szállodát a foglaláshoz, pl. egy bookingDAO.createHotelBooking() vagy hasonló
            System.out.println("Választott hotel ID: " + hotelId);
        }

        return "redirect:/ticket";
    }


}
