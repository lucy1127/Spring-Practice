package com.example.SpringDataOrder.service;


import com.example.SpringDataOrder.controller.dto.request.CreateOrderRequest;
import com.example.SpringDataOrder.controller.dto.request.UpdateOrderRequest;
import com.example.SpringDataOrder.model.OrderRepository;
import com.example.SpringDataOrder.model.entity.Meal;
import com.example.SpringDataOrder.model.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {


    private List<Order> orderList;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MealService mealService;

    public String createOrder(CreateOrderRequest request){ //建立訂單
        Order order = new Order();


        List<Meal> mealList = request.getMealList()
                .stream()
                .map(id -> mealService.getMealById(id))
                .collect(Collectors.toList());

        //如果餐點沒有在meal裡..
        for(Meal meal:mealList){
            if(meal == null){
                return "Something is not in meal..";
            }
        }

        if(request.getWaiter()!=null){
            order.setWaiter(request.getWaiter());
            order.setMealList(mealList);
        }else{
            return "You lost to set waiter...";
        }

        int total = 0;
        for(Meal meal:mealList){
            total +=meal.getPrice();
        }

        order.setTotalPrice(total);

        orderRepository.save(order);
        return "Create success !";
    }
    public List<Order> getOrderList(){ //得到所有使用者訂單資訊
        List<Order> response = orderRepository.findAll();
        return response;
    }

    public Order getOrderById(int id){
        Order response = orderRepository.findById(id);
        return response;
    }

    public String updateOrderById(int id, UpdateOrderRequest request){
        Order order = orderRepository.findById(id);

        if(null == order){
            return "ID is not exist..";
        }

        //尋找request.mealList中的資料
        List<Meal> mealList = request.getMealList()
                .stream()
                .map(seq -> mealService.getMealById(seq))
                .collect(Collectors.toList());


        if(request.getWaiter()!=null){
            order.setWaiter(request.getWaiter());
            order.setMealList(mealList);
        }else{
            return "You lost to set waiter...";
        }

        int total = 0;
        for(Meal meal:mealList){
            total +=meal.getPrice();
        }

        if(total == 0){
            Long count = orderRepository.deleteById(id);
            return "Nothing in MealList ...Delete Order";
        }

        order.setTotalPrice(total);
        orderRepository.save(order);
        return "Update success !";
    }

    public String deleteById(int id){
        Order order = orderRepository.findById(id);

        if(null == order){
            return "ID is not exist..";
        }

        Long count = orderRepository.deleteById(id);
        return "Delete success !";
    }
}
