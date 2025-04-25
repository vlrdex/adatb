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
public class Flight {
    private int id;
    private int startingTown;
    private LocalDateTime startingTime;
    private int landingTown;
    private LocalDateTime landingTime;
    private int planeId;
    private int price;

}
