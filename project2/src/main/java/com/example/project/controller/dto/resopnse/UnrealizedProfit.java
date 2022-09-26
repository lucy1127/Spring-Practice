package com.example.project.controller.dto.resopnse;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnrealizedProfit {

    private String stock;
    private String stockName;
    private Double nowPrice;
    private double sumRemainQty;
    private double sumFee;
    private double sumCost;
    private double sumMarketValue;
    private double sumUnrealProfit;
    private String sumProfitability;
    private List<UnrealizedDetail> detailList;

}
