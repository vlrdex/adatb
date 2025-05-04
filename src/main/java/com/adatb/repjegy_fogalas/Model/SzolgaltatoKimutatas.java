package com.adatb.repjegy_fogalas.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SzolgaltatoKimutatas {
    private String szolgaltato;
    private int ossz_eladott_jegy;
    private int ossz_bevetel;
}
