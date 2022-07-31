package com.example.Menu.service;

import com.example.Menu.model.Meal;
import com.example.Menu.model.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    List<Order> orderList;
    List<Meal> mealList;
    public OrderService(List<Order> orderList) {
        this.orderList = new ArrayList<>();
        this.mealList = new ArrayList<>();
        this.mealList.add(new Meal("hamburger", 100, "This is delicious"));
        this.mealList.add(new Meal("coke", 60, "It's good to drink"));
        this.orderList.add(new Order(1, 100, "Bill",this.mealList));
        this.orderList.add(new Order(2, 200, "Louis",this.mealList));
    }

    public Order getOrderById(int seq) {
        for (int i = 0; i < this.orderList.size(); i++) {
            if (this.orderList.get(i).getSeq()==seq) {
                return this.orderList.get(i);
            }
        }
        return null;
    }
    public int getTotal(){
        int total = 0;
        for(int i=0;i<this.orderList.size();i++){
            total += orderList.get(i).getTotalPrice();
        }
        return total;
    }
    public List<Order> getOrderList(){
        return this.orderList;
    }


}
