package com.adatb.repjegy_fogalas.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {
    private int flightId;
    private int seat;
    private int insuranceId;
    private int jegykategoria;
    private String name;
    private String email;
}
