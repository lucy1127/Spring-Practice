package com.example.project.controller.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SettlementRequest {

    private String branchNo;
    private String custSeq;

}
