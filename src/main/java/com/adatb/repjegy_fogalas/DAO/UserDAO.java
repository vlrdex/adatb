package com.adatb.repjegy_fogalas.DAO;

import com.adatb.repjegy_fogalas.Model.Custom_User;
import com.adatb.repjegy_fogalas.Model.Ticket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Custom_User findByEmail(String email){
        List<Custom_User> result=jdbcTemplate.query("SELECT * FROM FELHASZNALOK WHERE email=?",new UserRowMapper(),email);
        return result.isEmpty()? null : result.get(0);
    }

    public void creatUser(Custom_User user){
        jdbcTemplate.update("INSERT INTO FELHASZNALOK (email, nev, jelszo, szuletesi_datum, admin) VALUES (?,?,?,TO_DATE(?, 'YYYY-MM-DD'),0)",
                user.getEmail(),user.getName(),user.getPassword(),user.getBirthDate());
    }

    public List<Custom_User> readAllUser(){
        List<Custom_User> result = jdbcTemplate.query("SELECT * FROM FELHASZNALOK",new UserDAO.UserRowMapper());
        return result.isEmpty()? null : result;
    }

    public int updateUser(Custom_User user,String old_email){
        return jdbcTemplate.update("UPDATE FELHASZNALOK SET email=? , nev = ? , jelszo=? , szuletesi_datum=TO_DATE(?, 'YYYY-MM-DD') WHERE email = ?"
                ,user.getEmail(),user.getName(),user.getPassword(),user.getBirthDate(),old_email);
    }

    public void deleteUser(String email, HttpServletRequest request, HttpServletResponse response){
        jdbcTemplate.update("DELETE FROM FELHASZNALOK WHERE email = ?", email);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    }


    public static class UserRowMapper implements RowMapper<Custom_User>{

        @Override
        public Custom_User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Custom_User.builder()
                    .email(rs.getString("EMAIL"))
                    .password(rs.getString("JELSZO"))
                    .name(rs.getString("NEV"))
                    .birthDate(rs.getString("SZULETESI_DATUM"))
                    .admin(rs.getInt("ADMIN")==1)
                    .build();
        }
    }
}
