package com.example.project.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateHcmioRequest {

    private String tradeDate;
    private String docSeq;
    private String stock;
    private String bsType;
    private double price;
    private int qty;

}
