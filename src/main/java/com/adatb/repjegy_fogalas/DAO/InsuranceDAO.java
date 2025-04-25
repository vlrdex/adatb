package com.adatb.repjegy_fogalas.DAO;
import com.adatb.repjegy_fogalas.Model.Insurance;
import com.adatb.repjegy_fogalas.Model.Town;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class InsuranceDAO {
    private final JdbcTemplate jdbcTemplate;

    public InsuranceDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createInsurance(String nev, int cost, String leiras){
        jdbcTemplate.update("INSERT INTO BIZTOSITASOK (nev, ar, leiras) VALUES (?,?,?)",
                nev, cost, leiras);
    }

    public List<Insurance> readAllInsurance(){
        List<Insurance> result = jdbcTemplate.query("SELECT * FROM BIZTOSITASOK",new InsuranceDAO.InsuranceRowMapper());
        return result.isEmpty()? null : result;
    }
    public Insurance getInsuranceById(int id){
        List<Insurance> result = jdbcTemplate.query("SELECT * FROM BIZTOSITASOK WHERE id = ?",new InsuranceDAO.InsuranceRowMapper(), id);
        return result.get(0);
    }

    public int updateInsurance(int id, String nev, int ar, String leiras){
        return jdbcTemplate.update("UPDATE BIZTOSITASOK SET nev = ?, ar = ?, leiras = ? WHERE id = ?", nev, ar, leiras, id);
    }
    public int deleteInsurance(int id){
        return jdbcTemplate.update("DELETE FROM BIZTOSITASOK WHERE id = ?", id);
    }



    public static class InsuranceRowMapper implements RowMapper<Insurance>{
        @Override
        public Insurance mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Insurance.builder()
                    .id(rs.getInt("ID"))
                    .name(rs.getString("NEV"))
                    .cost(rs.getInt("AR"))
                    .description(rs.getString("LEIRAS"))
                    .build();
        }
    }
}
