package com.example.project.controller.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMstmbRequest {

    private String stock;

    private String stockName;

    private String marketType;

    private double curPrice;

    private String currency;
}



