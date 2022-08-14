package com.example.SpringDataOrder.controller;


import com.example.SpringDataOrder.controller.dto.request.CreateOrderRequest;
import com.example.SpringDataOrder.controller.dto.request.UpdateMealRequest;
import com.example.SpringDataOrder.controller.dto.request.UpdateOrderRequest;
import com.example.SpringDataOrder.controller.dto.response.StatusResponse;
import com.example.SpringDataOrder.model.entity.Order;
import com.example.SpringDataOrder.service.MealService;
import com.example.SpringDataOrder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping() //Create
    public StatusResponse  createOrder(@RequestBody CreateOrderRequest request){
      String  result = this.orderService.createOrder(request);
      return new StatusResponse(result);
    }


    @GetMapping()
    public List<Order> getAllOrders(){
        List<Order> orderList = this.orderService.getOrderList();
        return orderList;
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable int id){
        Order order = this.orderService.getOrderById(id);
        return order;
    }

    @PutMapping("/{id}") //update
    public StatusResponse  updateOrder(@PathVariable int id, @RequestBody UpdateOrderRequest request){
        String  result = this.orderService.updateOrderById(id,request);
        return new StatusResponse(result);
    }
    @DeleteMapping("/{id}") //update
    public StatusResponse  deleteOrder(@PathVariable int id){
        String  result = this.orderService.deleteById(id);
        return new StatusResponse(result);
    }

}
