package com.adatb.repjegy_fogalas.DAO;
import com.adatb.repjegy_fogalas.Model.Booking;
import com.adatb.repjegy_fogalas.Model.Custom_Flight;
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
public class Custom_FlightDAO {
    private final JdbcTemplate jdbcTemplate;

    public Custom_FlightDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Custom_Flight> readAllCustom_Flight(){
        List<Custom_Flight> result = jdbcTemplate.query("SELECT JARATOK.ID AS FLIGHTID, BE.NEV AS STARTINGTOWN, " +
                "KI.NEV AS LANDINGTOWN, JARATOK.KIINDULASI_IDOPONT AS STARTINGTIME, " +
                "JARATOK.ERKEZESI_IDOPONT AS LANDINGTIME, JARATOK.REPULO_ID AS PLANEID, JARATOK.AR AS PRICE " +
                "FROM JARATOK, VAROS BE, VAROS KI " +
                "WHERE JARATOK.KIINDULASI_HELY = BE.ID AND " +
                "JARATOK.ERKEZESI_HELY = KI.ID",new Custom_FlightDAO.Custom_FlightRowMapper());
        return result.isEmpty()? null : result;
    }
    public Custom_Flight getCustomFlightByFlightId(int jarat_id){
        List<Custom_Flight> result = jdbcTemplate.query("SELECT JARATOK.ID AS FLIGHTID, BE.NEV AS STARTINGTOWN, " +
                "KI.NEV AS LANDINGTOWN, JARATOK.KIINDULASI_IDOPONT AS STARTINGTIME, " +
                "JARATOK.ERKEZESI_IDOPONT AS LANDINGTIME, JARATOK.REPULO_ID AS PLANEID, JARATOK.AR AS PRICE " +
                "FROM JARATOK, VAROS BE, VAROS KI " +
                "WHERE JARATOK.KIINDULASI_HELY = BE.ID AND " +
                "JARATOK.ERKEZESI_HELY = KI.ID AND " +
                "JARATOK.ID = ?",new Custom_FlightDAO.Custom_FlightRowMapper(), jarat_id);
        return result.get(0);
    }
    public static class Custom_FlightRowMapper implements RowMapper<Custom_Flight>{

        @Override
        public Custom_Flight mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Custom_Flight.builder()
                    .flightId(rs.getInt("FLIGHTID"))
                    .startingTown(rs.getString("STARTINGTOWN"))
                    .startingTime(rs.getObject("STARTINGTIME", LocalDateTime.class))
                    .landingTown(rs.getString("LANDINGTOWN"))
                    .landingTime(rs.getObject("LANDINGTIME", LocalDateTime.class))
                    .planeId(rs.getInt("PLANEID"))
                    .price(rs.getInt("PRICE"))
                    .build();
        }
    }
}
