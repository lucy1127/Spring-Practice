package com.example.Menu.controller;

import com.example.Menu.model.Meal;
import com.example.Menu.model.Order;
import com.example.Menu.service.MealService;
import com.example.Menu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/order")

public class OrderController {
    @Autowired
    OrderService orderService;


    @GetMapping("/{seq}")
    public String getOrder(@RequestParam int seq, Model model){
        Order order = this.orderService.getOrderBySeq(seq);
        model.addAttribute("order",order);
        return "order";
    }

}
