package com.adatb.repjegy_fogalas.DAO;
import com.adatb.repjegy_fogalas.Model.Flight;
import com.adatb.repjegy_fogalas.Model.FlightDataTownToTown;
import com.adatb.repjegy_fogalas.Model.FlightNice;
import com.adatb.repjegy_fogalas.Model.Serch_Result;
import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Component
public class FlightDAO {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall jdbcCall;

    @PostConstruct
    public void init() {
        jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("kereses")
                .returningResultSet("p_cursor", new SerchResultRowMapper());
    }

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

    public List<FlightDataTownToTown> getFlightDataTownToTownWeek(){
        List<FlightDataTownToTown> result = jdbcTemplate.query("SELECT TRUNC(JARATOK.KIINDULASI_IDOPONT, 'IW') AS TIME, BE.NEV AS STARTINGTOWN, " +
                "KI.NEV AS LANDINGTOWN, COUNT(*) AS COUNT " +
                "FROM JARATOK " +
                "JOIN VAROS BE ON JARATOK.KIINDULASI_HELY = BE.ID " +
                "JOIN VAROS KI ON JARATOK.ERKEZESI_HELY = KI.ID " +
                "GROUP BY TRUNC(JARATOK.KIINDULASI_IDOPONT, 'IW'), BE.NEV, KI.NEV " +
                "ORDER BY TIME, STARTINGTOWN, LANDINGTOWN",new FlightDAO.FlightDataTownToTownRowMapper());
        return result.isEmpty()? null : result;
    }

    public List<FlightDataTownToTown> getFlightDataTownToTownMonth(){
        List<FlightDataTownToTown> result = jdbcTemplate.query("SELECT TRUNC(JARATOK.KIINDULASI_IDOPONT, 'MM') AS TIME, BE.NEV AS STARTINGTOWN, " +
                "KI.NEV AS LANDINGTOWN, COUNT(*) AS COUNT " +
                "FROM JARATOK " +
                "JOIN VAROS BE ON JARATOK.KIINDULASI_HELY = BE.ID " +
                "JOIN VAROS KI ON JARATOK.ERKEZESI_HELY = KI.ID " +
                "GROUP BY TRUNC(JARATOK.KIINDULASI_IDOPONT, 'MM'), BE.NEV, KI.NEV " +
                "ORDER BY TIME, STARTINGTOWN, LANDINGTOWN",new FlightDAO.FlightDataTownToTownRowMapper());
        return result.isEmpty()? null : result;
    }

    public List<FlightDataTownToTown> getFlightDataTownToTownPeoplesWeek(){
        List<FlightDataTownToTown> result = jdbcTemplate.query("SELECT TRUNC(JARATOK.KIINDULASI_IDOPONT, 'IW') AS TIME, BE.NEV AS STARTINGTOWN, " +
                "KI.NEV AS LANDINGTOWN, COUNT(FOGLALAS.ID) AS COUNT " +
                "FROM JARATOK " +
                "JOIN VAROS BE ON JARATOK.KIINDULASI_HELY = BE.ID " +
                "JOIN VAROS KI ON JARATOK.ERKEZESI_HELY = KI.ID " +
                "JOIN FOGLALAS ON JARATOK.ID = FOGLALAS.JARAT_ID " +
                "GROUP BY TRUNC(JARATOK.KIINDULASI_IDOPONT, 'IW'), BE.NEV, KI.NEV " +
                "ORDER BY TIME, STARTINGTOWN, LANDINGTOWN",new FlightDAO.FlightDataTownToTownRowMapper());
        return result.isEmpty()? null : result;
    }

    public List<FlightDataTownToTown> getFlightDataTownToTownPeoplesMonth(){
        List<FlightDataTownToTown> result = jdbcTemplate.query("SELECT TRUNC(JARATOK.KIINDULASI_IDOPONT, 'MM') AS TIME, BE.NEV AS STARTINGTOWN, " +
                "KI.NEV AS LANDINGTOWN, COUNT(FOGLALAS.ID) AS COUNT " +
                "FROM JARATOK " +
                "JOIN VAROS BE ON JARATOK.KIINDULASI_HELY = BE.ID " +
                "JOIN VAROS KI ON JARATOK.ERKEZESI_HELY = KI.ID " +
                "JOIN FOGLALAS ON JARATOK.ID = FOGLALAS.JARAT_ID " +
                "GROUP BY TRUNC(JARATOK.KIINDULASI_IDOPONT, 'MM'), BE.NEV, KI.NEV " +
                "ORDER BY TIME, STARTINGTOWN, LANDINGTOWN",new FlightDAO.FlightDataTownToTownRowMapper());
        return result.isEmpty()? null : result;
    }

    public int updateFlight(int id, int kiinduasi_hely, LocalDateTime kiindulasi_idopont, int erkezesi_hely, LocalDateTime erkezesi_idopont, int repulo_id, int ar){
        return jdbcTemplate.update("UPDATE JARATOK SET kiindulasi_hely = ?, kiindulasi_idopont = ?," +
                "erkezesi_hely = ?, erkezesi_idopont = ?," +
                " repulo_id = ?, ar = ? WHERE id = ?",
                kiinduasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar, id);
    }

    public List<Serch_Result> serch(int ki_hely,int be_hely,String date){
        MapSqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("nap", date)
                .addValue("ki_hely", ki_hely)
                .addValue("be_hely",be_hely);

        List<Serch_Result> result=(List<Serch_Result>)jdbcCall.execute(inParams).get("p_cursor");

        for (Serch_Result a:result){
            a.setFirst_flight(getFlightById(a.getFirst_id()));
            a.setFirst_flight(getFlightById(a.getSecond_id()));
        }

        return result;
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


    public static  class  FlightDataTownToTownRowMapper implements RowMapper<FlightDataTownToTown>{

        @Override
        public FlightDataTownToTown mapRow(ResultSet rs, int rowNum) throws SQLException {
            return FlightDataTownToTown.builder()
                    .time(rs.getObject("TIME", LocalDateTime.class))
                    .startingTown(rs.getString("STARTINGTOWN"))
                    .landingTown(rs.getString("LANDINGTOWN"))
                    .count(rs.getInt("COUNT"))
                    .build();
        }
    }

    public static class SerchResultRowMapper implements RowMapper<Serch_Result>{

        @Override
        public Serch_Result mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Serch_Result.builder()
                    .first_id(rs.getInt("FIRST"))
                    .second_id(rs.getInt("SECOND"))
                    .build();
        }
    }
}
