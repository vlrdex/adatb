package com.adatb.repjegy_fogalas.DAO;
import com.adatb.repjegy_fogalas.Model.Insurance;
import com.adatb.repjegy_fogalas.Model.Ticket_Category;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class Ticket_CategoryDAO {
    private final JdbcTemplate jdbcTemplate;

    public Ticket_CategoryDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTicket_Category(String nev, int kedvezmeny){
        jdbcTemplate.update("INSERT INTO JEGYKATEGORIA (nev, kedvezmeny) VALUES (?,?)",
                nev,kedvezmeny);
    }

    public List<Ticket_Category> readAllTicket_Category(){
        List<Ticket_Category> result = jdbcTemplate.query("SELECT * FROM JEGYKATEGORIA",new Ticket_CategoryDAO.Ticket_CategoryRowMapper());
        return result.isEmpty()? null : result;
    }

    public Ticket_Category getTicketCategoryById(int id){
        List<Ticket_Category> result = jdbcTemplate.query("SELECT * FROM JEGYKATEGORIA WHERE id = ?",new Ticket_CategoryDAO.Ticket_CategoryRowMapper(), id);
        return result.get(0);
    }

    public int updateTicket_Category(int id, String nev, int kedvezmeny) {
        return jdbcTemplate.update("UPDATE JEGYKATEGORIA SET nev = ?, kedvezmeny = ? WHERE id = ?", nev, kedvezmeny, id);
    }
    public int deleteTicket_Category(int id){
        return jdbcTemplate.update("DELETE FROM JEGYKATEGORIA WHERE id = ?", id);
    }



    public static class Ticket_CategoryRowMapper implements RowMapper<Ticket_Category>{
        @Override
        public Ticket_Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Ticket_Category.builder()
                    .id(rs.getInt("ID"))
                    .name(rs.getString("NEV"))
                    .discount(rs.getInt("KEDVEZMENY"))
                    .build();
        }
    }
}
