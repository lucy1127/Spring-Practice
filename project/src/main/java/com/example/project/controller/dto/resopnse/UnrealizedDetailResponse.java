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
public class UnrealizedDetailResponse {
    private List<UnrealizedDetail> resultList;
    private String responseCode;
    private String message;
}
