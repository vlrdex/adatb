package com.adatb.repjegy_fogalas.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeStats {
    private  int id;
    private String indulasi_varos;
    private String erkezesi_varos;
    private String indulasi_idopont;
    private String szolgaltato;
    private int eladott_jegyek;
    private int alapar;
    private int biztositasi_bevetel;
    private int jegy_bevetel;
    private int teljes_bevetel;

}
