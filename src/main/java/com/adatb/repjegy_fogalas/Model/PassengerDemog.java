package com.adatb.repjegy_fogalas.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerDemog {
    private String korcsoport;
    private int utasok_szama;
    private int repulesek_szama;
    private int atlagos_jegyar;
    private int atlagos_biztositas_ara;
}
