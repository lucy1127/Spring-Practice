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
public class MgniRequest {
    @Length(max = 20, message = "The string length of id must be less than 20")
    private String id;
    @Length(max = 7,message = "The string length of memberCode must be less than 7")
    private String memberCode;
    @Length(max = 3,message = "The string length of bankCode must be less than 3")
    private String bankCode;
    @Length(max = 21,message = "The string length of bicaccNo must be no more than 21")
    private String bicaccNo;
    private String contactName;
    private String date;

    private int page;
    private int size;

}
