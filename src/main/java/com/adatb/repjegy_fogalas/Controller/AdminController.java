package com.adatb.repjegy_fogalas.Controller;

import com.adatb.repjegy_fogalas.DAO.*;
import com.adatb.repjegy_fogalas.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private TownDAO townDAO;
    @Autowired
    private InsuranceDAO insuranceDAO;
    @Autowired
    private Ticket_CategoryDAO ticketCategoryDAO;
    @Autowired
    private Plane_ModelDAO planeModelDAO;
    @Autowired
    private PlaneDAO planeDAO;
    @Autowired
    private HotelDAO hotelDAO;
    @Autowired
    private FlightDAO flightDAO;
    @Autowired
    private TicketDAO ticketDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private BookingDAO bookingDAO;
    @GetMapping("/admin")
    public String admin(Model model){
        model.addAttribute("town", townDAO.readAllTown());
        model.addAttribute("insurance", insuranceDAO.readAllInsurance());
        model.addAttribute("ticketcategory", ticketCategoryDAO.readAllTicket_Category());
        model.addAttribute("planemodel", planeModelDAO.readAllPlane_Model());
        model.addAttribute("plane", planeDAO.readAllPlane());
        model.addAttribute("hotel", hotelDAO.readAllHotel());
        model.addAttribute("flight", flightDAO.readAllFlightNice());
        model.addAttribute("ticket", ticketDAO.readAllTicket());
        model.addAttribute("booking", bookingDAO.readAllBooking());
        model.addAttribute("userstat",ticketDAO.ticketNumberForUsers());
        model.addAttribute("statistic", planeModelDAO.getAveragePriceByModell());
        model.addAttribute("incomestat", ticketDAO.getIncomeStats());
        model.addAttribute("passengerdemog",userDAO.getPassengerDemog());
        model.addAttribute("szolgaltatostat", flightDAO.getSzolgaltatoStat());

        model.addAttribute("towntotownWeek", flightDAO.getFlightDataTownToTownWeek());
        return "admin";
    }

    @PostMapping(value = "admin/town/delete/{town_id}")
    public String deleteTown(@PathVariable("town_id") int town_id, RedirectAttributes redirectAttributes){
        try {
            townDAO.deleteTown(town_id);
        } catch (DataAccessException ex) {
            Throwable rootCause = ex.getCause();
            if (rootCause instanceof SQLException) {
                String message = rootCause.getMessage();
                if (message != null && message.contains("ORA-20001")) {
                    // Az ORA-20001 hibát (trigger dobta) elkapjuk
                    String error = "Nem törölhető a város, mert járatok kapcsolódnak hozzá!";
                    redirectAttributes.addFlashAttribute("error", error);
                    return "redirect:/admin";
                }
            }
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/town/update/{town_id}")
    public String updateTown(@PathVariable("town_id") int town_id, Model model) {
        Town town = townDAO.getTownById(town_id);
        if (town == null) {
            return "redirect:/admin";  // Ha nem található a város, átirányítás
        }
        model.addAttribute("town", town);
        return "town-update";  // Az update nézet visszaadása
    }
    @PostMapping("admin/town/update/{id}")
    public String updateMovie(@PathVariable("id") int town_id,
                              @RequestParam("name") String name) {
        townDAO.updateTown(town_id,name);
        return "redirect:/admin";
    }
    @GetMapping("admin/town/createtown")
    public String createTownView() {
        return "town-create";
    }
    @PostMapping(value = "/admin/town/createtown")
    public String createTown(
            @RequestParam("name") String name) {
        townDAO.createTown(name);
        return "redirect:/admin";
    }


    @PostMapping(value = "admin/insurance/delete/{insurance_id}")
    public String deleteInsurance(@PathVariable("insurance_id") int insurance_id){
        insuranceDAO.deleteInsurance(insurance_id);
        return "redirect:/admin";
    }
    @GetMapping("/admin/insurance/update/{insurance_id}")
    public String updateInsurance(@PathVariable("insurance_id") int insurance_id, Model model) {
        Insurance insurance = insuranceDAO.getInsuranceById(insurance_id);
        if (insurance == null) {
            return "redirect:/admin";  // Ha nem található a biztosítás, átirányítás
        }
        model.addAttribute("insurance", insurance);
        return "insurance-update";  // Az update nézet visszaadása
    }
    @PostMapping("admin/insurance/update/{id}")
    public String updateInsurance(@PathVariable("id") int insurance_id,
                                  @RequestParam("name") String name,
                                  @RequestParam("cost") int cost,
                                  @RequestParam("description") String description) {
        insuranceDAO.updateInsurance(insurance_id,name, cost, description);
        return "redirect:/admin";
    }
    @GetMapping("admin/insurance/createinsurance")
    public String createInsuranceView() {
        return "insurance-create";
    }
    @PostMapping(value = "/admin/insurance/createinsurance")
    public String createInsurance(@RequestParam("name") String name,
                                  @RequestParam("cost") int cost,
                                  @RequestParam("description") String description) {
        insuranceDAO.createInsurance(name,cost,description);
        return "redirect:/admin";
    }



    @PostMapping(value = "admin/ticketcategory/delete/{ticketcategory_id}")
    public String deleteTicketCategory(@PathVariable("ticketcategory_id") int ticketcategory_id){
        ticketCategoryDAO.deleteTicket_Category(ticketcategory_id);
        return "redirect:/admin";
    }
    @GetMapping("/admin/ticketcategory/update/{ticketcategory_id}")
    public String updateTicketCategory(@PathVariable("ticketcategory_id") int ticketcategory_id, Model model) {
        Ticket_Category ticket = ticketCategoryDAO.getTicketCategoryById(ticketcategory_id);
        if (ticket == null) {
            return "redirect:/admin";  // Ha nem található a jegykategória, átirányítás
        }
        model.addAttribute("ticketcategory", ticket);
        return "ticketcategory-update";  // Az update nézet visszaadása
    }
    @PostMapping("admin/ticketcategory/update/{id}")
    public String updateTicketCategory(@PathVariable("id") int ticketcategory_id,
                                  @RequestParam("name") String name,
                                  @RequestParam("discount") int discount) {
        ticketCategoryDAO.updateTicket_Category(ticketcategory_id,name, discount);
        return "redirect:/admin";
    }
    @GetMapping("admin/ticketcategory/createticketcategory")
    public String createTicketCategoryView() {
        return "ticketcategory-create";
    }
    @PostMapping(value = "/admin/ticketcategory/createticketcategory")
    public String createTicketCategory(@RequestParam("name") String name,
                                       @RequestParam("discount") int discount) {
        ticketCategoryDAO.createTicket_Category(name,discount);
        return "redirect:/admin";
    }


    @PostMapping(value = "admin/planemodel/delete/{planemodel_id}")
    public String deletePlaneModel(@PathVariable("planemodel_id") String planemodel_id, RedirectAttributes redirectAttributes){
        try{
            planeModelDAO.deletePlane_Model(planemodel_id);
        }catch (DataAccessException ex){
        Throwable rootCause = ex.getCause();
        if (rootCause instanceof SQLException) {
            String message = rootCause.getMessage();
            if (message != null && message.contains("ORA-20003")) {
                // Az ORA-20001 hibát (trigger dobta) elkapjuk
                String error = "Nem törölhető a modell, mert repülögép kapcsolódnak hozzá!";
                redirectAttributes.addFlashAttribute("error", error);
                return "redirect:/admin";
            }
        }
    }
        return "redirect:/admin";
    }
    @GetMapping("/admin/planemodel/update/{planemodel_id}")
    public String updatePlaneModel(@PathVariable("planemodel_id") String planemodel_id, Model model) {
        Plane_Model planeModel = planeModelDAO.getPlaneModelById(planemodel_id);
        if (planeModel == null) {
            return "redirect:/admin";  // Ha nem található a repülőmodel, átirányítás
        }
        model.addAttribute("planemodel", planeModel);
        return "planemodel-update";  // Az update nézet visszaadása
    }
    @PostMapping("admin/planemodel/update/{id}")
    public String updatePlaneModel(@PathVariable("id") String planemodel_id,
                                   @RequestParam("name") String name,
                                   @RequestParam("seatsNumber") int seatsNumber) {
        planeModelDAO.updatePlane_Model(planemodel_id,name, seatsNumber);
        return "redirect:/admin";
    }
    @GetMapping("admin/planemodel/createplanemodel")
    public String createPlaneModelView() {
        return "planemodel-create";
    }
    @PostMapping(value = "/admin/planemodel/createplanemodel")
    public String createPlaneModel(@RequestParam("model") String model,
                                   @RequestParam("name") String name,
                                   @RequestParam("seatsNumber") int seatsNumber) {
        planeModelDAO.createPlaneModel(model, name, seatsNumber);
        return "redirect:/admin";
    }



    @PostMapping(value = "admin/plane/delete/{plane_id}")
    public String deletePlane(@PathVariable("plane_id") int plane_id , RedirectAttributes redirectAttributes){
        try{
            planeDAO.deletePlane(plane_id);
        }catch (DataAccessException ex){
            Throwable rootCause = ex.getCause();
            if (rootCause instanceof SQLException) {
                String message = rootCause.getMessage();
                if (message != null && message.contains("ORA-20002")) {
                    // Az ORA-20001 hibát (trigger dobta) elkapjuk
                    String error = "Nem törölhető a repülőgép, mert járatok kapcsolódnak hozzá!";
                    redirectAttributes.addFlashAttribute("error", error);
                    return "redirect:/admin";
                }
            }
        }
        return "redirect:/admin";
    }
    @GetMapping("/admin/plane/update/{plane_id}")
    public String updatePlane(@PathVariable("plane_id") int plane_id, Model model) {
        Plane plane = planeDAO.getPlaneById(plane_id);
        if (plane == null) {
            return "redirect:/admin";  // Ha nem található a repülő, átirányítás
        }
        model.addAttribute("plane", plane);
        model.addAttribute("planemodel", planeModelDAO.readAllPlane_Model());
        return "plane-update";  // Az update nézet visszaadása
    }
    @PostMapping("admin/plane/update/{id}")
    public String updatePlane(@PathVariable("id") int plane_id,
                              @RequestParam("model") String model,
                              @RequestParam("serviceProvider") String serviceProvider) {
        planeDAO.updatePlane(plane_id, model, serviceProvider);
        return "redirect:/admin";
    }
    @GetMapping("admin/plane/createplane")
    public String createPlaneView(Model model) {
        model.addAttribute("planemodel", planeModelDAO.readAllPlane_Model());
        return "plane-create";
    }
    @PostMapping(value = "/admin/plane/createplane")
    public String createPlane(@RequestParam("model") String model,
                              @RequestParam("serviceProvider") String serviceProvider) {
        planeDAO.createPlane(model, serviceProvider);
        return "redirect:/admin";
    }




    @PostMapping(value = "admin/hotel/delete/{hotel_id}")
    public String delteHotel(@PathVariable("hotel_id") int hotel_id){
        hotelDAO.deleteHotel(hotel_id);
        return "redirect:/admin";
    }
    @GetMapping("/admin/hotel/update/{hotel_id}")
    public String updateHotel(@PathVariable("hotel_id") int hotel_id, Model model) {
        Hotel hotel = hotelDAO.getHotelById(hotel_id);
        Town town = townDAO.getTownById(hotel.getTownId());
        if (hotel == null) {
            return "redirect:/admin";  // Ha nem található a jegykategória, átirányítás
        }
        model.addAttribute("hotel", hotel);
        model.addAttribute("town", town);
        return "hotel-update";  // Az update nézet visszaadása
    }
//    TODO: TownID? lehessen-e megváltoztatni hogy melyik városban van a hotel?
    @PostMapping("admin/hotel/update/{id}")
    public String updateHotel(@PathVariable("id") int hotel_id,
                              @RequestParam("name") String name,
                              @RequestParam("description") String descripton) {
        hotelDAO.updateHotel(hotel_id, name, descripton);
        return "redirect:/admin";
    }
    @GetMapping("admin/hotel/createhotel")
    public String createHotelView(Model model) {
        model.addAttribute("town", townDAO.readAllTown());
        return "hotel-create";
    }
    @PostMapping(value = "/admin/hotel/createhotel")
    public String createHotel(@RequestParam("townId") int townId,
                              @RequestParam("name") String name,
                              @RequestParam("description") String description) {
        hotelDAO.createHotel(townId, name, description);
        return "redirect:/admin";
    }



    @PostMapping(value = "admin/flight/delete/{flight_id}")
    public String deleteFlight(@PathVariable("flight_id") int flight_id){
        flightDAO.deleteFlight(flight_id);
        return "redirect:/admin";
    }
    @GetMapping("/admin/flight/update/{flight_id}")
    public String updateFlight(@PathVariable("flight_id") int flight_id, Model model) {
        Flight flight = flightDAO.getFlightById(flight_id);
        if (flight == null) {
            return "redirect:/admin";  // Ha nem található a jegykategória, átirányítás
        }
        model.addAttribute("flight", flight);
        model.addAttribute("town", townDAO.readAllTown());
        model.addAttribute("plane", planeDAO.readAllPlane());
        model.addAttribute("starttown", townDAO.getTownById(flight.getStartingTown()));
        model.addAttribute("endtown", townDAO.getTownById(flight.getLandingTown()));
        model.addAttribute("thatplane", planeDAO.getPlaneById(flight.getPlaneId()));

        return "flight-update";  // Az update nézet visszaadása
    }

    @PostMapping("admin/flight/update/{id}")
    public String updateFlight(@PathVariable("id") int flight_id,
                               @RequestParam("startingTown") int startingTown,
                               @RequestParam("startingTime") LocalDateTime startingTime,
                               @RequestParam("landingTown") int landingTown,
                               @RequestParam("landingTime") LocalDateTime landingTime,
                               @RequestParam("planeId") int planeId,
                               @RequestParam("price") int price,
                               RedirectAttributes redirectAttributes) {

        try {
            flightDAO.updateFlight(flight_id, startingTown, startingTime, landingTown, landingTime, planeId, price);
        } catch (DataAccessException ex) {
            Throwable rootCause = ex.getCause();
            if (rootCause instanceof SQLException) {
                String message = rootCause.getMessage();
                if (message != null && message.contains("ORA-20001")) {
                    // Ha szeretnéd pontosan az üzenetet visszaadni:
                    String userMessage = message.split("ORA-20001:")[1].split("ORA-")[0].trim();
                    redirectAttributes.addFlashAttribute("error", userMessage);
                    return "redirect:/admin/flight/update/" + flight_id;
                }else if (message != null && message.contains("ORA-20107")){
                    String userMessage = message.split("ORA-20107:")[1].split("ORA-")[0].trim();
                    redirectAttributes.addFlashAttribute("error", userMessage);
                    return "redirect:/admin/flight/update/" + flight_id;
                }else if (message != null && message.contains("ORA-20002")){
                    String userMessage = message.split("ORA-20002:")[1].split("ORA-")[0].trim();
                    redirectAttributes.addFlashAttribute("error", userMessage);
                    return "redirect:/admin/flight/update/" + flight_id;
                }

            }
            redirectAttributes.addFlashAttribute("error", "Ismeretlen adatbázis hiba történt a járatmódosításkor.");
            return "redirect:/admin/flight/update/" + flight_id;
        }

        return "redirect:/admin";
    }

    @GetMapping("admin/flight/createflight")
    public String createFlightView(Model model) {
        model.addAttribute("town", townDAO.readAllTown());
        model.addAttribute("plane", planeDAO.readAllPlane());
        return "flight-create";
    }

    @PostMapping(value = "/admin/flight/createflight")
    public String createFlight(@RequestParam("startingTown") int startingTown,
                               @RequestParam("startingTime") LocalDateTime startingTime,
                               @RequestParam("landingTown") int landingTown,
                               @RequestParam("landingTime") LocalDateTime landingTime,
                               @RequestParam("planeId") int planeId,
                               @RequestParam("price") int price,
                               RedirectAttributes redirectAttributes) {

        if (landingTime.isBefore(startingTime)) {
            redirectAttributes.addFlashAttribute("error", "Hibás időpontok: az érkezés nem lehet korábban mint az indulás.");
            return "redirect:/admin/flight/createflight";
        }

        try {
            flightDAO.createFlight(startingTown, startingTime, landingTown, landingTime, planeId, price);
        } catch (DataAccessException ex) {
            Throwable rootCause = ex.getCause();
            if (rootCause instanceof SQLException) {
                String message = rootCause.getMessage();
                if (message != null && message.contains("ORA-20001")) {
                    // Trigger által küldött konkrét hibaüzenet kiírása
                    redirectAttributes.addFlashAttribute("error", "Az ár túl magas!");
                    return "redirect:/admin/flight/createflight";
                }else if (message != null && message.contains("ORA-20107")) {
                    // A múltbeli időpont ellenőrző trigger hibájának kezelése
                    redirectAttributes.addFlashAttribute("error", "A járat indulási időpontja nem lehet múltbeli időpont!");
                    return "redirect:/admin/flight/createflight";
                }else if (message != null && message.contains("ORA-20002")) {
                    // A múltbeli időpont ellenőrző trigger hibájának kezelése
                    redirectAttributes.addFlashAttribute("error", "Nem lehet az indulási város a célváros!");
                    return "redirect:/admin/flight/createflight";
                }
            }
            // Egyéb hiba
            redirectAttributes.addFlashAttribute("error", "Ismeretlen adatbázis hiba történt a járat létrehozásakor.");
            return "redirect:/admin/flight/createflight";
        }

        return "redirect:/admin";
    }





    @PostMapping(value = "admin/ticket/delete/{flight_id}/{seat}")
    public String deleteTicket(@PathVariable("flight_id") int flight_id,
                               @PathVariable("seat") int seat){
        ticketDAO.deleteTicket(flight_id, seat);
        return "redirect:/admin";
    }
    @GetMapping("/admin/ticket/update/{flight_id}/{seat}")
    public String updateTicket(@PathVariable("flight_id") int flight_id,
                               @PathVariable("seat") int seat, Model model) {
        Ticket ticket = ticketDAO.getTicketById(flight_id, seat);
        if (ticket == null) {
            return "redirect:/admin";  // Ha nem található a jegykategória, átirányítás
        }
        model.addAttribute("ticket", ticket);
        model.addAttribute("insurance", insuranceDAO.readAllInsurance());
        model.addAttribute("user", userDAO.readAllUser());

        return "ticket-update";  // Az update nézet visszaadása
    }

    @PostMapping("admin/ticket/update/{flight_id}/{seat}")
    public String updateTicket(@PathVariable("flight_id") int flight_id,
                               @PathVariable("seat") int seat,
                               @RequestParam("newSeat") int newSeat,
                               @RequestParam("insuranceId") int insuranceId,
                               @RequestParam("name") String name,
                               @RequestParam("email") String email) {
        ticketDAO.updateTicket(flight_id, seat, newSeat, insuranceId, name, email);
        return "redirect:/admin";
    }
    @GetMapping("admin/ticket/createticket")
    public String createTicketView(Model model) {
        model.addAttribute("insurance", insuranceDAO.readAllInsurance());
        model.addAttribute("flight", flightDAO.readAllFlight());
        model.addAttribute("user", userDAO.readAllUser());
        return "ticket-create";
    }
    @PostMapping(value = "/admin/ticket/createticket")
    public String createHotel(@RequestParam("flightId") int flightId,
                              @RequestParam("seat") int seat,
                              @RequestParam("insuranceId") int insuranceId,
                              @RequestParam("name") String name,
                              @RequestParam("email") String email) {
        ticketDAO.createTicket(flightId, seat, insuranceId, name, email);
        return "redirect:/admin";
    }



    @PostMapping(value = "admin/booking/delete/{booking_id}")
    public String deleteBooking(@PathVariable("booking_id") int booking_id){
        bookingDAO.deleteBooking(booking_id);
        return "redirect:/admin";
    }
    @GetMapping("/admin/booking/update/{booking_id}")
    public String updateBooking(@PathVariable("booking_id") int booking_id, Model model) {
        Booking booking = bookingDAO.getBookingById(booking_id);
        if (booking == null) {
            return "redirect:/admin";  // Ha nem található a jegykategória, átirányítás
        }
        model.addAttribute("booking", booking);
        model.addAttribute("ticketcategory", ticketCategoryDAO.readAllTicket_Category());
        model.addAttribute("flight", flightDAO.readAllFlight());

        return "booking-update";  // Az update nézet visszaadása
    }

    @PostMapping("admin/booking/update/{id}")
    public String updateFlight(@PathVariable("id") int booking_id,
                               @RequestParam("flight_id") int flight_id,
                               @RequestParam("ticket_category_id") int ticket_category_id,
                               @RequestParam("seat") int seat) {
        bookingDAO.updateBooking(booking_id, flight_id, ticket_category_id, seat);
        return "redirect:/admin";
    }
    @GetMapping("admin/booking/createbooking")
    public String createBookingView(Model model) {
        model.addAttribute("ticketcategory", ticketCategoryDAO.readAllTicket_Category());
        model.addAttribute("flight", flightDAO.readAllFlight());
        return "booking-create";
    }
    @PostMapping(value = "/admin/booking/createbooking")
    public String createHotel(@RequestParam("flight_id") int flight_id,
                              @RequestParam("ticket_category_id") int ticket_category_id,
                              @RequestParam("seat") int seat) {
        bookingDAO.createBooking(flight_id, ticket_category_id, seat);
        return "redirect:/admin";
    }

}
