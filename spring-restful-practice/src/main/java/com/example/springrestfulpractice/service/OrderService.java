package com.example.springrestfulpractice.service;


import com.example.springrestfulpractice.model.Meal;
import com.example.springrestfulpractice.model.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private List<Order> orderList;

    public OrderService(){
        List<Meal> mealList1 = new ArrayList<>();
        mealList1.add(new Meal("hamburger",100,"It's good"));
        mealList1.add(new Meal("coke",60,"It's nice"));

        List<Meal> mealList2 = new ArrayList<>();
        mealList2.add(new Meal("pizza",200,"It's yummy"));
        mealList2.add(new Meal("Fried chicken",80,"It's great"));

        this.orderList = new ArrayList<>();
        this.orderList.add(new Order(1,160,"Bill",mealList1));
        this.orderList.add(new Order(2,280,"Louis",mealList2));

    }

    public List<Order> getAllOrders(){
        return this.orderList;
    }

    public  Order getOrderById(int id){
        for(Order orderId:this.orderList){
            if(id == orderId.getSeq()){
               return orderId;
            }
        }
        return null;
    }
    public Order createOrder(Order order) {
        this.orderList.add(order);
        return order;
    }
    public Order updateOrder(int id,Order order) {
        for(Order updatedOrder:this.orderList){
            if(id == updatedOrder.getSeq()){
                updatedOrder.setTotalPrice(order.getTotalPrice());
                updatedOrder.setWaiter(order.getWaiter());
                updatedOrder.setMealList(order.getMealList());
                return updatedOrder;
            }
        }
        return null;
    }
    public Order deleteOrder(int id) {
        for(Order order:this.orderList){
            if(id == order.getSeq()){
                this.orderList.remove(order);
                return order;
            }

        }
        return null;
    }
}
