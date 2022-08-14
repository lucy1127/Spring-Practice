package com.example.SpringDataOrder.service;


import com.example.SpringDataOrder.controller.dto.request.CreateMealRequest;
import com.example.SpringDataOrder.controller.dto.request.UpdateMealRequest;
import com.example.SpringDataOrder.model.MealRepository;
import com.example.SpringDataOrder.model.entity.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealService {
    List<Meal> mealList;

    @Autowired
    private MealRepository mealRepository;

    public List<Meal> getMealList(){ //得到所有菜單資料
        List<Meal> response = mealRepository.findAll();

        return response;
    }

    public Meal getMealById(int id){ //得到單筆菜單資訊
        Meal response = mealRepository.findById(id);
        return response;
    }

    public String createMeal(CreateMealRequest request){

        Meal meal = new Meal();

        if(request.getName() != null && request.getPrice() != null) {
            meal.setId(request.getId());
            meal.setName(request.getName());
            meal.setPrice(request.getPrice());
        }else{
            return "You lost something...(name or price)";
        }

        mealRepository.save(meal);
        return "Create success !";

    }

    public String updateMealById(int id , UpdateMealRequest request){
        Meal meal = mealRepository.findById(id);

        if(null == meal){
            return "ID is not exist..";
        }

        if(request.getName() != null && request.getPrice() != null) {
            meal.setName(request.getName());
            meal.setPrice(request.getPrice());
        }else{
            return "You lost something...(name or price)";
        }

        mealRepository.save(meal);
        return "Update success !";

    }

    public String deleteMealById(int id){

        Meal meal = mealRepository.findById(id);
        if(null == meal){
            return "ID is not exist..";
        }

        Long count = mealRepository.deleteById(id);

        return "Delete success !";
    }
}
