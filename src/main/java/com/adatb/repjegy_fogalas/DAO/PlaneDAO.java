package com.adatb.repjegy_fogalas.DAO;
import com.adatb.repjegy_fogalas.Model.Plane;
import com.adatb.repjegy_fogalas.Model.Ticket_Category;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class PlaneDAO {
    private final JdbcTemplate jdbcTemplate;

    public PlaneDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createPlane(String model, String szolgaltato){
        jdbcTemplate.update("INSERT INTO REPULOGEP (modell, szolgaltato) VALUES (?,?)",
                model, szolgaltato);
    }

    public List<Plane> readAllPlane(){
        List<Plane> result = jdbcTemplate.query("SELECT * FROM REPULOGEP",new PlaneDAO.PlaneRowMapper());
        return result.isEmpty()? null : result;
    }

    public Plane getPlaneById(int id){
        List<Plane> result = jdbcTemplate.query("SELECT * FROM REPULOGEP WHERE id = ?",new PlaneDAO.PlaneRowMapper(), id);
        return result.get(0);
    }

    public List<Plane> biggerThenAVG(){
        return jdbcTemplate.query("SELECT" +
                "    j.id, " +
                "    j.ar as modell, " +
                "    r.szolgaltato " +
                "FROM " +
                "    JARATOK j " +
                "JOIN " +
                "    REPULOGEP r ON j.repulo_id = r.id " +
                "WHERE " +
                "    j.ar > (" +
                "        SELECT AVG(j2.ar)" +
                "        FROM JARATOK j2" +
                "        WHERE j2.repulo_id = j.repulo_id" +
                "    )" +
                "ORDER BY " +
                "    j.ar DESC",new PlaneRowMapper());
    }

    public int updatePlane(int id, String model, String szolgaltato) {
        return jdbcTemplate.update("UPDATE REPULOGEP SET modell = ?, szolgaltato = ? WHERE id = ?", model, szolgaltato, id);
    }
    public int deletePlane(int id){
        return jdbcTemplate.update("DELETE FROM REPULOGEP WHERE id = ?", id);
    }



    public static class PlaneRowMapper implements RowMapper<Plane>{
        @Override
        public Plane mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Plane.builder()
                    .id(rs.getInt("ID"))
                    .model(rs.getString("MODELL"))
                    .serviceProvider(rs.getString("SZOLGALTATO"))
                    .build();
        }
    }
}
