package com.adatb.repjegy_fogalas.Controller;

import com.adatb.repjegy_fogalas.DAO.*;
import com.adatb.repjegy_fogalas.Model.Booking;
import com.adatb.repjegy_fogalas.Model.Custom_User;
import com.adatb.repjegy_fogalas.Model.Ticket;
import com.adatb.repjegy_fogalas.Service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    UserDAO userDAO;
    PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private Custom_TicketDAO customTicketDAO;
    @Autowired
    private TicketDAO ticketDAO;

    @Autowired
    UserController(UserDAO userDAO, PasswordEncoder passwordEncoder){
        this.userDAO=userDAO;
        this.passwordEncoder=passwordEncoder;
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute("fav",ticketDAO.favoriteLocatons(5));
        return "index";
    }

    @PostMapping("/index")
    public String index(Model model,@RequestParam("hany") int hany){
        model.addAttribute("fav",ticketDAO.favoriteLocatons(hany));
        return "index";
    }

    @GetMapping("/regist")
    public String regist(){
        return "regist";
    }



    @PostMapping("/regist")
    public String regist(
            @RequestParam("email") String email,
            @RequestParam("password") String pasword,
            @RequestParam("confirm_password") String password_conf,
            @RequestParam("name") String name,
            @RequestParam("birthdate") String birthDate,
            Model model
    ){
        List<String> errors=new ArrayList<>();
        if (userDAO.findByEmail(email)!=null){
            errors.add("Email már foglalt");
        }
        if (!pasword.equals(password_conf)){
            errors.add("Jelszó és ellenőrző jelszó nem egyezik");
        }
        if(errors.isEmpty()){
            pasword=passwordEncoder.encode(pasword);
            Custom_User user=new Custom_User(email,pasword,name,birthDate,false);
            try {
                userDAO.creatUser(user);
            } catch (DataAccessException ex) {
                Throwable rootCause = ex.getCause();
                if (rootCause instanceof SQLException) {
                    String message = rootCause.getMessage();
                    if (message != null && message.contains("ORA-20010")) {
                        errors.add("Nem lehet a születésnap a jövöben");
                    }
                }
            }
        }

        if(errors.isEmpty()){
            return "login";
        }
        model.addAttribute("errors",errors);
        return "regist";
    }

    @GetMapping("/profil")
    public String profil(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Custom_User user = userDAO.findByEmail(email);
        model.addAttribute("user",user);
        model.addAttribute("customticket", customTicketDAO.readUserCustom_Ticket(email));
        return "profil";

    }

    @PostMapping("/delete")
    public String delete(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        userDAO.deleteUser(email,request,response);
        return "index";
    }

    @GetMapping("/modify")
    public String modify(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Custom_User user=userDAO.findByEmail(email);
        model.addAttribute("user",user);
        return "modify";
    }



    @PostMapping("/modify")
    public String userUpdate(@RequestParam("email") String email,
                             @RequestParam("old_password") String old_password,
                             @RequestParam("password") String password,
                             @RequestParam("confirm_password") String password_conf,
                             @RequestParam("name") String name,
                             @RequestParam("birthdate") String birthDate,
                             Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String old_email = authentication.getName();


        List<String> errors = new ArrayList<>();

        if (!passwordEncoder.matches(old_password, userDAO.findByEmail(old_email).getPassword())) {
            errors.add("A régi jelszó nem megfelelő");
        }

        if (!old_email.equals(email) && userDAO.findByEmail(email) != null) {
            errors.add("Foglalt email cím");
        }
        if (!password.isEmpty() && !password.equals(password_conf)) {
            errors.add("A jelszó és az ellenőrző jelszó nem egyeznek meg");
        }
        if (password.isEmpty()) {
            password = old_password;
        }
        if (birthDate.isBlank()){
            birthDate=userDAO.findByEmail(old_email).getBirthDate();
        }
        if (errors.isEmpty()) {
            password = passwordEncoder.encode(password);
            Custom_User user = new Custom_User(email, password, name, birthDate, userDAO.findByEmail(old_email).isAdmin());
            try {
                userDAO.updateUser(user,old_email);
                Authentication authentication2 = SecurityContextHolder.getContext().getAuthentication();
                UserDetails updatedUser = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken newAuth =
                        new UsernamePasswordAuthenticationToken(updatedUser, authentication2.getCredentials(), updatedUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(newAuth);
            } catch (DataAccessException ex) {
                Throwable rootCause = ex.getCause();
                if (rootCause instanceof SQLException) {
                    String message = rootCause.getMessage();
                    if (message != null && message.contains("ORA-20010")) {
                        errors.add("Nem lehet a születésnap a jövöben");
                    }
                }
            }
            model.addAttribute("user",user);
        }

        if (errors.isEmpty()){
            return "profil";
        }
        model.addAttribute("errors", errors);
        return "modify";
    }





}
