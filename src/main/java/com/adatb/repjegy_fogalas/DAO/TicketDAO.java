package com.adatb.repjegy_fogalas.DAO;
import com.adatb.repjegy_fogalas.Model.Booking;
import com.adatb.repjegy_fogalas.Model.Ticket;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class TicketDAO {
    private final JdbcTemplate jdbcTemplate;

    public TicketDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTicket(int jarat_id, int ulohely, int biztositas_id, String nev, String email){
        jdbcTemplate.update("INSERT INTO JEGYEK (jarat_id, ulohely, biztositas_id, nev, email) VALUES (?,?,?,?,?)",
                jarat_id, ulohely, biztositas_id, nev, email);
    }

    public List<Ticket> readAllTicket(){
        List<Ticket> result = jdbcTemplate.query("SELECT * FROM JEGYEK",new TicketDAO.TicketRowMapper());
        return result.isEmpty()? null : result;
    }

    public Ticket getTicketById(int jarat_id, int ulohely){
        List<Ticket> result = jdbcTemplate.query("SELECT * FROM JEGYEK WHERE jarat_id = ? AND ulohely = ?",new TicketDAO.TicketRowMapper(),
                jarat_id, ulohely);
        return result.get(0);
    }

    public int updateTicket(int jarat_id,int ulohely, int uj_ulohely, int biztositas_id, String nev, String email){
        return jdbcTemplate.update("UPDATE JEGYEK SET ulohely = ?, biztositas_id = ?, nev = ?, email = ? WHERE jarat_id = ? AND ulohely = ?",
                uj_ulohely, biztositas_id, nev, email, jarat_id, ulohely);
    }
    public int deleteTicket(int jarat_id, int ulohely){
        return jdbcTemplate.update("DELETE FROM JEGYEK WHERE jarat_id = ? AND ulohely = ?", jarat_id, ulohely);
    }

    public List<Ticket> userTicket(String email){
        List<Ticket> result = jdbcTemplate.query("SELECT * FROM FOGLALAS WHERE email = ?",new TicketDAO.TicketRowMapper(), email);
        return result;
    }

    public static class TicketRowMapper implements RowMapper<Ticket>{

        @Override
        public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Ticket.builder()
                    .flightId(rs.getInt("JARAT_ID"))
                    .seat(rs.getInt("ULOHELY"))
                    .insuranceId(rs.getInt("BIZTOSITAS_ID"))
                    .name(rs.getString("NEV"))
                    .email(rs.getString("EMAIL"))
                    .build();
        }
    }
}
