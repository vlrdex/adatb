package com.adatb.repjegy_fogalas.DAO;
import com.adatb.repjegy_fogalas.Model.*;
import jakarta.annotation.PostConstruct;
import org.apache.catalina.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;
import oracle.jdbc.OracleTypes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Component
public class TicketDAO {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall jdbcCall;
    private SimpleJdbcCall nepszeru;

    @PostConstruct
    public void init() {
        jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("foglalas_statisztika")
                .returningResultSet("p_cursor", new StatRowMapper())
                .withoutProcedureColumnMetaDataAccess();
        nepszeru= new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("legnepszerubb_utvonalak")
                .withProcedureName("legnepszerubb_utvonalak")
                .declareParameters(
                        new SqlParameter("p_limit", Types.INTEGER),
                        new SqlOutParameter("p_cursor", OracleTypes.CURSOR, new FavoriteRowMapper())
                )
                .withoutProcedureColumnMetaDataAccess();
    }

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
    public List<IncomeStats> getIncomeStats(){
        List<IncomeStats> result = jdbcTemplate.query("SELECT      j.id AS jarat_id,     v1.nev AS indulasi_varos,     v2.nev AS celvaros,     j.kiindulasi_idopont,     r.szolgaltato,     COUNT(DISTINCT jegy.ulohely) AS eladott_jegyek,     j.ar AS alapar,     SUM(NVL(b.ar, 0)) AS biztositas_bevetel,     (COUNT(DISTINCT jegy.ulohely) * j.ar) - SUM(j.ar * NVL(jk.kedvezmeny, 0) / 100) AS jegy_bevetel,     (COUNT(DISTINCT jegy.ulohely) * j.ar) - SUM(j.ar * NVL(jk.kedvezmeny, 0) / 100) + SUM(NVL(b.ar, 0)) AS teljes_bevetel FROM      JARATOK j JOIN      VAROS v1 ON j.kiindulasi_hely = v1.id JOIN      VAROS v2 ON j.erkezesi_hely = v2.id JOIN      REPULOGEP r ON j.repulo_id = r.id LEFT JOIN      JEGYEK jegy ON j.id = jegy.jarat_id LEFT JOIN      FOGLALAS f ON j.id = f.jarat_id AND jegy.ulohely = f.ulohely LEFT JOIN      JEGYKATEGORIA jk ON f.jegykategoria_id = jk.id LEFT JOIN      BIZTOSITASOK b ON jegy.biztositas_id = b.id GROUP BY      j.id, v1.nev, v2.nev, j.kiindulasi_idopont, r.szolgaltato, j.ar ORDER BY      teljes_bevetel DESC", new TicketDAO.IncomeRowMapper());
        return result.isEmpty()? null : result;
    }

    public List<User_stat> ticketNumberForUsers(){
        return (List<User_stat>) jdbcCall.execute().get("p_cursor");
    }

    public List<FavoriteLocatons> favoriteLocatons(int num){
        MapSqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("p_limit", num);

        return (List<FavoriteLocatons>)nepszeru.execute(inParams).get("p_cursor");
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

    private static class StatRowMapper implements RowMapper<User_stat>{
        @Override
        public User_stat mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User_stat.builder()
                    .email(rs.getString("EMAIL"))
                    .db(rs.getInt("DARAB"))
                    .build();
        }
    }

    private static class FavoriteRowMapper implements RowMapper<FavoriteLocatons>{

        @Override
        public FavoriteLocatons mapRow(ResultSet rs, int rowNum) throws SQLException {
            return FavoriteLocatons.builder()
                    .ki(rs.getString("KI"))
                    .be(rs.getString("BE"))
                    .emberek(rs.getInt("EMBEREK"))
                    .build();
        }
    }
    private static class IncomeRowMapper implements RowMapper<IncomeStats>{

        @Override
        public IncomeStats mapRow(ResultSet rs, int rowNum) throws SQLException {
            return IncomeStats.builder()
                    .id(rs.getInt("JARAT_ID"))
                    .indulasi_varos(rs.getString("INDULASI_VAROS"))
                    .erkezesi_varos(rs.getString("CELVAROS"))
                    .indulasi_idopont(rs.getString("KIINDULASI_IDOPONT"))
                    .szolgaltato(rs.getString("SZOLGALTATO"))
                    .eladott_jegyek(rs.getInt("ELADOTT_JEGYEK"))
                    .alapar(rs.getInt("ALAPAR"))
                    .biztositasi_bevetel(rs.getInt("BIZTOSITAS_BEVETEL"))
                    .jegy_bevetel(rs.getInt("JEGY_BEVETEL"))
                    .teljes_bevetel(rs.getInt("TELJES_BEVETEL"))
                    .build();
        }
    }
}
