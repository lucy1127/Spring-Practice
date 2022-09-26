package com.example.project.controller.dto.resopnse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InstantResponse {
    private String stock;
    private double dealPrice;
    private String mType;
}
