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

    public void createPlaneModel(String model, String nev, int ulohelyekSzama){
        jdbcTemplate.update("INSERT INTO MODELL (modell, nev, ulohelyek_szama) VALUES (?,?,?)",
                model, nev, ulohelyekSzama);
    }

    public List<Plane_Model> readAllPlane_Model(){
        List<Plane_Model> result = jdbcTemplate.query("SELECT * FROM MODELL",new Plane_ModelDAO.Plane_ModelRowMapper());
        return result.isEmpty()? null : result;
    }
    public List<Plane_Model> getAveragePriceByModell(){
        List<Plane_Model> result = jdbcTemplate.query("SELECT m.nev AS modell_nev, AVG(j.ar) AS atlag_ar\n" +
                "FROM jaratok j\n" +
                "LEFT JOIN repulogep r ON j.repulo_id = r.id\n" +
                "LEFT JOIN modell m ON r.modell = m.modell\n" +
                "GROUP BY m.nev\n" +
                "ORDER BY atlag_ar DESC", new Plane_ModelDAO.Plane_ModelRowMapper());
        return result.isEmpty()? null : result;
    }

    public Plane_Model getPlaneModelById(String id){
        List<Plane_Model> result = jdbcTemplate.query("SELECT * FROM MODELL WHERE modell = ?",new Plane_ModelDAO.Plane_ModelRowMapper(), id);
        return result.get(0);
    }

    public int updatePlane_Model(String id, String nev, int ulohely_szama){
        return jdbcTemplate.update("UPDATE MODELL SET nev = ?, ulohelyek_szama = ? WHERE modell = ?", nev, ulohely_szama, id);
    }
    public int deletePlane_Model(String id){
        return jdbcTemplate.update("DELETE FROM MODELL WHERE modell = ?", id);
    }



    public static class Plane_ModelRowMapper implements RowMapper<Plane_Model>{
        @Override
        public Plane_Model mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Plane_Model.builder()
                    .model(rs.getString("MODELL"))
                    .name(rs.getString("NEV"))
                    .seatsNumber(rs.getInt("ULOHELYEK_SZAMA"))
                    .build();
        }
    }
}
