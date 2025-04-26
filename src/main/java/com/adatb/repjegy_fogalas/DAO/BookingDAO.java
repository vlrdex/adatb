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
public class BookingDAO {
    private final JdbcTemplate jdbcTemplate;

    public BookingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createBooking(int jarat_id, int jegykategoria_id, int ulohely){
        jdbcTemplate.update("INSERT INTO FOGLALAS (jarat_id, jegykategoria_id, ulohely) VALUES (?,?,?)",
                jarat_id, jegykategoria_id, ulohely);
    }

    public List<Booking> readAllBooking(){
        List<Booking> result = jdbcTemplate.query("SELECT * FROM FOGLALAS",new BookingDAO.BookingRowMapper());
        return result.isEmpty()? null : result;
    }

    public Booking getBookingById(int id){
        List<Booking> result = jdbcTemplate.query("SELECT * FROM FOGLALAS WHERE id = ?",new BookingDAO.BookingRowMapper(), id);
        return result.get(0);
    }
    public Booking getBookingByFlightIdAndSeat(int jarat_id, int ulohely){
        List<Booking> result = jdbcTemplate.query("SELECT * FROM FOGLALAS WHERE jarat_id = ? AND ulohely = ?",new BookingDAO.BookingRowMapper(), jarat_id, ulohely);
        return result.get(0);
    }

    public int updateBooking(int id, int jarat_id, int jegykategoria_id, int ulohely){
        return jdbcTemplate.update("UPDATE FOGLALAS SET jarat_id = ?, jegykategoria_id = ?, ulohely = ? WHERE id = ?",
                jarat_id, jegykategoria_id, ulohely, id);
    }
    public int deleteBooking(int id){
        return jdbcTemplate.update("DELETE FROM FOGLALAS WHERE id = ?", id);
    }

    public static class BookingRowMapper implements RowMapper<Booking>{

        @Override
        public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Booking.builder()
                    .id(rs.getInt("ID"))
                    .flight_id(rs.getInt("JARAT_ID"))
                    .ticket_category_id(rs.getInt("JEGYKATEGORIA_ID"))
                    .seat(rs.getInt("ULOHELY"))
                    .build();
        }
    }
}
