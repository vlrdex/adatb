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
public class Custom_Ticket {
    private String ticket_category;
    private int seat;
    private String insurance;
    private String name;
    private String email;
    private String startingTown;
    private String landingTown;
    private LocalDateTime startingTime;
    private LocalDateTime landingTime;
    private int price;

}
