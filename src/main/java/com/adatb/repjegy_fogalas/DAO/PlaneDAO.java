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

    public void createPlane(Plane plane){
        jdbcTemplate.update("INSERT INTO REPULOGEP (id, model, szolgaltato) VALUES (?,?,?)",
                plane.getId(),plane.getModel(),plane.getServiceProvider());
    }

    public List<Plane> readAllPlane(){
        List<Plane> result = jdbcTemplate.query("SELECT * FROM REPULOGEP",new PlaneDAO.PlaneRowMapper());
        return result.isEmpty()? null : result;
    }

    public int updatePlane(int id, String model, int szolgaltato) {
        return jdbcTemplate.update("UPDATE REPULOGEP SET model = ? AND szolgaltato = ? WHERE id = ?", model, szolgaltato, id);
    }
    public int deletePlane(int id){
        return jdbcTemplate.update("DELETE FROM REPULOGEP WHERE id = ?", id);
    }



    public static class PlaneRowMapper implements RowMapper<Plane>{
        @Override
        public Plane mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Plane.builder()
                    .id(rs.getInt("ID"))
                    .model(rs.getString("MODEL"))
                    .serviceProvider(rs.getString("SZOLGALTATO"))
                    .build();
        }
    }
}
