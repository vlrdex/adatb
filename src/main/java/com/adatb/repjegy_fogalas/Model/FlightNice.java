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
public class FlightNice {

        private int id;
        private String startingTown;
        private LocalDateTime startingTime;
        private String landingTown;
        private LocalDateTime landingTime;
        private String planeId;
        private int price;


}
