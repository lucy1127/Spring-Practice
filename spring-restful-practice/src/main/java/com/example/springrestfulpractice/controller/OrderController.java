package com.example.springrestfulpractice.controller;


import com.example.springrestfulpractice.model.Meal;
import com.example.springrestfulpractice.model.Order;
import com.example.springrestfulpractice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //回傳json
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping()
    public List<Order>  getAllOrders() { //取得所有訂單
        List<Order> orderList = this.orderService.getAllOrders();
        return orderList;
    }

    @GetMapping("/{id}") //根據ID取得單筆訂單
    public Order getOrderById(@PathVariable int id) {
        Order order = this.orderService.getOrderById(id);
        return order;
    }
    @PostMapping()//新增訂單
    public Order createOrder(@RequestBody Order order) {
        Order createOrder = this.orderService.createOrder(order);
        return order;
    }
    @PutMapping("/{id}")//修改訂單
    public Order updateOrder(@PathVariable int id,@RequestBody Order order) {
        Order updateOrder = this.orderService.updateOrder(id,order);
        return updateOrder;
    }
    @DeleteMapping("/{id}")//刪除訂單
    public Order deleteOrder(@PathVariable int id) {
        Order deletedOrder = this.orderService.deleteOrder(id);
        return deletedOrder;
    }

}
