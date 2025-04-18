package com.adatb.repjegy_fogalas.DAO;
import com.adatb.repjegy_fogalas.Model.Plane_Model;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class Plane_ModelDAO {
    private final JdbcTemplate jdbcTemplate;

    public Plane_ModelDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createInsurance(Plane_Model plane_Model){
        jdbcTemplate.update("INSERT INTO MODEL (model, nev, ulohelyek_szama) VALUES (?,?,?)",
                plane_Model.getModel(),plane_Model.getName(),plane_Model.getSeatsNumber());
    }

    public List<Plane_Model> readAllPlane_Model(){
        List<Plane_Model> result = jdbcTemplate.query("SELECT * FROM MODEL",new Plane_ModelDAO.Plane_ModelRowMapper());
        return result.isEmpty()? null : result;
    }

    public int updatePlane_Model(int id, String nev, int ulohely_szama){
        return jdbcTemplate.update("UPDATE MODEL SET nev = ? AND ulohelyek_szama = ? WHERE id = ?", nev, ulohely_szama, id);
    }
    public int deletePlane_Model(int id){
        return jdbcTemplate.update("DELETE FROM MODEL WHERE id = ?", id);
    }



    public static class Plane_ModelRowMapper implements RowMapper<Plane_Model>{
        @Override
        public Plane_Model mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Plane_Model.builder()
                    .model(rs.getString("MODEL"))
                    .name(rs.getString("NEV"))
                    .seatsNumber(rs.getInt("ULOHELYEK_SZAMA"))
                    .build();
        }
    }
}
