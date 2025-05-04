package com.adatb.repjegy_fogalas.DAO;

import com.adatb.repjegy_fogalas.Model.Custom_User;
import com.adatb.repjegy_fogalas.Model.IncomeStats;
import com.adatb.repjegy_fogalas.Model.PassengerDemog;
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

    public List<PassengerDemog> getPassengerDemog(){
        List<PassengerDemog> result = jdbcTemplate.query("SELECT \n" +
                "    CASE \n" +
                "        WHEN EXTRACT(YEAR FROM SYSDATE) - EXTRACT(YEAR FROM f.szuletesi_datum) < 18 THEN 'Kiskorú'\n" +
                "        WHEN EXTRACT(YEAR FROM SYSDATE) - EXTRACT(YEAR FROM f.szuletesi_datum) BETWEEN 18 AND 30 THEN 'Fiatal felnőtt'\n" +
                "        WHEN EXTRACT(YEAR FROM SYSDATE) - EXTRACT(YEAR FROM f.szuletesi_datum) BETWEEN 31 AND 50 THEN 'Középkorú'\n" +
                "        WHEN EXTRACT(YEAR FROM SYSDATE) - EXTRACT(YEAR FROM f.szuletesi_datum) BETWEEN 51 AND 65 THEN 'Idősebb felnőtt'\n" +
                "        ELSE 'Idős'\n" +
                "    END AS korcsoport,\n" +
                "    COUNT(DISTINCT f.email) AS utasok_szama,\n" +
                "    COUNT(DISTINCT j.jarat_id) AS repulesek_szama,\n" +
                "    ROUND(AVG(jar.ar), 2) AS atlagos_jegyar,\n" +
                "    ROUND(AVG(COALESCE(b.ar, 0)), 2) AS atlagos_biztositas_ar\n" +
                "FROM \n" +
                "    FELHASZNALOK f\n" +
                "LEFT JOIN \n" +
                "    JEGYEK j ON f.email = j.email\n" +
                "LEFT JOIN \n" +
                "    JARATOK jar ON j.jarat_id = jar.id\n" +
                "LEFT JOIN \n" +
                "    BIZTOSITASOK b ON j.biztositas_id = b.id\n" +
                "GROUP BY \n" +
                "    CASE \n" +
                "        WHEN EXTRACT(YEAR FROM SYSDATE) - EXTRACT(YEAR FROM f.szuletesi_datum) < 18 THEN 'Kiskorú'\n" +
                "        WHEN EXTRACT(YEAR FROM SYSDATE) - EXTRACT(YEAR FROM f.szuletesi_datum) BETWEEN 18 AND 30 THEN 'Fiatal felnőtt'\n" +
                "        WHEN EXTRACT(YEAR FROM SYSDATE) - EXTRACT(YEAR FROM f.szuletesi_datum) BETWEEN 31 AND 50 THEN 'Középkorú'\n" +
                "        WHEN EXTRACT(YEAR FROM SYSDATE) - EXTRACT(YEAR FROM f.szuletesi_datum) BETWEEN 51 AND 65 THEN 'Idősebb felnőtt'\n" +
                "        ELSE 'Idős'\n" +
                "    END\n" +
                "ORDER BY \n" +
                "    utasok_szama DESC",new UserDAO.PassengerRowMapper());
        return result.isEmpty()? null : result;
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
    public static class PassengerRowMapper implements RowMapper<PassengerDemog>{

        @Override
        public PassengerDemog mapRow(ResultSet rs, int rowNum) throws SQLException {
            return PassengerDemog.builder()
                    .korcsoport(rs.getString("KORCSOPORT"))
                    .utasok_szama(rs.getInt("UTASOK_SZAMA"))
                    .repulesek_szama(rs.getInt("REPULESEK_SZAMA"))
                    .atlagos_jegyar(rs.getInt("ATLAGOS_JEGYAR"))
                    .atlagos_biztositas_ara(rs.getInt("ATLAGOS_BIZTOSITAS_AR"))
                    .build();
        }
    }
}
