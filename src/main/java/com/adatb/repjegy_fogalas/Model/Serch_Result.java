package com.adatb.repjegy_fogalas.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Serch_Result {
    int first_id;
    int second_id;
    Flight first_flight;
    Flight second_flight;

    public boolean is_direct(){
        return first_id==second_id;
    }

}
