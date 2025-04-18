package com.adatb.repjegy_fogalas.DAO;
import com.adatb.repjegy_fogalas.Model.Hotel;
import com.adatb.repjegy_fogalas.Model.Town;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class HotelDAO {
    private final JdbcTemplate jdbcTemplate;

    public HotelDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createHotel(Hotel hotel){
        jdbcTemplate.update("INSERT INTO SZALLODAK (id, varos_id, nev, leiras) VALUES (?,?,?,?)",
                hotel.getId(),hotel.getVarosId(),hotel.getName(),hotel.getDescription());
    }

    public List<Hotel> readAllHotel(){
        List<Hotel> result = jdbcTemplate.query("SELECT * FROM SZALLODAK",new HotelDAO.HotelRowMapper());
        return result.isEmpty()? null : result;
    }

    public int updateHotel(int id, String nev, String leiras){
        return jdbcTemplate.update("UPDATE SZALLODAK SET nev = ? AND leiras = ? WHERE id = ?", nev, leiras, id);
    }
    public int deleteHotel(int id){
        return jdbcTemplate.update("DELETE FROM SZALLODAK WHERE id = ?", id);
    }

    public static class HotelRowMapper implements RowMapper<Hotel>{

        @Override
        public Hotel mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Hotel.builder()
                    .id(rs.getInt("ID"))
                    .name(rs.getString("NEV"))
                    .varosId(rs.getInt("VAROS_ID"))
                    .description(rs.getString("LEIRAS"))
                    .build();
        }
    }
}
