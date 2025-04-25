package com.adatb.repjegy_fogalas.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {
    private int id;
    private int flight_id;
    private int ticket_category_id;
    private int seat;
}
