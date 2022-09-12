package com.example.project.controller.dto.resopnse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnrealizedDetail {

    private String tradeDate;
    private String branchNo;
    private String custSeq;
    private String docSeq;
    private String stock;
    private String stockName;
    private double buyPrice;
    private double nowPrice;
    private int qty;
    private double remainQty;
    private double fee;
    private double cost;
    private double marketValue;
    private double unrealProfit;
    private String profitability;
}
