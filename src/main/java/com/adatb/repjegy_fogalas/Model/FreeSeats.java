package com.adatb.repjegy_fogalas.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FreeSeats {

    private int id;
    private String utvonal;
    private String idopont;
    private int ulohelyek;
    private int foglalthely;
    private int szabadhelyek;


}
