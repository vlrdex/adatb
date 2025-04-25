package com.adatb.repjegy_fogalas.DAO;
import com.adatb.repjegy_fogalas.Model.Town;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class TownDAO {
    private final JdbcTemplate jdbcTemplate;

    public TownDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTown(String nev){
        jdbcTemplate.update("INSERT INTO VAROS (nev) VALUES (?)",
                nev);
    }

    public List<Town> readAllTown(){
        List<Town> result = jdbcTemplate.query("SELECT * FROM VAROS",new TownDAO.TownRowMapper());
        return result.isEmpty()? null : result;
    }
    public Town getTownById(int id){
        List<Town> result = jdbcTemplate.query("SELECT * FROM VAROS WHERE id = ?",new TownDAO.TownRowMapper(), id);
        return result.get(0);
    }

    public int updateTown(int id, String nev){
        return jdbcTemplate.update("UPDATE VAROS SET nev = ? WHERE id = ?", nev, id);
    }
    public int deleteTown(int id){
        return jdbcTemplate.update("DELETE FROM VAROS WHERE id = ?", id);
    }



    public static class TownRowMapper implements RowMapper<Town>{
        @Override
        public Town mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Town.builder()
                    .id(rs.getInt("ID"))
                    .name(rs.getString("NEV"))
                    .build();
        }
    }
}
