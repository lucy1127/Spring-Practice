package com.example.Menu.service;

import com.example.Menu.model.Meal;
import com.example.Menu.model.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    List<Order> orderList;

    public OrderService(List<Order> orderList) {

        List<Meal> mealList1 = new ArrayList<>();
        mealList1.add(new Meal("hamburger", 100, "This is delicious"));
        mealList1.add(new Meal("coke", 60, "It's good to drink"));

        List<Meal> mealList2 = new ArrayList<>();
        mealList2.add(new Meal("hamburger", 200, "This is delicious"));
        mealList2.add(new Meal("coke", 60, "It's good to drink"));

        this.orderList = new ArrayList<>();
        this.orderList.add(new Order(1, "Bill", mealList1));
        this.orderList.add(new Order(2, "Louis", mealList2));
    }

    public Order getOrderById(int seq) {
        for (int i = 0; i < this.orderList.size(); i++) {
            if (this.orderList.get(i).getSeq() == seq) {
                return this.orderList.get(i);
            }
        }
        return null;
    }

    public int getIncome() {
        int total = 0;
        for (int i = 0; i < this.orderList.size(); i++) {
            total += orderList.get(i).getTotalPrice();
        }
        return total;
    }

    public List<Order> getOrderList() {
        return this.orderList;
    }


}
