package com.example.project.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddBalanceRequest {
    private String branchNo;
    private String custSeq;
    private String tradeDate;
    private String stock;
    private Double buyPrice;
    private Integer qty;
}
