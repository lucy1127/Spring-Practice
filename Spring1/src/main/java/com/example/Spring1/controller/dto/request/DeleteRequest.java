package com.example.Spring1.controller.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteRequest {

    @NotNull(message = "ID cannot be null")
    @Length(max = 20, message = "The string length of id must be less than 20")
    private String id;
}
