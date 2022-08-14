package com.example.SpringDataOrder.controller.dto.request;


import com.example.SpringDataOrder.model.entity.Meal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {

    private String waiter;
    private List<Integer> mealList;
}
