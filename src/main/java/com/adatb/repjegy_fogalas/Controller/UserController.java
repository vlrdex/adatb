package com.adatb.repjegy_fogalas.Controller;

import com.adatb.repjegy_fogalas.DAO.TownDAO;
import com.adatb.repjegy_fogalas.DAO.UserDAO;
import com.adatb.repjegy_fogalas.Model.Custom_User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    UserDAO userDAO;
    PasswordEncoder passwordEncoder;

    @Autowired
    UserController(UserDAO userDAO, PasswordEncoder passwordEncoder){
        this.userDAO=userDAO;
        this.passwordEncoder=passwordEncoder;
    }
    @Autowired
    private TownDAO townDAO;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/regist")
    public String regist(){
        return "regist";
    }

    @GetMapping("/admin")
    public String admin(Model model){
        model.addAttribute("town", townDAO.readAllTown());
        return "admin";
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
            userDAO.creatUser(user);
            return "login";
        }

        model.addAttribute("errors",errors);
        return "regist";
    }



}
