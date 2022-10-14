package com.example.Spring1.controller.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @NotBlank(message = "accNo cannot be null")
    @Length(max = 7,message = "The string length of accNo must be less than 7")
    private String accNo;// 結算帳戶帳號
    @NotNull(message = "price cannot be null")
    @DecimalMin(value = "0", inclusive = false, message = "price must be greater than 0")
    @Digits(integer = 10, fraction = 4, message = "digits of price is not correct")
    private BigDecimal price;
}
