package com.example.SpringDataOrder.controller;


import com.example.SpringDataOrder.controller.dto.request.CreateMealRequest;
import com.example.SpringDataOrder.controller.dto.request.UpdateMealRequest;
import com.example.SpringDataOrder.controller.dto.response.StatusResponse;
import com.example.SpringDataOrder.model.entity.Meal;
import com.example.SpringDataOrder.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meal")
public class MealController {

    @Autowired
    MealService mealService;

    @GetMapping()
    public List<Meal> getAllMeals(){
        List<Meal> mealList = this.mealService.getMealList();
        return mealList;
    }

    @GetMapping("/{id}")
    public Meal getMealId(@PathVariable int id){
        Meal meal = this.mealService.getMealById(id);
        return meal;
    }

    @PostMapping()
    public StatusResponse createMeal(@RequestBody CreateMealRequest request) {
        String mealCreate = this.mealService.createMeal(request);

        return new StatusResponse(mealCreate);
    }

    @PutMapping("/{id}")
    public StatusResponse updateMeal(@PathVariable int  id,@RequestBody UpdateMealRequest request) {
        String mealUpdate = this.mealService.updateMealById(id,request);

        return new StatusResponse(mealUpdate);
    }

    @DeleteMapping("/{id}")
    public StatusResponse deleteMealById(@PathVariable int id) {

        String mealDelete = this.mealService.deleteMealById(id);
        return new StatusResponse(mealDelete);
    }
}
