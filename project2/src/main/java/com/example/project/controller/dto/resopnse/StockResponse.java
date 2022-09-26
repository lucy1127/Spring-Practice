package com.example.project.controller.dto.resopnse;


import com.example.project.model.entity.Mstmb;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockResponse {
    private Mstmb mstmb;
    private String response;
}
