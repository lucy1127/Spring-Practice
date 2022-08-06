package com.example.Menu.controller;


import com.example.Menu.model.Order;
import com.example.Menu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping(value = "/order")

public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/{seq}")
    public String getOrderById(@PathVariable int seq, Model model) {
        Order order = this.orderService.getOrderById(seq);
        model.addAttribute("order", order);
        return "order";
    }
    @GetMapping("/income")
    public String getIncome(Model model){
        int total = this.orderService.getIncome();
        model.addAttribute("income",total);
        return "income";
    }

}
