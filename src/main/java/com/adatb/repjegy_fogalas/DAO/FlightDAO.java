package com.adatb.repjegy_fogalas.DAO;
import com.adatb.repjegy_fogalas.Model.Flight;
import com.adatb.repjegy_fogalas.Model.FlightNice;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class FlightDAO {
    private final JdbcTemplate jdbcTemplate;

    public FlightDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createFlight(int kiinduasi_hely, LocalDateTime kiindulasi_idopont, int erkezesi_hely, LocalDateTime erkezesi_idopont, int repulo_id, int ar){
        jdbcTemplate.update("INSERT INTO JARATOK (kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES (?,?,?,?,?,?)",
                kiinduasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar);
    }

    public List<Flight> readAllFlight(){
        List<Flight> result = jdbcTemplate.query("SELECT * FROM JARATOK",new FlightDAO.FlightRowMapper());
        return result.isEmpty()? null : result;
    }

    public List<FlightNice> readAllFlightNice(){
        return jdbcTemplate.query("SELECT J.id as i, J.kiindulasi_idopont as ki_ip, J.erkezesi_idopont as be_ip, V1.nev as ki_hely, V2.nev as be_hely , R.modell as rep , j.ar " +
                "FROM JARATOk J, VAROS V1, VAROS V2 , REPULOGEP R " +
                "WHERE J.kiindulasi_hely=V1.id AND J.erkezesi_hely=V2.id AND J.repulo_id=R.id",new FlightNiceRowMapper());
    }

    public Flight getFlightById(int id){
        List<Flight> result = jdbcTemplate.query("SELECT * FROM JARATOK WHERE id = ?",new FlightDAO.FlightRowMapper(), id);
        return result.get(0);
    }

    public int updateFlight(int id, int kiinduasi_hely, LocalDateTime kiindulasi_idopont, int erkezesi_hely, LocalDateTime erkezesi_idopont, int repulo_id, int ar){
        return jdbcTemplate.update("UPDATE JARATOK SET kiindulasi_hely = ?, kiindulasi_idopont = ?," +
                "erkezesi_hely = ?, erkezesi_idopont = ?," +
                " repulo_id = ?, ar = ? WHERE id = ?",
                kiinduasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar, id);
    }
    public int deleteFlight(int id){
        return jdbcTemplate.update("DELETE FROM JARATOK WHERE id = ?", id);
    }

    public static class FlightRowMapper implements RowMapper<Flight>{

        @Override
        public Flight mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Flight.builder()
                    .id(rs.getInt("ID"))
                    .startingTown(rs.getInt("KIINDULASI_HELY"))
                    .startingTime(rs.getObject("KIINDULASI_IDOPONT", LocalDateTime.class))
                    .landingTown(rs.getInt("ERKEZESI_HELY"))
                    .landingTime(rs.getObject("ERKEZESI_IDOPONT", LocalDateTime.class))
                    .planeId(rs.getInt("REPULO_ID"))
                    .price(rs.getInt("AR"))
                    .build();
        }
    }

    public static  class  FlightNiceRowMapper implements RowMapper<FlightNice>{

        @Override
        public FlightNice mapRow(ResultSet rs, int rowNum) throws SQLException {
            return FlightNice.builder()
                    .id(rs.getInt("I"))
                    .startingTown(rs.getString("KI_HELY"))
                    .landingTown(rs.getString("BE_HELY"))
                    .startingTime(rs.getObject("KI_IP", LocalDateTime.class))
                    .landingTime(rs.getObject("BE_IP",LocalDateTime.class))
                    .planeId(rs.getString("REP"))
                    .price(rs.getInt("AR"))
                    .build();
        }
    }
}
