package com.example.Menu.controller;

import com.example.Menu.model.Order;
import com.example.Menu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/total")

public class TotalController {
    @Autowired
    OrderService orderService;

   @GetMapping("/all")
    public String getOrderById(Model model){
        int total = this.orderService.getTotal();
        model.addAttribute("total",total);
        List<Order> orderList = this.orderService.getOrderList();
        model.addAttribute("orderList",orderList);
        return "total";
    }
}

