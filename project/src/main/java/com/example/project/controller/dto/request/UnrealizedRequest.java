package com.example.project.controller.dto.request;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnrealizedRequest implements Serializable {

    private String branchNo;
    private String custSeq;
    private String stock;

    private String minProfit;
    private String maxProfit;
}
