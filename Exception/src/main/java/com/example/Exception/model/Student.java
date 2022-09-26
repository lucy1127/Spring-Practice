package com.example.Exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    private String id;

    @NotBlank(message = "名字不可空")
    private String name;

    @NotNull(message = "年齡不可為空")
    private Integer age;
}
