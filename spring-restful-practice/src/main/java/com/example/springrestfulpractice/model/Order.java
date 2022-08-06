package com.example.springrestfulpractice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// 使用 Lombok 加入 Getter, Setter, Constructor
@Getter
@Setter
@AllArgsConstructor
public class Order {
    private int seq;
    private int totalPrice;
    private String waiter;
    private List<Meal> mealList;
}