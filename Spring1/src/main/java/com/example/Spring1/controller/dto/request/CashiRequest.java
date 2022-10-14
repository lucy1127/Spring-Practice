package com.example.Spring1.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CashiRequest {

    @Length(max = 20,message = "The string length of account must be no more than 20")
    private String id;
    @Length(max = 7,message = "The string length of account must be no more than 7")
    private String account;// 結算帳戶帳號
    @Length(max = 3,message = "The string length of currency must be less than 3")
    private String currency;

    private int page;
    private int size;
}
