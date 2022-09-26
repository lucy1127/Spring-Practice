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
public class SumUnrealizedProfit {
    private List<UnrealizedProfit> resultList;
    private String responseCode;
    private String message;
}
