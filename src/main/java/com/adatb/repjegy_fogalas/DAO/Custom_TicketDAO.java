package com.adatb.repjegy_fogalas.DAO;
import com.adatb.repjegy_fogalas.Model.Booking;
import com.adatb.repjegy_fogalas.Model.Custom_Ticket;
import com.adatb.repjegy_fogalas.Model.Ticket;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class Custom_TicketDAO {
    private final JdbcTemplate jdbcTemplate;

    public Custom_TicketDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Custom_Ticket> readAllCustom_Ticket(){
        List<Custom_Ticket> result = jdbcTemplate.query("SELECT JEGYKATEGORIA.NEV, JEGYEK.ULOHELY, BIZTOSITASOK.NEV, " +
                "JEGYEK.NEV, JEGYEK.EMAIL, KI.NEV AS KINT, BE.NEV AS BENT, JARATOK.KIINDULASI_IDOPONT, JARATOK.ERKEZESI_IDOPONT, JARATOK.AR " +
                "FROM JARATOK, BIZTOSITASOK, JEGYEK, JEGYKATEGORIA, VAROS KI, VAROS BE " +
                "WHERE JEGYKATEGORIA.ID = JEGYEK.JEGYKATEGORIA_ID AND " +
                "BIZTOSITASOK.ID = JEGYEK.BIZTOSITAS_ID AND " +
                "KI.ID = JARATOK.KIINDULASI_HELY AND " +
                "BE.ID = JARATOK.ERKEZESI_HELY AND " +
                "JARATOK.ID = JEGYEK.JARAT_ID AND ",new Custom_TicketDAO.Custom_TicketRowMapper());
        return result.isEmpty()? null : result;
    }
    public List<Custom_Ticket> readUserCustom_Ticket(String email){
        List<Custom_Ticket> result = jdbcTemplate.query("SELECT JEGYKATEGORIA.NEV AS JEGYK, JEGYEK.ULOHELY AS ULO, BIZTOSITASOK.NEV AS BIZT, " +
                "JEGYEK.NEV AS JEGYNEV, JEGYEK.EMAIL AS JEGYEMAIL, KI.NEV AS KINT, BE.NEV AS BENT, JARATOK.KIINDULASI_IDOPONT AS KINTIDO, JARATOK.ERKEZESI_IDOPONT AS BENTIDO, JARATOK.AR AS AR " +
                "FROM JARATOK, BIZTOSITASOK, JEGYEK, JEGYKATEGORIA, VAROS KI, VAROS BE " +
                "WHERE JEGYKATEGORIA.ID = JEGYEK.JEGYKATEGORIA_ID AND " +
                "BIZTOSITASOK.ID = JEGYEK.BIZTOSITAS_ID AND " +
                "KI.ID = JARATOK.KIINDULASI_HELY AND " +
                "BE.ID = JARATOK.ERKEZESI_HELY AND " +
                "JARATOK.ID = JEGYEK.JARAT_ID AND " +
                "JEGYEK.EMAIL = ?",new Custom_TicketDAO.Custom_TicketRowMapper(), email);
        return result.isEmpty()? null : result;
    }
    public static class Custom_TicketRowMapper implements RowMapper<Custom_Ticket>{

        @Override
        public Custom_Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Custom_Ticket.builder()
                    .ticket_category(rs.getString("JEGYK"))
                    .seat(rs.getInt("ULO"))
                    .insurance(rs.getString("BIZT"))
                    .name(rs.getString("JEGYNEV"))
                    .email(rs.getString("JEGYEMAIL"))
                    .startingTown(rs.getString("KINT"))
                    .landingTown(rs.getString("BENT"))
                    .startingTime(rs.getObject("KINTIDO", LocalDateTime.class))
                    .landingTime(rs.getObject("BENTIDO", LocalDateTime.class))
                    .price(rs.getInt("AR"))
                    .build();
        }
    }
}
